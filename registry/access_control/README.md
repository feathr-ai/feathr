# Access Control Notes (WIP)

A Global Switch `RBAC_ENABLED` is required to set as `True` to turn on the access control protection feature.

Please note that in this version, only access control management API and UI experience are included. Supported scenarios status are tracked below:

- General Fundation:
  - [x] Access Control Abstrct Class
  - [x] API Spec Contents for Access Control Management APIs
  - [ ] API Sepc Contents for Registry API Access Control
- SQL Implementaion:
  - [x] `userroles` table CUD through FastAPI
  - [x] `userroles` table schema & test data
  - [x] Enable/Disable with `RBAC_ENABLE` configuration
  - [ ] Initialize default Admin role for project creator: After `create_project` API is ready
- UI Experience
  - [x] Hidden page `../management` for global admin to make CUD requests to `userroles` table
  - [x] Use id token in Management API Request headers to identify requestor
  - [ ] Protect SQL Registry API with Access Control: After `create_project` API is ready
- Future Enhancements:
  - [ ] Functional in Feathr Client
  - [ ] Support Security Group scenario in Access Control
  - [ ] Support AAD Groups
  - [ ] Support Other OAuth Providers
  