create table if not exists spytm.daily_quote (
    date date not null,
    serie varchar(255) not null,
    symbol varchar(30) not null,
    adjusted_close float8 not null,
    dividend_amount float8 not null,
    close float8 not null,
    high float8 not null,
    low float8 not null,
    open float8 not null,
    volume int8 not null,
    split_coefficient float8 not null,

    primary key (date, serie, symbol)
);
