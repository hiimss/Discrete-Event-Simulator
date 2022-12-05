package cs2030.simulator;

import cs2030.util.Pair;

import java.util.Optional;

class LeaveEvent extends EventStub {

    private static final int PRIORITY = 3;
    private static final String LEAVE = "LEAVE";

    LeaveEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s leaves", this.getEventTime(), 
                this.getCustomer());
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public String getEventName() {
        return LEAVE;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {

        Statistic currStatistic = shop.getStatistic();
        Statistic newStatistic = new Statistic(currStatistic.getTotalWaiting(),
                currStatistic.getTotalServing(), currStatistic.getTotalLeave() + 1, 
                currStatistic.getTotalNormalServe());
        Shop newShop = new Shop(shop.getServers(), newStatistic, 
                shop.getRestTime(), shop.getSelfServerQueue());
                
        return Pair.of(Optional.empty(), newShop);
    }
} 

