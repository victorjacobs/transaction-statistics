package me.victorjacobs.transactionstatistics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Class representing a transaction.
 * Created by Victor on 10/02/2017.
 */
@Data
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
     * Floor the timestamp to the nearest second.
     * @return Timestamp floored to the nearest second, in milliseconds
     */
    public long getTimestampFlooredToSecond() {
        return (timestamp / 1000) * 1000;
    }

    @Override
    public String toString() {
        return "Transaction [" + timestamp + " " + amount + "]";
    }
}
