package me.victorjacobs.transactionstatistics.service;

import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.model.Transaction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Clock;

/**
 * Created by Victor on 10/02/2017.
 */
@Service
@Scope("singleton")
public class StatisticsStore {
    private Statistic[] buckets = new Statistic[60];
    private double[] bucketTimestamps = new double[60];
    private final Clock clock;

    public StatisticsStore() {
        this.clock = Clock.systemDefaultZone();
    }

    StatisticsStore(Clock clock) {
        this.clock = clock;
    }

    public synchronized void add(Transaction transaction) {
        if (shouldDiscardTransaction(transaction)) {
            return;
        }

        if (shouldResetBucketFor(transaction)) {
            setBucket(transaction, new Statistic());
        }

        updateBucketTimestamp(transaction);
        setBucket(transaction, getBucket(transaction).add(transaction));
    }

    public Statistic getStatistic() {
        return Statistic.combine(buckets);
    }

    private boolean shouldDiscardTransaction(Transaction transaction) {
        return clock.millis() - transaction.getTimestamp() > 60000 ||
                clock.millis() < transaction.getTimestamp();
    }

    private boolean shouldResetBucketFor(Transaction transaction) {
        return getBucket(transaction) == null
                || getBucketTimestamp(transaction) != transaction.getTimestampFlooredToSecond();
    }

    private Statistic getBucket(Transaction transaction) {
        return buckets[getBucketIndex(transaction)];
    }

    private void setBucket(Transaction transaction, Statistic statistic) {
        buckets[getBucketIndex(transaction)] = statistic;
    }

    private double getBucketTimestamp(Transaction transaction) {
        return bucketTimestamps[getBucketIndex(transaction)];
    }

    private void updateBucketTimestamp(Transaction transaction) {
        bucketTimestamps[getBucketIndex(transaction)] = transaction.getTimestampFlooredToSecond();
    }

    private int getBucketIndex(Transaction transaction) {
        return (int) Math.floor(transaction.getTimestamp() / 1000) % 60;
    }
}
