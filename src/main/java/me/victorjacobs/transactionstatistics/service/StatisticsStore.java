package me.victorjacobs.transactionstatistics.service;

import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.model.Transaction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Clock;

/**
 * Represents the store of the time-series transaction statistics. Storage happens by dividing incoming transactions
 * over buckets. There are 60 buckets, one for every second in the requested time-series window. Bucket N contains the
 * statistics for all transactions for which the timestamp (in seconds) % 60 == N. When a values comes in, it is combined
 * with the statistics object in the bucket and the actual value discarded. This ensures constant memory complexity.
 *
 * When the statistics for the entire window are requested, all statistics objects in all buckets are combined with
 * each other. Since there are only 60 buckets, this ensures constant memory complexity for reading the statistics.
 *
 * Clock can be injected, which allows for consistent unit tests.
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

    /**
     * Add a transaction to the store.
     * @param transaction   Transaction to be added
     */
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

    /**
     * Get statistics over all transactions that were stored in the last 60 seconds.
     * @return Statistics over all transaction received in the last 60 seconds
     */
    public Statistic getStatistic() {
        return Statistic.combine(buckets);
    }

    /**
     * Whether or not a transaction should be discarded and not added to storage. This is when it either lies outside of
     * the window or when the timestamp is in the future.
     * @param transaction   Transaction to consider
     * @return Whether or not a transaction should be discarded
     */
    private boolean shouldDiscardTransaction(Transaction transaction) {
        return clock.millis() - transaction.getTimestamp() > 60000 ||
                clock.millis() < transaction.getTimestamp();
    }

    /**
     * Whether or not the bucket in which a given transaction falls, should be reset to an empty value. This is the case
     * when either there is not yet a bucket set for the transaction, or when the timestamp identifying the bucket is
     * different from the one computed from the transaction.
     * @param transaction   Transaction to consider
     * @return Whether or not to reset the bucket for this transaction
     */
    private boolean shouldResetBucketFor(Transaction transaction) {
        return getBucket(transaction) == null
                || getBucketTimestamp(transaction) != transaction.getTimestampFlooredToSecond();
    }

    /**
     * Get the bucket of which the transaction is part.
     * @param transaction   Transaction for which to return the bucket
     * @return Bucket related to given transaction
     */
    private Statistic getBucket(Transaction transaction) {
        return buckets[getBucketIndex(transaction)];
    }

    /**
     * Set bucket related to given transaction to given value
     * @param transaction   Transaction for which set the bucket value
     * @param statistic     Value to set the bucket to
     */
    private void setBucket(Transaction transaction, Statistic statistic) {
        buckets[getBucketIndex(transaction)] = statistic;
    }

    /**
     * Get the timestamp identifying the bucket of which given transaction is part.
     * @param transaction   Transaction for which to find timestamp
     * @return Timestamp identifying the bucket
     */
    private double getBucketTimestamp(Transaction transaction) {
        return bucketTimestamps[getBucketIndex(transaction)];
    }

    /**
     * Update the timestamp identifying the bucket of which the given transaction is part to the value derived from the
     * same transaction.
     * @param transaction   Transaction for which to update the bucket timestamp
     */
    private void updateBucketTimestamp(Transaction transaction) {
        bucketTimestamps[getBucketIndex(transaction)] = transaction.getTimestampFlooredToSecond();
    }

    /**
     * Derive array index of bucket for given transaction, used accessing backing arrays of the service.
     * @param transaction   Transaction for which to find the bucket index
     * @return Index of bucket in backing arrays
     */
    private int getBucketIndex(Transaction transaction) {
        return (int) Math.floor(transaction.getTimestamp() / 1000) % 60;
    }
}
