package me.victorjacobs.transactionstatistics.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the Statistic model
 * Created by Victor on 10/02/2017.
 */
public class StatisticTest {
    private Statistic s1;
    private Statistic s2;
    private Statistic s3;

    @Before
    public void setup() {
        s1 = new Statistic(10.0, 10.0, 10.0, 10.0, 1);
        s2 = new Statistic(4.0, 4.0, 4.0, 4.0, 1);
        s3 = new Statistic(14.0, 3.5, 6.0, 1.0, 4);     // Corresponds with [1, 5, 2, 6]
    }

    @Test
    public void combine() {
        Statistic combined = Statistic.combine(new Statistic[]{s1, s2, s3});

        assertEquals(28.0, combined.getSum(), 0.001);
        assertEquals(4.666, combined.getAvg(), 0.001);
        assertEquals(10.0, combined.getMax(), 0.001);
        assertEquals(4.0, combined.getMin(), 0.001);
        assertEquals(6, combined.getCount());
    }
}
