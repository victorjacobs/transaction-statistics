package me.victorjacobs.transactionstatistics.service;

import me.victorjacobs.transactionstatistics.BaseTest;
import me.victorjacobs.transactionstatistics.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Victor on 10/02/2017.
 */
public class StatisticsStoreTest extends BaseTest {
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

    @Test
    public void addTransaction_wrapsAround() {
        long currentMillis = Instant.now().toEpochMilli();

        when(clock.millis()).thenReturn(currentMillis);
        statisticsStore.add(createTransaction(10.0, 10));

        when(clock.millis()).thenReturn(currentMillis + 60000);
        statisticsStore.add(createTransaction(4.0, 10));

        assertStatisticStore(4.0, 4.0, 4.0, 4.0, 1);
    }

    @Test
    public void addTransaction_wrapsAround_differentBucket() {
        long currentMillis = Instant.now().toEpochMilli();

        when(clock.millis()).thenReturn(currentMillis);
        statisticsStore.add(createTransaction(10.0, 10));

        when(clock.millis()).thenReturn(currentMillis + 60000);
        statisticsStore.add(createTransaction(4.0, 12));

        assertStatisticStore(4.0, 4.0, 4.0, 4.0, 1);
    }

    @Test
    public void addTransaction_outOfOrder() {
        when(clock.millis()).thenReturn(Instant.now().toEpochMilli());

        statisticsStore.add(createTransaction(10.0, 5));
        statisticsStore.add(createTransaction(4.0, 10));

        assertStatisticStore(14.0, 7.0, 10.0, 4.0, 2);
    }

    @Test
    public void addTransaction_transactionBeforeWindow() {
        when(clock.millis()).thenReturn(Instant.now().toEpochMilli());

        statisticsStore.add(createTransaction(10.0, 5));
        statisticsStore.add(createTransaction(4.0, 100));

        assertStatisticStore(10.0, 10.0, 10.0, 10.0, 1);
    }

    @Test
    public void addTransaction_transactionInFuture() {
        when(clock.millis()).thenReturn(Instant.now().toEpochMilli());

        statisticsStore.add(createTransaction(10.0, 5));
        statisticsStore.add(createTransaction(4.0, -100));

        assertStatisticStore(10.0, 10.0, 10.0, 10.0, 1);
    }

    @Test
    public void addTransaction_negativeAmount() {
        when(clock.millis()).thenReturn(Instant.now().toEpochMilli());

        statisticsStore.add(createTransaction(10.0, 5));
        statisticsStore.add(createTransaction(-10.0, 10));

        assertStatisticStore(0.0, 0.0, 10.0, -10.0, 2);
    }

    private void assertStatisticStore(double sum, double avg, double max, double min, long count) {
        assertStatistic(sum, avg, max, min, count, statisticsStore.getStatistic());
    }

    private Transaction createTransaction(double amount, int secondsAgo) {
        return new Transaction(amount, clock.millis() - secondsAgo * 1000);
    }
}
