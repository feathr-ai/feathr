# Feathr Registry Access Control Gateway Specifications

## Registry API with Access Control Gateway

**Access Control Gateway** is an access control **Plugin** component of feature registry API. It can work with different type of backend registry. When user enables this component, registry requests will be validated in a gateway as below flow chart:

```mermaid
flowchart TD
    A[Get Registry API Request] --> B{Is Id Token Valid?};
    B -- No --> D[Return 401];
    B -- Yes --> C{Have Permission?};
    C -- No --> F[Return 403];
    C -- Yes --> E[Call Downstream API*];
    E --> G{API Service Available?}
    G -- No --> I[Return 503]
    G -- Yes --> H[Return API Results]
```

If Access control plugin is NOT enabled, the flow will start from **Call Downstream API***

## Access Control Registry API

- For all **get** requests, check **read** permission for certain project.
- For all **post** request, check **write** permission for certain project.
- For all **access control management** request, check **manage** permission for certain project.
- In case of feature level query, will verify the parent project access of the feature.
- Registry API calls and returns will be transparently transferred.

## Management Rules

### Initialize `userroles` table

Users needs to create a `userroles` table with [schema.sql](scripts/schema.sql) at the very first place. The process will be similar with SQL Registry `bacpac` initialization.

### Initialize `userroles` records

In current version, user needs to manually initialize `userroles` table admins in SQL table.
When `create_registry` and `create_project` API is enabled, default admin role will be assigned to the creator.
Admin roles can add or delete roles in management UI page or through management API.

### Environment Settings

`ENABLE_RBAC` needs to be set to deploy a registry backend with access control plugin.

| Variable| Description|
|---|---|
| RBAC_CONNECTION_STR| Connection String of the SQL database that host access control tables, required.|
| RBAC_API_BASE| Aligned API base|
| RBAC_REGISTRY_URL| The downstream Registry API Endpoint|
| RBAC_AAD_INSTANCE | Instance like "https://login.microsoftonline.com" |
| RBAC_AAD_TENANT_ID| Used get auth url together with `RBAC_AAD_INSTANCE`|
| RBAC_API_AUDIENCE| Used as audience to decode jwt tokens|

## Notes

Supported scenarios status are tracked below:

- General Foundations:
  - [x] Access Control Abstract Class
  - [x] API Spec Contents for Access Control Management APIs
  - [x] API Spec Contents for Registry API Access Control
  - [x] Separate Registry API and Access Control into different implementation
  - [x] A docker file to contain all required component for deployments
- SQL Implementation:
  - [x] `userroles` table CRUD through FastAPI
  - [x] `userroles` table schema & test data, could be used to make `.bacpac` file for SQL table initialize.
  - [x] Initialize default Project Admin role for project creator
  - [ ] Initialize default Global Admin Role for workspace creator
- UI Experience
  - [x] Hidden page `../management` for global admin to make CUD requests to `userroles` table
  - [x] Use id token in Management API Request headers to identify requestor
- Future Enhancements:
  - [x] Support AAD Application token  
  - [x] Support OAuth tokens with `email` attributes
  - [ ] Functional in Feathr Client
  - [ ] Support AAD Groups
  - [ ] Support Other OAuth Providers
  