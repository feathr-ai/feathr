from abc import ABC, abstractmethod
import threading
from distutils.log import debug, warn
import os
import pymssql


providers = []

class DbConnection(ABC):
    @abstractmethod
    def execute(self, sql: str, *args, **kwargs) -> list[dict]:
        pass

def quote(id):
    if isinstance(id, str):
        return f"'{id}'"
    else:
        return ",".join([f"'{i}'" for i in id])


def parse_conn_str(s: str) -> dict:
    """
    TODO: Not a sound and safe implementation
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


class MssqlConnection(DbConnection):
    @staticmethod
    def connect(*args, **kwargs):
        conn_str = os.environ["CONNECTION_STR"]
        if "Server=" not in conn_str:
            debug("`CONNECTION_STR` is not in ADO connection string format")
            return None
        return MssqlConnection(parse_conn_str(conn_str))

    def __init__(self, params):
        self.params = params
        self.make_connection()
        self.mutex = threading.Lock()
        
    def make_connection(self):
        self.conn = pymssql.connect(**self.params)

    def execute(self, sql: str, *args, **kwargs) -> list[dict]:
        debug(f"SQL: `{sql}`")
        # NOTE: Only one cursor is allowed at the same time
        retry = 0
        while True:
            try:
                with self.mutex:
                    c = self.conn.cursor(as_dict=True)
                    c.execute(sql, *args, **kwargs)
                    return c.fetchall()
            except pymssql.OperationalError:
                warn("Database error, retrying...")
                # Reconnect
                self.make_connection()
                retry += 1
                if retry >= 3:
                    # Stop retrying
                    raise
                pass


providers.append(MssqlConnection)


def connect():
    for p in providers:
        ret = p.connect()
        if ret is not None:
            return ret
    raise RuntimeError("Cannot connect to database")