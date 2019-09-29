package dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Account {
    @QuerySqlField(index = true)
    private long id;

    @QuerySqlField
    private double balance;

    public Account(long id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account { id = " + id + ", balance = " + balance + "}";
    }
}
