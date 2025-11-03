package com.github.andriytyranovets.unitalk_project;

public record BetsStatisticsDTO(String car, long totalAmount, int betsPlaced) {

     static BetsStatisticsDTO includeBetIntoStat(BetsStatisticsDTO statisticsDTO, BetDTO betDTO) {
        return new BetsStatisticsDTO(
                statisticsDTO.car(),
                statisticsDTO.totalAmount() + betDTO.amount(),
                statisticsDTO.betsPlaced() + 1
        );
    }

    static BetsStatisticsDTO aggregateStats(BetsStatisticsDTO lhs, BetsStatisticsDTO rhs) {
         return new BetsStatisticsDTO(
                 lhs.car(),
                 lhs.totalAmount() + rhs.totalAmount(),
                 lhs.betsPlaced() + rhs.betsPlaced()
         );
    }
}
