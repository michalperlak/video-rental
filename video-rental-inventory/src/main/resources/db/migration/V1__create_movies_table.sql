create table MOVIES (
    ID int not null identity(1,1) primary key,
    UUID uuid not null,
    TITLE varchar(250) not null,
    RELEASE_DATE date not null
);
