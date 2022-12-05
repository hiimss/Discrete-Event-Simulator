package cs2030.simulator;

import cs2030.util.Pair;

import java.util.Optional;

class DoneEvent extends EventStub {

    private final Server server;
    
    private static final String DONE = "DONE";
    private static final double DECIMAL_ZERO = 0.0;

    DoneEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
    }

    @Override
    public String toString() {
        if (server.isSelfServed()) {
            return String.format("%.3f %s done serving by self-check %s",
                    this.getEventTime(), this.getCustomer(), server.getId());
        }
        return String.format("%.3f %s done serving by %s", this.getEventTime(),
                this.getCustomer(), server.getId());
    }

    @Override
    public String getEventName() {
        return DONE;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {

        Double restingTime = shop.getRestTime().get(shop.getStatistic()
                .getTotalNormalServe());
        Server updatedServer = shop.getServers().get(server.getId() - 1);
        Integer normalServe = shop.getStatistic().getTotalNormalServe();

        if (updatedServer.isSelfServed()) {
            restingTime = DECIMAL_ZERO;
        } else {
            normalServe += 1;
        }

        Server newServer = new Server(updatedServer.getId(), false, 
                getServer().getMaxQueue(), updatedServer.getWaitingCustomer(), 
                this.getEventTime() + restingTime, server.isSelfServed(), 
                !restingTime.equals(DECIMAL_ZERO));
        Statistic currStatistic = shop.getStatistic();
        Statistic newStatistic = new Statistic(currStatistic.getTotalWaiting(),
                currStatistic.getTotalServing() + 1, currStatistic.getTotalLeave(), 
                normalServe);
        Shop newShop = new Shop(shop.getServers().set(newServer.getId() - 1,
                    newServer), newStatistic, shop.getRestTime(), 
                shop.getSelfServerQueue());

        if (!restingTime.equals(DECIMAL_ZERO)) {
            return Pair.of(Optional.of(new RestEvent(this.getCustomer(), 
                            this.getEventTime() + restingTime, server)), 
                    newShop);
        }
        return Pair.of(Optional.empty(), newShop);
    }

}
