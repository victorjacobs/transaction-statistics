package me.victorjacobs.transactionstatistics.controller;

import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.service.StatisticsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the /statistics endpoint.
 * Created by Victor on 10/02/2017.
 */
@RestController
public class StatisticsController {
    private final StatisticsStore statisticsStore;

    @Autowired
    public StatisticsController(StatisticsStore statisticsStore) {
        this.statisticsStore = statisticsStore;
    }

    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    public Statistic getStatistics() {
        return statisticsStore.getStatistic();
    }
}
