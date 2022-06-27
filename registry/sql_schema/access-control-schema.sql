create table userroles
(
    record_id int IDENTITY(1,1), 
    project_name varchar(50) not null,
    user_name varchar(50) not null,
    role_name varchar(50) not null,
    create_by varchar(50) not null,
    create_reason varchar(50) not null,
    create_time datetime not null,
    delete_by varchar(50),
    delete_reason varchar(50),
    delete_time datetime,
)
 
create table access
(
    record_id uniqueidentifier NOT NULL DEFAULT newid(), 
    project_name varchar(50) not null,
    access_name varchar(50) not null,
)
 
create table roleaccess
(
    record_id uniqueidentifier NOT NULL DEFAULT newid(), 
    role_name varchar(50) not null,
    access_name varchar(50) not null,
)