create table if not exists ap_data
(
    unique_id     varchar(36)   not null
        primary key,
    enabled_perks varchar(2048) not null,
    bought_perks  varchar(2048) not null,
    data_hash     varbinary(16) not null
);