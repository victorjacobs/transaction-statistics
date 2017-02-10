package me.victorjacobs.transactionstatistics.service;

import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.model.Transaction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Victor on 10/02/2017.
 */
@Service
@Scope("singleton")
public class StatisticsStore {
    private Statistic[] store = new Statistic[60];

    public synchronized void add(Transaction transaction) {
        if (shouldDiscardTransaction(transaction)) {
            return;
        }

        Statistic oldStatistic = store[calculateBucket(transaction)];
        store[calculateBucket(transaction)] = oldStatistic.add(transaction);
    }

    public Statistic getStatistic() {
        return Statistic.combine(store);
    }

    private boolean shouldDiscardTransaction(Transaction transaction) {
        // TODO check for future
        // TODO re enable check here
        return System.currentTimeMillis() - transaction.getTimestamp() > 600000000;
    }

    private int calculateBucket(Transaction transaction) {
        return (int) Math.floor(transaction.getTimestamp() / 1000) % 60;
    }
}
