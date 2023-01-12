import sqlalchemy as db

import pandas as pd
from sqlalchemy.orm import sessionmaker

engine = db.create_engine('sqlite:///test1.sqlite') #Create test.sqlite automatically
connection = engine.connect()
metadata = db.MetaData()
Session = sessionmaker(bind=engine)
session = Session()
# res = db.inspect(engine).has_table("emp")
# print(res)

emp = db.Table('emp', metadata,
              db.Column('Id', db.Integer(), primary_key=True),
              db.Column('name', db.String(255), nullable=False),
              db.Column('salary', db.Float(), default=100.0),
              db.Column('active', db.Boolean(), default=True)
              )

metadata.create_all(engine) #Creates the table

#Inserting record one by one
# query = db.insert(emp).values(Id=1, name='naveen', salary=60000.00, active=True) 
# ResultProxy = connection.execute(query)


# #Inserting many records at ones
# query = db.insert(emp) 
# values_list = [{'Id':'2', 'name':'ram', 'salary':80000, 'active':False},
#                {'Id':'3', 'name':'ramesh', 'salary':70000, 'active':True}]
# ResultProxy = connection.execute(query,values_list)
from sqlalchemy.sql import and_


# result = connection.execute(db.select([emp]).where(emp.c.name == "naveen")).fetchall()

query = db.select(emp.c.salary,emp.c.name).where((emp.c.name == "naveen")|(emp.c.name == "ram")) # & is also supported
result = connection.execute(query).fetchall()
result = connection.execute(query).first()
result = session.execute(query).all()
print(result)
# result[:3]
# for row in result:
#     print(row.__dict__)



from sqlalchemy.orm import sessionmaker
Session = sessionmaker(bind=engine)
session = Session()
# import sqlalchemy as db
# from sqlalchemy.ext.declarative import declarative_base
 
# Base = declarative_base()
 
# # DEFINE THE ENGINE (CONNECTION OBJECT)
# engine = db.create_engine(
#     "sqlite:///test1.sqlite")
 
# # CREATE THE TABLE MODEL TO USE IT FOR QUERYING
# class Students(Base):
 
#     __tablename__ = 'emp'
#     id = db.Column('Id', db.Integer()),
#     name = db.Column('name', db.String(255), nullable=False),
#     salary = db.Column('salary', db.Float(), default=100.0),
#     active = db.Column('active', db.Boolean(), default=True)
 
 
# # CREATE A SESSION OBJECT TO INITIATE QUERY
# # IN DATABASE
# Session = sessionmaker(bind=engine)
# session = Session()
 
# # SELECT first_name FROM students
# result = session.query(Students.id)
# print("Query 1:", result)
# exit(0)
 
# # SELECT first_name, last_name, course
# # FROM students
# result = result.add_columns(Students.last_name,
#                             Students.course)
# print("Query 2:", result)
 
# # VIEW THE ENTRIES IN THE RESULT
# for r in result:
#     print(r.first_name, "|", r.last_name, "|", r.course)




import sqlalchemy
from sqlalchemy import *
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# print "sqlalchemy version:",sqlalchemy.__version__ 

engine = create_engine('sqlite:///:memory:', echo=False)
metadata = MetaData()
users_table = Table('users', metadata,
     Column('id', Integer, primary_key=True),
     Column('name', String),
)
metadata.create_all(engine) 

class User(declarative_base()):
    __tablename__ = 'users'
    
    id = Column(Integer, primary_key=True)
    name = Column(String)
    
    def __init__(self, name):
        self.name = name

Session = sessionmaker(bind=engine)
session = Session()

user1 = User("anurag")
session.add(user1)
session.commit()


res = session.query(User).all()
for row in res:
    print(row.__dict__)
print(res)
