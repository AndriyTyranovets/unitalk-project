package com.github.andriytyranovets.unitalk_project;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {
    private final Set<String> cars = new HashSet<>();
    private final Map<String, List<BetDTO>> carsToBets = new HashMap<>();

    /**
     * Place a bet on car.
     * @param car a car model to bet on
     * @param amount a bet amount in USD
     * @return Status message
     */
    @PostMapping("/bet")
    public String acceptBet(@RequestParam(name = "car") String car, @RequestParam(name = "amount") int amount) {
        if(!cars.contains(car)) {
            return "Error: Car not found";
        }
        if(amount <= 0) {
            return "Error: Amount must be above 0";
        }
        if(amount > 499999) {
            return "Error: bet is too large for online processing";
        }

        carsToBets
                .computeIfAbsent(car, c -> new LinkedList<BetDTO>())
                .add(new BetDTO(car, amount));

        return "Bet accepted";
    }

    /**
     * List statistics for bets placed on all cars
     * @return list of aggregated bets statistics
     */
    @GetMapping("/bet")
    public Collection<BetsStatisticsDTO> getAllBets() {
        return this.carsToBets.entrySet()
                .stream()
                .map(e -> collectBetStatistics(e.getKey(), e.getValue()))
                .toList();
    }

    /**
     * Collect statistics for bets placed on specific car
     * @param car a car to get statistics for
     * @return if car is not available or no bets were placed on the car - empty list; list with single statistics item otherwise
     */
    @GetMapping("/bet/{car}")
    public Collection<BetsStatisticsDTO> getBetsOnCar(@PathVariable String car) {
        var stats = collectBetStatistics(car, this.carsToBets.get(car));
        //noinspection unchecked
        return stats == null ? Collections.EMPTY_LIST : List.of(stats);
    }

    /**
     * List all cars available to place bets on
     * @return list of car names
     */
    @GetMapping("/cars")
    public Collection<String> getAllCars() {
        return this.cars;
    }

    private BetsStatisticsDTO collectBetStatistics(String car, List<BetDTO> bets) {
        if(bets == null) return null;

        return bets.stream()
                .reduce(
                        new BetsStatisticsDTO(car, 0, 0),
                        BetsStatisticsDTO::includeBetIntoStat,
                        BetsStatisticsDTO::aggregateStats
                );
    }

    @PostConstruct
    public void init() {
        this.cars.addAll(Set.of("Ferrari", "BMW", "Audi", "Honda"));
    }
}
