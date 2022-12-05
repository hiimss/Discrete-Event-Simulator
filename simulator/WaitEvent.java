package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.PQ;

import java.util.Optional;

class WaitEvent extends EventStub {

    private final Server server;
    private final Integer identifier;

    private static final String WAIT = "WAIT";

    WaitEvent(Customer customer, double eventTime, 
            Server server, Integer identifier) {
        super(customer, eventTime);
        this.server = server;
        this.identifier = identifier;
    }

    @Override
    public String getEventName() {
        return WAIT;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String toString() {
        if (server.isSelfServed()) {
            return String.format("%.3f %s waits at self-check %s", this.getEventTime(),
                    this.getCustomer(), identifier);
        }
        return String.format("%.3f %s waits at %s", this.getEventTime(),
                this.getCustomer(), identifier);
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Server updatedServer = shop.getServers().get(server.getId() - 1);
        PQ<Customer> customerPriorityQueue = updatedServer.getWaitingCustomer();
        Double finalFreeTime = updatedServer.getFreeTime();

        Server currServer = new Server(updatedServer.getId(), updatedServer.isBusy(), 
                updatedServer.getMaxQueue(), customerPriorityQueue, 
                updatedServer.getFreeTime(), updatedServer.isSelfServed(), 
                updatedServer.getIsResting());

        ImList<Server> newServers = shop.getServers()
            .set(updatedServer.getId() - 1, currServer);

        Shop newShop = new Shop(newServers, shop.getStatistic(), 
                shop.getRestTime(), shop.getSelfServerQueue());

        return Pair.of(Optional.of(new ServeEvent(this.getCustomer(), 
                        finalFreeTime, currServer, true, this.server.getId())), newShop);
    }
}
