import sqlite3
con = sqlite3.connect("/tmp/feathr_registry.db")
cur = con.cursor()

cur.execute("CREATE TABLE IF NOT EXISTS entities(entity_id varchar PRIMARY KEY,qualified_name varchar, entity_type varchar, attributes NVARCHAR) ")
cur.execute("CREATE TABLE IF NOT EXISTS edges(edge_id varchar PRIMARY KEY,from_id varchar, to_id varchar, conn_type varchar) ")
sql_query = """SELECT name FROM sqlite_master WHERE type='table';"""
cur.execute(sql_query)
print(cur.fetchall())
cur.close()