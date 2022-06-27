create table entities
(
    entity_id varchar(50) not null primary key,
    qualified_name varchar(200) not null,
    entity_type varchar(100) not null,
    attributes NVARCHAR(MAX) not null,
)

create table edges
(
    edge_id   varchar(50) not null primary key,
    from_id   varchar(50) not null,
    to_id     varchar(50) not null,
    conn_type varchar(20) not null,
)

create table userroles
(
    record_id int IDENTITY(1,1), 
    project_name varchar(50) not null,
    user_name varchar(50) not null,
    role_name varchar(50) not null,
    create_reason varchar(50) not null,
    create_time datetime not null,
    delete_reason varchar(50),
    delete_time datetime,
)
 
create table access
(
    record_id int IDENTITY(1,1), 
    project_name varchar(50) not null,
    access_name varchar(50) not null,
)
 
create table roleaccess
(
    record_id int IDENTITY(1,1), 
    role_name varchar(50) not null,
    access_name varchar(50) not null,
)