package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.PQ;

import java.util.Optional;

class ArriveEvent extends EventStub {

    private final Boolean needSort;
    
    private static final int PRIORITY = 2;
    private static final int ZERO = 0;
    private static final String ARRIVE = "ARRIVE";

    ArriveEvent(Customer customer, double eventTime, Boolean needSort) {
        super(customer, eventTime);
        this.needSort = needSort;
    }

    @Override
    public String toString() {
        return String.format("%.3f %s arrives", this.getEventTime(), this.getCustomer());
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public String getEventName() {
        return ARRIVE;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {

        ImList<Server> freeServers = shop.getServers().filter(server -> !server.isBusy())
            .filter(server -> !server.getIsResting());

        ImList<Server> eligibleServers = freeServers.filter((server) ->
                server.getFreeTime() <= this.getEventTime());

        if (freeServers.isEmpty() || eligibleServers.isEmpty()) {

            ImList<Server> sortedServers = shop.getServers();

            for (int i = 0; i < sortedServers.size(); i++) {
                Server currServer = sortedServers.get(i);

                if (currServer.isSelfServed()) {

                    PQ<Customer> selfServerQueue = shop.getSelfServerQueue()
                        .add(this.getCustomer());

                    Server currUpdatedServer = new Server(currServer.getId(), 
                            currServer.isBusy(), currServer.getMaxQueue(), 
                            currServer.getWaitingCustomer(), 
                            currServer.getFreeTime(), currServer.isSelfServed(), 
                            currServer.getIsResting());

                    ImList<Server> newServers = shop.getServers()
                        .set(currServer.getId() - 1, currUpdatedServer);

                    Shop newShop = new Shop(newServers, shop.getStatistic(), 
                            shop.getRestTime(), selfServerQueue);

                    if (shop.getSelfServerQueue().size() < currServer.getMaxQueue()) {
                        return Pair.of(Optional.of(new WaitEvent(
                                        this.getCustomer(), this.getEventTime(), currServer, 
                                        currServer.getId())), newShop);
                    }
                } else {
                    PQ<Customer> customerPriorityQueue = currServer.getWaitingCustomer()
                        .add(this.getCustomer());

                    Server currUpdatedServer = new Server(currServer.getId(), 
                            currServer.isBusy(), currServer.getMaxQueue(), customerPriorityQueue, 
                            currServer.getFreeTime(), currServer.isSelfServed(), 
                            currServer.getIsResting());

                    ImList<Server> newServers = shop.getServers()
                        .set(currServer.getId() - 1, currUpdatedServer);
                    Shop newShop = new Shop(newServers, shop.getStatistic(), 
                            shop.getRestTime(), shop.getSelfServerQueue());

                    if (currServer.getWaitingCustomer().size() < currServer.getMaxQueue()) {
                        return Pair.of(Optional.of(new WaitEvent(
                                        this.getCustomer(), this.getEventTime(), 
                                        currServer, currServer.getId())), newShop);
                    }
                }
            }
            return Pair.of(Optional.of(new LeaveEvent(this.getCustomer(), 
                            this.getEventTime())), shop);
        }
        Server selectedServer = eligibleServers.get(ZERO);
        return Pair.of(Optional.of(new ServeEvent(this.getCustomer(), 
                        this.getEventTime(), selectedServer, false, selectedServer.getId())), shop);
    }
}
