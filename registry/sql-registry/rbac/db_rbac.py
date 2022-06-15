from typing import List
from rbac import RBAC
from rbac.models import Access, RoleAccessMapping, UserRole, RoleType
from registry import connect


class DbRBAC(RBAC):
    def __init__(self):
        self.conn = connect()

    def get_users(self) -> list[str]:
        ret = self.conn.query(
            f"select user_name from userroles")
        return list([r["user_name"] for r in ret])

    def get_userroles(self) -> list[UserRole]:
        rows = self.conn.query(
            fr"""select record_id, project_name, user_name, role_name, create_reason, create_time, delete_reason, delete_time
            from userroles
            where delete_reason is null""")
        ret = []
        for row in rows:
            ret.append(UserRole(**row))
        return ret

    def get_userrole(self, user_name: str) -> list[UserRole]:
        rows = self.conn.query(
            fr"""select record_id, project_name, user_name, rolename, create_reason, create_time, delete_reason, delete_time
            from userroles
            where delete_reason is null and user_name ='{user_name}'""")
        ret = []
        for row in rows:
            ret.append(UserRole(**row))
        return ret

    def add_userrole(self, project_name: str, user_name: str, role_name: str, create_reason: str):
        query = f"""insert into userroles (project_name, user_name, role_name, create_reason, create_time) 
            values ('{project_name}','{user_name}','{role_name}','{create_reason}', getutcdate())"""
        return self.conn.update(query)

    def delete_userrole(self, project_name: str, user_name: str, role_name: str, delete_reason: str):
        query = f'''UPDATE userroles SET
            [delete_reason] = '{delete_reason}',
            [delete_time] = getutcdate() 
            WHERE [user_name] = '{user_name}' and [project_name] = '{project_name}' and [role_name] = '{role_name}' 
            and [delete_time] is null'''
        return self.conn.update(query)

    def validate_access(self, userrole: UserRole, access: Access):
        if userrole.project_name == access.project_name and access.access_name in RoleAccessMapping[userrole.role_name]:
            return True
        else:
            return False
