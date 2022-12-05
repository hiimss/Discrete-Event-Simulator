package cs2030.simulator;

import cs2030.util.PQ;

class Server {

    private final Integer id;
    private final PQ<Customer> waitingCustomer;
    private final Integer maxQueue;
    private final Boolean busy;
    private final Double freeTime;
    private final Boolean isResting;
    private final boolean isSelfServed;
    
    private static final double DECIMAL_ZERO = 0.0;

    Server(Integer id) {
        this.id = id;
        this.busy = false;
        this.maxQueue = 1;
        this.waitingCustomer = new PQ<Customer>(new CustomerComparator());
        this.freeTime = DECIMAL_ZERO;
        this.isSelfServed = false;
        this.isResting = false;
    }

    Server(Integer id, Integer maxQueue) {
        this.id = id;
        this.waitingCustomer = new PQ<Customer>(new CustomerComparator());
        this.busy = false;
        this.maxQueue = maxQueue;
        this.freeTime = DECIMAL_ZERO;
        this.isSelfServed = false;
        this.isResting = false;
    }

    Server(Integer id, Integer maxQueue, Boolean isSelfServed) {
        this.id = id;
        this.waitingCustomer = new PQ<Customer>(new CustomerComparator());
        this.busy = false;
        this.maxQueue = maxQueue;
        this.freeTime = DECIMAL_ZERO;
        this.isSelfServed = isSelfServed;
        this.isResting = false;
    }

    Server(Integer id, Boolean busy, Integer maxQueue, 
            PQ<Customer> waitingCustomer, Double freeTime, 
            Boolean isSelfServed, Boolean isResting) {
        this.id = id;
        this.busy = busy;
        this.maxQueue = maxQueue;
        this.waitingCustomer = waitingCustomer;
        this.freeTime = freeTime;
        this.isSelfServed = isSelfServed;
        this.isResting = isResting;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public Integer getId() {
        return id;
    }

    public PQ<Customer> getWaitingCustomer() {
        return waitingCustomer;
    }

    public Boolean isBusy() {
        return busy;
    }

    public Integer getMaxQueue() {
        return maxQueue;
    }

    public Double getFreeTime() {
        return this.freeTime;
    }

    public boolean isSelfServed() {
        return isSelfServed;
    }

    public Boolean getIsResting() {
        return isResting;
    }
}
