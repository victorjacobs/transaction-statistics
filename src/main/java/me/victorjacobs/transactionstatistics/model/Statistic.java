package me.victorjacobs.transactionstatistics.model;

/**
 * Created by Victor on 10/02/2017.
 */
public class Statistic {
    private final double sum;
    private final double avg;
    private final double max;
    private final double min;
    private final long count;

    public Statistic(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public Statistic() {
        this(0, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }

    public Statistic add(Transaction transaction) {
        long newCount = count + 1;
        double newAvg = avg + ((transaction.getAmount() - avg) / newCount);
        double newMax = Math.max(max, transaction.getAmount());
        double newMin = Math.min(min, transaction.getAmount());
        double newSum = sum + transaction.getAmount();

        return new Statistic(newSum, newAvg, newMax, newMin, newCount);
    }

    public static Statistic combine(Statistic[] statistics) {
        long totalCount = 0;
        int numberOfStatistics = 0;
        double totalMax = Double.NEGATIVE_INFINITY;
        double totalMin = Double.POSITIVE_INFINITY;
        double totalSum = 0;
        double totalAvg = 0;

        for (Statistic statistic : statistics) {
            if (statistic == null) {
                continue;
            }

            numberOfStatistics++;
            totalAvg = totalAvg + ((statistic.getAvg() - totalAvg) / numberOfStatistics);
            totalCount += statistic.getCount();
            totalMax = Math.max(totalMax, statistic.getMax());
            totalMin = Math.min(totalMin, statistic.getMin());
            totalSum += statistic.getSum();
        }

        return new Statistic(totalSum, totalAvg, totalMax, totalMin, totalCount);
    }

    @Override
    public String toString() {
        return "Statistic [sum: " + sum +
                ", avg: " + avg +
                ", max: " + max +
                ", min: " + min +
                ", count: " + count + "]";
    }
}
