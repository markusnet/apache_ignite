package dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

/**
 * Created by shamim on 19/10/16.
 */
public class Person implements Serializable {

    @QuerySqlField(index = true)
    private long id;

    @QuerySqlField(index = true)
    private long orgId;

    @QuerySqlField
    private String name;

    @QuerySqlField(index = true)
    private double salary;

    public Person(long id, long orgId, String name, int salary) {
        this.id = id;
        this.orgId = orgId;
        this.salary = salary;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Person {id = " + id + ", name = " + name + ", orgId = " + orgId + ", salary = " + salary + "}";
    }
}
