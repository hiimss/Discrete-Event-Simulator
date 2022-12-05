package cs2030.simulator;

import cs2030.util.Pair;

import java.util.Optional;

class EventStub implements Event {

    private final Customer customer;
    private final double eventTime;
    
    private static final String STUB = "STUB";
    private static final int ZERO = 0;

    EventStub(Customer customer, double eventTime) {
        this.customer = customer;
        this.eventTime = eventTime;
    }

    public String toString() {
        return String.format("%.3f", this.eventTime);
    }

    @Override
    public double getEventTime() {
        return this.eventTime;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        return Pair.of(Optional.empty(), shop);
    }

    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public String getEventName() {
        return STUB;
    }

    @Override
    public Server getServer() {
        return new Server(ZERO);
    }
}
