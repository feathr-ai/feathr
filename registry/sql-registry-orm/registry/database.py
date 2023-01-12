from abc import ABC, abstractmethod
from contextlib import contextmanager
import logging
import threading
import os
import sqlite3

# Checks if the platform is Max (Darwin).
# If so, imports _scproxy that is necessary for pymssql to work on MacOS
import platform
if platform.system().lower().startswith('dar'):
    import _scproxy

import pymssql


providers = []

class DbConnection(ABC):
    @abstractmethod
    def query(self, sql: str, *args, **kwargs) -> list[dict]:
        pass

def quote(id):
    if isinstance(id, str):
        return f"'{id}'"
    else:
        return ",".join([f"'{i}'" for i in id])


def parse_conn_str(s: str) -> dict:
    """
    TODO: Not a sound and safe implementation, but useful enough in this case
    as the connection string is provided by users themselves.
    """
    parts = dict([p.strip().split("=", 1)
                 for p in s.split(";") if len(p.strip()) > 0])
    server = parts["Server"].split(":")[1].split(",")[0]
    return {
        "host": server,
        "database": parts["Initial Catalog"],
        "user": parts["User ID"],
        "password": parts["Password"],
        # "charset": "utf-8",   ## For unknown reason this causes connection failure
    }


import sqlalchemy as db

import pandas as pd

class SQLiteConnection(DbConnection):
    @staticmethod
    def connect(autocommit = True):
        # conn_str = os.environ["CONNECTION_STR"]
        conn_str = "Server="
        if "Server=" not in conn_str:
            logging.debug("`CONNECTION_STR` is not in ADO connection string format")
            return None
        params = {
        "host": "localhost",
        "database": "feathr_registry.db",
        # "charset": "utf-8",   ## For unknown reason this causes connection failure
    }
        if not autocommit:
            params["autocommit"] = False
        return SQLiteConnection(params)

    def __init__(self, params):
        self.params = params
        self.make_connection()
        self.mutex = threading.Lock()

        engine = db.create_engine('sqlite:////tmp/feathr_registry.db') #Create test.sqlite automatically
        connection = engine.connect()
        metadata = db.MetaData()

        entities_table = db.Table('entities', metadata,
                    db.Column('entity_id', db.String(50),nullable=False, primary_key=True),
                    db.Column('qualified_name', db.String(200), nullable=False),
                    db.Column('entity_type', db.String(100),nullable=False),
                    db.Column('attributes', db.String(2000), nullable=False) #TODO: sqlite doesn't enforce length but others might
                    )
        edges_table = db.Table('edges', metadata,
                    db.Column('edge_id', db.String(50),nullable=False, primary_key=True),
                    db.Column('from_id', db.String(50), nullable=False),
                    db.Column('to_id', db.String(20), nullable=False),
                    db.Column('conn_type', db.String(20), nullable=False) 
                    )
        metadata.create_all(engine) #Creates the table


    def make_connection(self):
        # use ` check_same_thread=False` otherwise an error like 
        # sqlite3.ProgrammingError: SQLite objects created in a thread can only be used in that same thread. The object was created in thread id 140309046605632 and this is thread id 140308968896064.
        # will be thrown out
        # TODO: remove hard coded path
        self.conn = sqlite3.connect("/tmp/feathr_registry.db",  check_same_thread=False)
        self.conn.row_factory = sqlite3.Row

    def query(self, sql: str, *args, **kwargs) -> list[dict]:
        """
        Make SQL query and return result
        """
        print(f"SQL: `{sql}`")
        # NOTE: Only one cursor is allowed at the same time
        retry = 0
        while True:
            try:
                c = self.conn.cursor()
                c.execute(sql, *args, **kwargs)
                return c.fetchall()
            except sqlite3.OperationalError:
                logging.warning("Database error, retrying...")
                # Reconnect
                self.make_connection()
                retry += 1
                if retry >= 3:
                    # Stop retrying
                    raise
                pass

    @contextmanager
    def transaction(self):
        """
        Start a transaction so we can run multiple SQL in one batch.
        User should use `with` with the returned value, look into db_registry.py for more real usage.

        NOTE: `self.query` and `self.execute` will use a different MSSQL connection so any change made
        in this transaction will *not* be visible in these calls.

        The minimal implementation could look like this if the underlying engine doesn't support transaction.
        ```
        @contextmanager
        def transaction(self):
            try:
                c = self.create_or_get_connection(...)
                yield c
            finally:
                c.close(...)
        ```
        """
        conn = None
        cursor = None
        try:
            # As one MssqlConnection has only one connection, we need to create a new one to disable `autocommit`
            conn = SQLiteConnection.connect().conn
            cursor = conn.cursor()
            yield cursor
        except Exception as e:
            logging.warning(f"Exception: {e}")
            if conn:
                conn.rollback()
            raise e
        finally:
            if conn:
                conn.commit()

providers.append(SQLiteConnection)


def connect(*args, **kargs):
    for p in providers:
        ret = p.connect(*args, **kargs)
        if ret is not None:
            return ret
    raise RuntimeError("Cannot connect to database")
