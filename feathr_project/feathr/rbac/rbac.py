from enum import Enum
from loguru import logger
import pymssql

# Please fill in the configs and test user to test this RBAC prototype
rbacSqlConfig = dict({
    "server": "",
    "database": "",
    "user": "",
    "password": ""
})

TEST_USER = ""

# A Enum to restrict Role inputs
class Role(str, Enum):
    admin = "admin",
    consumer ="consumer",
    producer = "producer"
    monitor = "monitor",
    default = "default"

#TODO(Yuqing): Enable Underneath Access and integrate with Feature Registry APIs
Access = ["Registry.Read", "Registry.Write",
          "Registry.Management", "Compute.LogReview"]

RoleAccessMapping = {
    Role.admin: Access,
    Role.consumer: Access[:1],
    Role.producer: Access[:3],
    Role.monitor: Access[2:],
    Role.default: []
}


#TODO(Yuqing): Integrate a general user info context for API layer to consume
class UserContext():
    def __init__(self, userInfo=None):
        self.name = userInfo["name"]

# A Basic class with sql connections and common sql functions
class MSSQL():
    def __init__(self, config=None):
        self.connection = pymssql.connect(
            server=config["server"],
            user=config["user"],
            password=config["password"],
            database=config["database"],
        )
    
    def checkTableExists(self, tableName):
        with self.connection.cursor() as cursor:
            cursor.execute(f"SELECT COUNT(*) FROM information_schema.tables WHERE table_name = '{tableName}'")
            if cursor.fetchone()[0] == 1:
                return True
        return False

    def execute(self, query: str):
        with self.connection.cursor() as cursor:
            try:
                cursor.execute(query)
            except Exception as e:
                logger.error(f"SQL Query {query} failed with error：{e}")
        self.connection.commit()

class rbacSql(object):
    def __init__(self, rbacSqlConfig=None):
        self.tableName = "userrole"
        self.rbacSql = MSSQL(rbacSqlConfig)
        self.userrole = {}
        self.initUserRoleTable()
        self.getUserRoleMapping()
    
    def getUserRoleMapping(self):
        self.userrole = {}
        with self.rbacSql.connection.cursor() as cursor:
            cursor.execute("SELECT user_name, role_name FROM userrole WHERE delete_time is null")
            row = cursor.fetchone()
            while row:
                user_name = str(row[0]).strip()
                role_name = str(row[1]).strip()
                if self.userrole.__contains__(user_name):
                    self.userrole[user_name].append(role_name)
                else:
                    self.userrole[user_name] = [role_name]
                row=cursor.fetchone()
        logger.info(f"User Role mapping refreshed: {self.userrole}")

    def assignUserRole(self, user_name:str, role_name:Role, create_reason:str):
        user_name = user_name.lower().strip()
        role_name = role_name
        assignedRoles = self.getUserRole(user_name)
        if role_name in assignedRoles:
            logger.info(f"User: {user_name} already assigned with Role: {role_name}.")
        else:
            query = f"""INSERT INTO {self.tableName} ([user_name], [role_name], [create_time], [create_reason]) 
            VALUES ('{user_name}', '{role_name}', getutcdate(), '{create_reason}')"""
            self.rbacSql.execute(query)
            logger.info(f"User: {user_name} are assigned with Role: {role_name} with reasone: {create_reason}.")
            self.getUserRoleMapping()
    
    def getUserRole(self, user_name:str):
        user_name = user_name.lower()
        if self.userrole.__contains__(user_name):
            logger.info(f"User: {user_name} has been assigned with Role: {self.userrole[user_name]}.")
            return self.userrole[user_name]
        else:
            logger.info(f"User: {user_name} doesn't have any role assigned. Return {Role.default}.")
            return [str(Role.default)]

    def removeUserRole(self, user_name:str, role_name:str, delete_reason:str):
        if role_name in self.userrole[user_name]:
            query = f'''UPDATE {self.tableName} SET
            [delete_reason] = '{delete_reason}',
            [delete_time] = getutcdate() 
            WHERE [user_name] = '{user_name}' and [role_name] = '{role_name}' and [delete_time] is null'''
            self.rbacSql.execute(query)
            logger.info(f"User: {user_name} is removed from Role {role_name}.")
            self.getUserRoleMapping()
        else:
            logger.info(f"User: {user_name} doesn't have this Role {role_name}")

    def initUserRoleTable(self):
        if not self.rbacSql.checkTableExists(self.tableName):
            userRoleTable = f"""create table {self.tableName}(
                record_id int IDENTITY(1,1), 
                user_name char(20) not null,
                role_name char(20) not null,
                create_time datetime not null,
                create_reason char(50) not null,
                delete_time datetime,
                delete_reason char(50))"""
            logger.info(userRoleTable)
            self.rbacSql.execute(userRoleTable)
            sampleData = f"""INSERT INTO {self.tableName} ([user_name], [role_name], [create_time], [create_reason]) 
                VALUES ('{TEST_USER}', '{Role.default}', getutcdate(), 'test_init')"""
            logger.info(sampleData)
            self.rbacSql.execute(sampleData)
            logger.info(f"Table {self.tableName} not exist, create a new one with sample data")
        else:
            logger.info(f"Table {self.tableName} already exist")

    def getUserACL(self, roles):
        accessList = []
        for role in set(roles):
            try:
                access = RoleAccessMapping[role]
                accessList += access
            except Exception as e:
                logger.error(f"Access parse failed with error：{e}")
        logger.info(f"Access list are {set(accessList)} based on Role {roles}")
        return set(accessList)


rbac = rbacSql(rbacSqlConfig)
rbac.getUserRoleMapping()
rbac.assignUserRole(TEST_USER, Role.consumer, "test")
rbac.getUserRole(TEST_USER)
rbac.assignUserRole(TEST_USER, Role.producer, "test")
rbac.getUserRole(TEST_USER)
rbac.removeUserRole(TEST_USER, Role.producer, "test remove")
roles = rbac.getUserRole(TEST_USER)
rbac.getUserACL(roles)