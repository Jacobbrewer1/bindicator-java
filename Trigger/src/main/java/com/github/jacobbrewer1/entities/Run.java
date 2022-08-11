package com.github.jacobbrewer1.entities;

import org.joda.time.DateTime;

public class Run {

    private int id;
    private DateTime dateRan;
    private DateTime nextRun;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getDateRan() {
        return dateRan;
    }

    public void setDateRan(DateTime dateRan) {
        this.dateRan = dateRan;
    }

    public void calculateNextRun() {
        nextRun = dateRan.plusDays(1);
    }

    public DateTime getNextRun() {
        return nextRun;
    }

    public void setNextRun(DateTime nextRun) {
        this.nextRun = nextRun;
    }
}
