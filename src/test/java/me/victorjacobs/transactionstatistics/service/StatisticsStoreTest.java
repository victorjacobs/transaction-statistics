package me.victorjacobs.transactionstatistics.service;

import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Victor on 10/02/2017.
 */
public class StatisticsStoreTest {
    private StatisticsStore statisticsStore;

    @Before
    public void setup() {
        statisticsStore = new StatisticsStore();
    }

    @Test
    public void testAddTransactionToEmptyStore() {
        statisticsStore.add(createTransaction(10.0, 10));

        assertStatisticStore(10.0, 10.0, 10.0, 10.0, 1);
    }

    private void assertStatisticStore(double sum, double avg, double max, double min, long count) {
        assertStatistic(sum, avg, max, min, count, statisticsStore.getStatistic());
    }

    private void assertStatistic(double sum, double avg, double max, double min, long count, Statistic stat) {
        assertEquals(count, stat.getCount());
        assertEquals(avg, stat.getAvg(), 0.001);
        assertEquals(sum, stat.getSum(), 0.001);
        assertEquals(max, stat.getMax(), 0.001);
        assertEquals(min, stat.getMin(), 0.001);
    }

    // TODO maybe should move this somewhere dedicated, but where?
    private Transaction createTransaction(double amount, int secondsAgo) {
        return new Transaction(amount, System.currentTimeMillis() - (secondsAgo * 1000));
    }
}
