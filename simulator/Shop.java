package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.PQ;

import java.util.List;

class Shop {

    private final ImList<Server> servers;
    private final Statistic statistic;
    private final PQ<Customer> selfServerQueue;
    private final ImList<Double> restTime;

    private static final double DECIMAL_ZERO = 0.0;
    private static final int ZERO = 0;

    Shop(List<Server> servers) {
        this.servers = ImList.of(servers);
        this.statistic = new Statistic(DECIMAL_ZERO, ZERO, ZERO);
        this.selfServerQueue = new PQ<Customer>(new CustomerComparator());
        this.restTime = ImList.of();
    }

    Shop(ImList<Server> servers) {
        this.servers = servers;
        this.statistic = new Statistic(DECIMAL_ZERO, ZERO, ZERO);
        this.selfServerQueue = new PQ<Customer>(new CustomerComparator());
        this.restTime = ImList.of();
    }

    Shop(ImList<Server> servers, ImList<Double> restTime) {
        this.servers = servers;
        this.statistic = new Statistic(DECIMAL_ZERO, ZERO, ZERO);
        this.selfServerQueue = new PQ<Customer>(new CustomerComparator());
        this.restTime = restTime;
    }

    Shop(ImList<Server> servers, Statistic statistic, 
            ImList<Double> restTime, PQ<Customer> selfServerQueue) {
        this.servers = servers;
        this.statistic = statistic;
        this.selfServerQueue = selfServerQueue;
        this.restTime = restTime;
    }

    public ImList<Server> getServers() {
        return servers;
    }

    @Override
    public String toString() {
        return servers.toString();
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public PQ<Customer> getSelfServerQueue() {
        return selfServerQueue;
    }

    public ImList<Double> getRestTime() {
        return restTime;
    }
}
