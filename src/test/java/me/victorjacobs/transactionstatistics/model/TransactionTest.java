package me.victorjacobs.transactionstatistics.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Victor on 10/02/2017.
 */
public class TransactionTest {
    @Test
    public void getTimestampFloored() {
        Transaction transaction = new Transaction(10.0, 1234);
        assertEquals(1000, transaction.getTimestampFlooredToSecond(), 0.001);
    }
}
