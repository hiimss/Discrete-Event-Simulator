package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.PQ;

import java.util.Optional;

class ServeEvent extends EventStub {

    private final Server server;
    private final Boolean isComingFromWaitingQueue;
    private final Integer identifierId;

    private static final int PRIORITY = 3;
    private static final double DECIMAL_ZERO = 0.0;
    private static final String SERVE = "SERVE";

    ServeEvent(Customer customer, double eventTime, 
            Server server, Boolean isComingFromWaitingQueue, 
            Integer identifierId) {
        super(customer, eventTime);
        this.server = server;
        this.isComingFromWaitingQueue = isComingFromWaitingQueue;
        this.identifierId = identifierId;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {

        PQ<Customer> customerPriorityQueue = shop.getServers()
            .get(server.getId() - 1).getWaitingCustomer();
        PQ<Customer> selfServicePriorityQueue = shop.getSelfServerQueue();
        Double waitTime = DECIMAL_ZERO;

        if (isComingFromWaitingQueue) {
            if (server.isSelfServed()) {
                Pair<Customer, PQ<Customer>> customerPair = shop.getSelfServerQueue().poll();
                selfServicePriorityQueue = customerPair.second();
                waitTime = this.getEventTime() - this.getCustomer().getArrivalTime();
            } else {
                Pair<Customer, PQ<Customer>> customerPair = shop.getServers()
                    .get(server.getId() - 1).getWaitingCustomer().poll();
                customerPriorityQueue = customerPair.second();
                waitTime = this.getEventTime() - this.getCustomer().getArrivalTime();
            }
        }

        Double finishServingTime = this.getEventTime() + this.getCustomer().getServiceTime();
        Server currServer = new Server(server.getId(), true, getServer().getMaxQueue(),
                customerPriorityQueue, finishServingTime, 
                server.isSelfServed(), server.getIsResting());
        ImList<Server> newServers = shop.getServers().set(server.getId() - 1, currServer);
        Statistic currStatistic = shop.getStatistic();
        
        Shop newShop = new Shop(newServers,
                new Statistic(currStatistic.getTotalWaiting() + waitTime, 
                    currStatistic.getTotalServing(), currStatistic.getTotalLeave(), 
                    currStatistic.getTotalNormalServe()), 
                shop.getRestTime(), selfServicePriorityQueue);

        return Pair.of(Optional.of(new DoneEvent(this.getCustomer(),
                        finishServingTime, currServer)), newShop);
    }

    @Override
    public String getEventName() {
        return SERVE;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String toString() {
        if (server.isSelfServed()) {
            return String.format("%.3f %s serves by self-check %s", this.getEventTime(),
                    this.getCustomer(), identifierId);
        }
        return String.format("%.3f %s serves by %d", this.getEventTime(), 
                this.getCustomer(), identifierId);
    }
}
