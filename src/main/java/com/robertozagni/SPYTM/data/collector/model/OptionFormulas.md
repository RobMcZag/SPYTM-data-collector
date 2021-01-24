# Option Formulas (Black & Scholes)
Introduction to the Black-Scholes formula | Finance & Capital Markets | Khan Academy  
https://www.youtube.com/watch?v=pr-u4LCFYEY
## Black & Scholes Formula
* Black & Scholes formula for an European call option (exercise only at end)   
  `C0 = S0 * N(d1) - X * e^(r*T) * N(d2)`  
  * `d1 = ( ln(S0 / X) + T * (r + s^2 /2 ) ) / ( s * sqrt(T) )`  
  * `d2 = ( ln(S0 / X) + T * (r - s^2 /2 ) ) / ( s * sqrt(T) )`  
    
###Terms
S0 = stock price  
X = exercise price = strike price
r = risk free interest rate  
T = time to expiration  
σ=s= std dev of log returns (volatility)  
--  
N(X) = Cumulative distribution function for a standard normal distribution.  
I.e. the probability that a random variable is less than or equal to X.  
It is always `0 < N(X) < 1` as N(X) is a probability.  


## Rough Explanation
The Black & Scholes formula is made up of two main terms, one to account
what you will get, based on the underlying stock price, and the other
to account what you are going to pay, based on the strike.

    C0 = S0 * N(d1) - X * e^(r*T) * N(d2)  =
       =    get     -    pay

    get = S0 * N(d1) 
        = stock price * some probability

    pay = X * e^(r*T) * N(d2) 
        = strike * discount to expiration * some probability
    
  * discount to present time : `e^(r*T)`
  * present value of exercise price : `X * e^(r*T)`

d1 and d2 vary based on price versus strike ratio, as we have the
term `ln(S0 / X)` where `S0 / X` will be bigger when S0 grows;
the larger the ratio the larder `d1` and `d2`.  
A larger input in N(X) will also mean a value closer to 1.

Importantly `ln(S0 / X)` is going to be positive when `S0/X > 1 => S0 > X`,
so when price is above strike the two terms add up, raising `d1` and `d2`.

### Effect of volatility
We have `σ=s` in `d1` and `d2` and we have it squared in the numerator 
versus plain σ in the denominator. So the effect of `σ` is   
* high `d1` when `σ` is high
* low `d2` when `σ` is high

The final effect on the option value C0 is therefore:
*  when `σ` goes up `C0` goes up, as `d1` goes up and `d2` goes down;
*  when `σ` goes down `C0` goes down, as `d1` goes down and `d2` goes up;

## Measuring volatility
In the B.S. formula we have defined volatility as `σ=s= std dev of log returns`,
so how do we measure `σ` ?  
For the formula the volatility of a security is something that is a 
constant, for application of the formula it is a fixed propety of the 
security, even if we know that it can change over time.  

The way to ESTIMATE `σ` is to look at the HISTORY or prices for 
the security and calculate the standard deviation of log returns over 
a period where the security did not have dramatic price changes.  
The `σ` is therefore always an estimate, there is no way to know the 
actual intrinsic value at some time, let alone to figure out a lifetime 
value for the security!

### Implied volatility
As `σ` is never actually known, but estimated we can use the fact that we 
actually kow the option price, as well as all other terms, to 
calculate what is the `implied volatility`, i.e. the volatility that 
the market is pricing in the option price.
Usually the implied volatility is calculated for options with strikes 
at the money (ATM), i.e. where S0 ≈ X.

## Calculating DELTA
The delta of an option measures the amplitude of the change of its price 
in function of the change of the price of its underlying.  
https://www.iotafinance.com/en/Formula-Delta-of-a-Call-Option.html  

Delta is calcualted as 
* `∂ = N(d1)` with  
    * `d1 = ( ln(S0 / X) + T * (r + s^2 /2 ) ) / ( s * sqrt(T) )`  
