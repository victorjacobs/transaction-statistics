package me.victorjacobs.transactionstatistics;

import me.victorjacobs.transactionstatistics.model.Statistic;

import static org.junit.Assert.assertEquals;

/**
 * Base test containing helper methods for tests.
 * Created by Victor on 11/02/2017.
 */
public abstract class BaseTest {
    /**
     * Assert whether or not a given statistic equals the given parameters.
     * @param sum   Sum
     * @param avg   Average
     * @param max   Maximum
     * @param min   Minimum
     * @param count Count
     * @param stat  Statistic to assert
     */
    protected static void assertStatistic(double sum, double avg, double max, double min, long count, Statistic stat) {
        assertEquals(sum, stat.getSum(), 0.001);
        assertEquals(avg, stat.getAvg(), 0.001);
        assertEquals(max, stat.getMax(), 0.001);
        assertEquals(min, stat.getMin(), 0.001);
        assertEquals(count, stat.getCount());
    }
}
