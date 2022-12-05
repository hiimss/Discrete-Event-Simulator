package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.PQ;

import java.util.List;

public class Simulate2 {

    private final Integer totalServers;
    private final PQ<Event> pq;
    private final Shop shop;

    private static final double DECIMAL_ZERO = 0.0;

    public Simulate2(Integer totalServers, List<Double> customerArrivalTime) {
        this.totalServers = totalServers;

        ImList<Server> servers = ImList.of();
        for (int i = 0; i < totalServers; i++) {
            Server server = new Server(i + 1);
            servers = servers.add(server);
        }
        
        Shop shop = new Shop(servers);
        ImList<Double> tempRestTime = ImList.of();
        PQ<Event> currentPq = new PQ<Event>(new EventComparator());

        for (int i = 0; i < customerArrivalTime.size(); i++) {
            Customer customer = new Customer(i + 1, customerArrivalTime.get(i));
            EventStub eventStub = new EventStub(customer, customerArrivalTime.get(i));
            currentPq = currentPq.add(eventStub);
            tempRestTime = tempRestTime.add(DECIMAL_ZERO);
            shop = new Shop(servers, tempRestTime);
        }
        this.shop = shop;
        this.pq = currentPq;
    }

    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s", pq, shop);
    }

    public String run() {
        PQ<Event> currPQ = this.pq;
        String result = "";
        while (!currPQ.isEmpty()) {
            Pair<Event, PQ<Event>> poll = currPQ.poll();
            result += (poll.first() + "\n");
            currPQ = poll.second();
        }
        return result + "-- End of Simulation --";
    }
}
