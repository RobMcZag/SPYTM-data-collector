# CSV
###V7
* SPY.csv  
https://query1.finance.yahoo.com/v7/finance/download/SPY?period1=1492449771&period2=1495041771&interval=1d&events=history

# Quote & Info
Very good descriptionof URLs and params at StackOverflow answer 
[Yahoo Finance URL not working](https://stackoverflow.com/questions/44030983/yahoo-finance-url-not-working)

### Price (v8)
`/v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=3mo`  

* Intervals:

  * `&interval=3mo` 3 months, going back until initial trading date.
  * `&interval=1d` 1 day, going back until initial trading date.
  * `&interval=5m` 5 minuets, going back 80(ish) days.
  * `&interval=1m` 1 minuet, going back 4-5 days.

* Add pre & post market data
  * `&includePrePost=true`

* Add dividends & splits
  * `&events=div%2Csplit`

* Example URLs:  
`https://query1.finance.yahoo.com/v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=1d&includePrePost=true&events=div%2Csplit`  
The above request will return all price data for ticker AAPL on a 1 day interval including pre and post market data as well as dividends and splits.

#### Example files
* **chart_v8_APPL.json**  
`https://query1.finance.yahoo.com/v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=1d&includePrePost=true`
* **chart_v8_APPL_w_DIV_SPLIT.json**  
`https://query1.finance.yahoo.com/v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=1d&includePrePost=true&events=div%2Csplit`

### Current quote and general stock info
#### Example files
* quote_v7_MSFT.json  
`https://query1.finance.yahoo.com/v7/finance/quote?symbols=MSFT`


### Fundamental Data
`/v10/finance/quoteSummary/AAPL?modules=` 
(Full list of modules in SO answer)
* Example URL:
`https://query1.finance.yahoo.com/v10/finance/quoteSummary/AAPL?modules=assetProfile%2CearningsHistory`  
Querying for: assetProfile and earningsHistory

###Options contracts
`/v7/finance/options/AAPL`  
(current expiration)

`/v7/finance/options/AAPL?date=1579219200`  
(January 17, 2020 expiration)

* Example URLs:  
  * `https://query2.yahoo.finance.com/v7/finance/options/AAPL`  
(current expiration)
  * `https://query2.yahoo.finance.com/v7/finance/options/AAPL?date=1579219200`  
(January 17, 2020 expiration)