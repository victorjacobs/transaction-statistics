package me.victorjacobs.transactionstatistics.service;

import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Victor on 10/02/2017.
 */
public class StatisticsStoreTest {
    private StatisticsStore statisticsStore;
    private Clock clock;

    @Before
    public void setup() {
        clock = mock(Clock.class);
        statisticsStore = new StatisticsStore(clock);
    }

    @Test
    public void addTransaction_emptyStore() {
        when(clock.millis()).thenReturn(Instant.now().toEpochMilli());
        statisticsStore.add(createTransaction(10.0, 10));
        statisticsStore.add(createTransaction(4.0, 15));

        assertStatisticStore(14.0, 7.0, 10.0, 4.0, 2);
    }

    @Test
    public void addTransaction_toSameBucket() {
        when(clock.millis()).thenReturn(Instant.now().toEpochMilli());
        statisticsStore.add(createTransaction(10.0, 10));
        statisticsStore.add(createTransaction(4.0, 10));

        assertStatisticStore(14.0, 7.0, 10.0, 4.0, 2);
    }

//    public void addTransaction_

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
        return new Transaction(amount, clock.millis() - secondsAgo * 1000);
    }
}
