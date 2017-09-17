package me.victorjacobs.transactionstatistics.controller;

import lombok.RequiredArgsConstructor;
import me.victorjacobs.transactionstatistics.model.Transaction;
import me.victorjacobs.transactionstatistics.service.StatisticsStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the /transactions endpoint.
 * Created by Victor on 10/02/2017.
 */
@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final StatisticsStore statisticsStore;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/transactions")
    public void postTransaction(@RequestBody Transaction transaction) {
        statisticsStore.add(transaction);
    }
}
