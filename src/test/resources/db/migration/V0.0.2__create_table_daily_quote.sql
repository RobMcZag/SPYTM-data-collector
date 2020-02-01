create table if not exists spytm.daily_quote (
    provider varchar(255) not null,
    quotetype varchar(255) not null,
    symbol varchar(30) not null,
    date date not null,

    open float8 not null,
    close float8 not null,
    low float8,
    high float8,
    volume int8,

    adjusted_close float8,
    dividend_amount float8,
    split_coefficient float8,

    primary key (provider, quotetype, symbol, date)
);
