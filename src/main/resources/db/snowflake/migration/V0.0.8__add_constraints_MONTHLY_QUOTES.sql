-- See method getTableName(QuoteType quoteType) in SnowflakeStorageService
ALTER TABLE MONTHLY_QUOTES
    ADD CONSTRAINT MONTHLY_QUOTES_METADATA_FK FOREIGN KEY (provider, quotetype, symbol)
    REFERENCES QUOTES_METADATA (provider, quotetype, symbol);

ALTER TABLE MONTHLY_QUOTES
    ADD CONSTRAINT MONTHLY_QUOTES_PK PRIMARY KEY (provider, quotetype, symbol, date);
