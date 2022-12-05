package cs2030.simulator;

import cs2030.util.Lazy;

import java.util.function.Supplier;

class Customer {

    private final int id;
    private final double arrivalTime;
    private final Lazy<Double> serviceTime;

    Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = Lazy.of(new DefaultServiceSupplier());
    }

    Customer(int id, double arrivalTime, Supplier<Double> serviceTimeSupplier) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = Lazy.of(serviceTimeSupplier);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public Integer getId() {
        return this.id;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public Double getServiceTime() {
        return serviceTime.get();
    }
}
