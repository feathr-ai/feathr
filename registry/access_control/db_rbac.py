from access_control import config
from common.database import connect
from access_control.models import AccessType, UserRole, RoleType, SUPER_ADMIN_SCOPE
from access_control.interface import RBAC
import os
import logging

class DbRBAC(RBAC):
    def __init__(self):
        if not os.environ.get("CONNECTION_STR"):
            os.environ["CONNECTION_STR"] = config.CONNECTION_STR
        self.conn = connect()
        # cached user role records
        self._refresh_cache()

    def _refresh_cache(self):
        #TODO: add user role cache refresh interval, e.g. daily
        self.userroles = self._get_userroles()
        self.global_admin = self._get_global_admin_users()

    def _get_userroles(self) -> list[UserRole]:
        """query all the active user role records in SQL table
        """
        rows = self.conn.query(
            fr"""select record_id, project_name, user_name, role_name, create_by, create_reason, create_time, delete_by, delete_reason, delete_time
            from userroles
            where delete_reason is null""")
        ret = []
        for row in rows:
            r = UserRole(**row)
            ret.append(UserRole(**row))
        logging.info(f"{ret.__len__} user roles are get.")
        return ret

    def _get_global_admin_users(self) -> list[str]:
        return [u.user_name for u in self.userroles if (u.project_name == SUPER_ADMIN_SCOPE and u.role_name == RoleType.ADMIN)]

    def validate_project_access_users(self, project: str, user: str, access: str = AccessType.READ) -> bool:
        for u in self.userroles:
            if (u.user_name == user and u.project_name in [project, SUPER_ADMIN_SCOPE] and (access in u.access)):
                return True
        return False

    def get_userroles_by_user(self, user_name: str, role_name: str = None) -> list[UserRole]:
        """query the active user role of certain user
        """
        query = fr"""select record_id, project_name, user_name, role_name, create_by, create_reason, create_time, delete_by, delete_reason, delete_time
            from userroles
            where delete_reason is null and user_name ='{user_name}'"""
        if role_name:
            query += fr"and role_name = '{role_name}'"
        rows = self.conn.query(query)
        ret = []
        for row in rows:
            ret.append(UserRole(**row))
        return ret

    def get_userroles_by_project(self, project_name: str, role_name: str = None) -> list[UserRole]:
        """query the active user role of certain project.
        """
        query = fr"""select record_id, project_name, user_name, role_name, create_by, create_reason, create_time, delete_reason, delete_time
            from userroles
            where delete_reason is null and project_name ='{project_name}'"""
        if role_name:
            query += fr"and role_name = '{role_name}'"
        rows = self.conn.query(query)
        ret = []
        for row in rows:
            ret.append(UserRole(**row))
        return ret

    def add_userrole(self, project_name: str, user_name: str, role_name: str, create_reason: str, by: str):
        """insert new user role relationship into sql table
        """
        # check if record already exist
        for u in self.userroles:
            if u.project_name == project_name and u.user_name == user_name and u.role_name == role_name:
                logging.warning(
                    f"User {user_name} already have {role_name} role of {project_name}.")
                return True

        # insert new record
        query = f"""insert into userroles (project_name, user_name, role_name, create_by, create_reason, create_time)
            values ('{project_name}','{user_name}','{role_name}','{by}' ,'{create_reason}', getutcdate())"""
        self.conn.update(query)
        self._refresh_cache()
        return

    def delete_userrole(self, project_name: str, user_name: str, role_name: str, delete_reason: str, by: str):
        """mark existing user role relationship as deleted with reason
        """
        query = fr"""UPDATE userroles SET
            [delete_by] = '{by}',
            [delete_reason] = '{delete_reason}',
            [delete_time] = getutcdate()
            WHERE [user_name] = '{user_name}' and [project_name] = '{project_name}' and [role_name] = '{role_name}'
            and [delete_time] is null"""
        self.conn.update(query)
        self._refresh_cache()
        return

    def init_userrole(self, creator_name: str, project_name: str):
        """initialize user role relationship when a new project is created
        TODO: Add init user role to every new project call
        """
        create_by = "system"
        create_reason = "creator of project, get admin by default."
        query = fr"""insert into userroles (project_name, user_name, role_name, create_by, create_reason, create_time)
            values ('{project_name}','{creator_name}','{RoleType.ADMIN}','{create_by}','{create_reason}', getutcdate())"""
        return self.conn.update(query)
