package cs2030.simulator;

import cs2030.util.Pair;

import java.util.Optional;

class RestEvent extends EventStub {

    private final Server server;
    
    private static final String REST = "REST";
    private static final int ZERO = 0;

    RestEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
    }

    @Override
    public int getPriority() {
        return ZERO;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String getEventName() {
        return REST;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        
        Server server = shop.getServers().get(getServer().getId() - 1);
        Server newServer = new Server(server.getId(), server.isBusy(), 
                server.getMaxQueue(), server.getWaitingCustomer(), 
                this.getEventTime(), server.isSelfServed(), false);
        Shop newShop = new Shop(shop.getServers().set(newServer.getId() - 1,
                    newServer), shop.getStatistic(), shop.getRestTime(), 
                shop.getSelfServerQueue());

        return Pair.of(Optional.empty(), newShop);
    }
}
