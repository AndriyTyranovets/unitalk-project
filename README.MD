# UniTalk project
## Available endpoints
* `POST /bet` - place a bet on a car. Parameters `car` for car name and `amount` for bet amount in USD. Bet must be more than \$0 and less than \$500,000 (exclusive)
* `GET /bet/{car}` - List of bet statistics for specified car. If car is not specified lists statistics about bets on all cars
* `GET /cars` - List of cars to place bet on 
