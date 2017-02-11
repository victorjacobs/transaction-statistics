package me.victorjacobs.transactionstatistics.controller;

import me.victorjacobs.transactionstatistics.model.Transaction;
import me.victorjacobs.transactionstatistics.service.StatisticsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller for the /transactions endpoint.
 * Created by Victor on 10/02/2017.
 */
@RestController
public class TransactionController {
    private final StatisticsStore statisticsStore;

    @Autowired
    public TransactionController(StatisticsStore statisticsStore) {
        this.statisticsStore = statisticsStore;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transactions", method = POST)
    public void postTransaction(@RequestBody Transaction transaction) {
        statisticsStore.add(transaction);
    }
}
