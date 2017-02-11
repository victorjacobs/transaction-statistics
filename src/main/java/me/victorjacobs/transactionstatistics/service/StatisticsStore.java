package me.victorjacobs.transactionstatistics.service;

import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.model.Transaction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the store of the time-series transaction statistics. Storage happens by dividing incoming transactions
 * over buckets. There are 60 buckets, one for every second in the requested time-series window. Bucket N contains the
 * statistics for all transactions for which the timestamp (in seconds) % 60 == N. When a values comes in, it is combined
 * with the statistics object in the bucket and the actual value discarded. This ensures constant memory complexity.
 *
 * To link the buckets with the actual time window they are representing, a second list of "bucketTimestamps" is kept.
 * Here every entry is the lower bound timestamp of the window the bucket represents. This is to make sure that when the
 * buckets wrap around, that it can be detected and properly reset.
 *
 * When the statistics for the entire window are requested, all statistics objects in the buckets are combined with
 * each other. Since there are only 60 buckets, this ensures constant time complexity for reading the statistics.
 * However, here we need to make sure that the data in all the buckets is really from the last 60 seconds. In the case
 * where a bucket isn't written to for a long time, it will contain statistics that have nothing to do with the window,
 * but will be included in the total statistics if not properly managed. This is done by only using the buckets used
 * for data in the last 60 seconds to calculate the overall statistics. This is favorable over pruning the buckets on
 * read because would introduce unneeded complexity in synchronising writes to the backing data store.
 *
 * Clock can be injected, easy unit testing.
 * Created by Victor on 10/02/2017.
 */
@Service
@Scope("singleton")
public class StatisticsStore {
    private static final int WINDOW_SIZE_MILLIS = 60000;

    private static final int WINDOW_SIZE_SECONDS = (int) Math.floor(WINDOW_SIZE_MILLIS / 1000);

    private Statistic[] buckets = new Statistic[WINDOW_SIZE_SECONDS];
    private long[] bucketTimestamps = new long[WINDOW_SIZE_SECONDS];
    private final Clock clock;

    public StatisticsStore() {
        this.clock = Clock.systemUTC();
    }

    StatisticsStore(Clock clock) {
        this.clock = clock;
    }

    /**
     * Add a transaction to the store. This entire method is synchronised to ensure no dirty reads can happen.
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
        return Statistic.combine(getRelevantBuckets());
    }

    /**
     * Filters the buckets to the ones that are relevant in statistics calculation. I.e. only buckets that are marked
     * as having a timestamp within the last 60 seconds.
     * @return Buckets for the last 60 seconds
     */
    private List<Statistic> getRelevantBuckets() {
        // Ensure that the window start is consistent over the filtering of the buckets by fixing it in a variable
        long windowStart = clock.millis() - WINDOW_SIZE_MILLIS;
        LinkedList<Statistic> relevantBuckets = new LinkedList<>();

        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] == null) {
                continue;
            }

            if (bucketTimestamps[i] >= windowStart) {
                relevantBuckets.push(buckets[i]);
            }
        }

        return relevantBuckets;
    }

    /**
     * Whether or not a transaction should be discarded and not added to storage. This is when it either lies outside of
     * the window or when the timestamp is in the future.
     * @param transaction   Transaction to consider
     * @return Whether or not a transaction should be discarded
     */
    private boolean shouldDiscardTransaction(Transaction transaction) {
        return clock.millis() - transaction.getTimestamp() > WINDOW_SIZE_MILLIS ||
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
    private long getBucketTimestamp(Transaction transaction) {
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
        return (int) Math.floor(transaction.getTimestamp() / 1000) % WINDOW_SIZE_SECONDS;
    }
}
