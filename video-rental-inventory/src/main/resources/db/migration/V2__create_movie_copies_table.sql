create table MOVIE_COPIES (
    ID int not null identity(1,1) primary key,
    UUID uuid not null,
    MOVIE_ID varchar(250) not null,
    ADDED_EPOCH_MILLIS long not null
);