-- See method getTableName(QuoteType quoteType) in SnowflakeStorageService
CREATE TABLE IF NOT EXISTS WEEKLY_QUOTES (
                provider VARCHAR(120) NOT NULL,
                quotetype VARCHAR(120) NOT NULL,
                symbol VARCHAR(20) NOT NULL,
                date DATE NOT NULL,
                open NUMBER(38, 5),
                high NUMBER(38, 5),
                low NUMBER(38, 5),
                close NUMBER(38, 5),
                volume NUMBER(38, 0),
                adjustedClose NUMBER(38, 5),
                dividendAmount NUMBER(38, 5),
                splitCoefficient NUMBER(38, 5),
                insert_ts timestamp_ntz default current_timestamp()::timestamp_ntz NOT NULL
                );
