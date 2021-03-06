
ALTER TABLE QUOTES_METADATA
    ADD CONSTRAINT QUOTES_METADATA_PK PRIMARY KEY (provider, quotetype, symbol);

ALTER TABLE DAILY_QUOTES
    ADD CONSTRAINT QUOTES_METADATA_FK FOREIGN KEY (provider, quotetype, symbol)
    REFERENCES QUOTES_METADATA (provider, quotetype, symbol);

ALTER TABLE DAILY_QUOTES
    ADD CONSTRAINT DAILY_QUOTES_PK PRIMARY KEY (provider, quotetype, symbol, date);
