CREATE TABLE IF NOT EXISTS QUOTES_METADATA (
    provider VARCHAR(120) NOT NULL,
    quotetype VARCHAR(120) NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    timeZone VARCHAR(50),
    lastRefreshed VARCHAR(50),
    insert_ts timestamp_ntz default current_timestamp()::timestamp_ntz NOT NULL
);
