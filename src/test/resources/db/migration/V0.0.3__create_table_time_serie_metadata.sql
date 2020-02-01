create table if not exists spytm.time_serie_metadata (
    provider varchar(255) not null,
    quotetype varchar(255) not null,
    symbol varchar(30) not null,

    description varchar(255),
    time_zone varchar(30),
    last_refreshed varchar(30),

    primary key (provider, quotetype, symbol)
);
