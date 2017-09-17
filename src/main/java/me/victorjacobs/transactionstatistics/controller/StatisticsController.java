package me.victorjacobs.transactionstatistics.controller;

import lombok.RequiredArgsConstructor;
import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.service.StatisticsStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the /statistics endpoint.
 * Created by Victor on 10/02/2017.
 */
@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsStore statisticsStore;

    @GetMapping(path = "/statistics")
    public Statistic getStatistics() {
        return statisticsStore.getStatistic();
    }
}
