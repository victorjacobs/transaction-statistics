package me.victorjacobs.transactionstatistics.model;

import me.victorjacobs.transactionstatistics.BaseTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the Statistic model
 * Created by Victor on 10/02/2017.
 */
public class StatisticTest extends BaseTest {
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
    public void add() {
        Statistic combined = s1.add(new Transaction(4.0, 0));

        assertStatistic(14.0, 7.0, 10.0, 4.0, 2, combined);
    }

    @Test
    public void combine() {
        Statistic combined = Statistic.combine(new Statistic[]{s1, s2, s3});

        assertStatistic(28.0, 4.666, 10.0, 4.0, 6, combined);
    }
}
