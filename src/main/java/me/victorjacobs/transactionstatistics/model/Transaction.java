package me.victorjacobs.transactionstatistics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing a transaction
 * Created by Victor on 10/02/2017.
 */
public class Transaction {
    private final double amount;
    private final long timestamp;

    @JsonCreator
    public Transaction(
            @JsonProperty("amount") double amount,
            @JsonProperty("timestamp") long timestamp
    ) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    /**
     * @return Amount of the transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @return Timestamp of transaction in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Floor the timestamp to the nearest second.
     * @return Timestamp floored to the nearest second, in milliseconds
     */
    public double getTimestampFlooredToSecond() {
        return Math.floor(timestamp / 1000) * 1000;
    }

    @Override
    public String toString() {
        return "Transaction [" + timestamp + " " + amount + "]";
    }
}
