package me.victorjacobs.transactionstatistics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
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

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction [" + timestamp + " " + amount + "]";
    }
}
