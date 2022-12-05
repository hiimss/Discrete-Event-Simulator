package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.PQ;

import java.util.List;
import java.util.Optional;

public class Simulate3 {

    private final Integer totalServers;
    private final PQ<Event> pq;

    private static final double DECIMAL_ZERO = 0.0;

    public Simulate3(Integer totalServers, List<Double> customerArrivalTime) {
        this.totalServers = totalServers;

        ImList<Server> servers = ImList.of();
        for (int i = 0; i < totalServers; i++) {
            Server server = new Server(i + 1);
            servers = servers.add(server);
        }

        Shop shop = new Shop(servers, generateEmptyRestTime(customerArrivalTime.size()));
        EventStub eventStub;
        PQ<Event> currentPq = new PQ<Event>(new EventComparator());

        for (int i = 0; i < customerArrivalTime.size(); i++) {
            Customer customer = new Customer(i + 1, customerArrivalTime.get(i));
            eventStub = new ArriveEvent(customer, customerArrivalTime.get(i), false);
            currentPq = currentPq.add(eventStub);

        }

        PQ<Event> finalPq = new PQ<>(new EventComparator());
        while (!currentPq.isEmpty()) {
            Pair<Event, PQ<Event>> currPair = currentPq.poll();
            Event curr = currPair.first();
            currentPq = currPair.second();
            finalPq = finalPq.add(curr);

            Pair<Optional<Event>, Shop> executePair = curr.execute(shop);
            shop = executePair.second();

            PQ<Event> finalCurrentPq = currentPq;
            currentPq = executePair.first().map((x) -> finalCurrentPq.add(x)).orElse(currentPq);
        }
        this.pq = finalPq;
    }

    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %d", pq, totalServers);
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

    public ImList<Double> generateEmptyRestTime(Integer total) {
        ImList<Double> tempRestTimes = ImList.of();
        for (int i = 0; i < total; i++) {
            tempRestTimes = tempRestTimes.add(DECIMAL_ZERO);
        }
        return tempRestTimes;
    }
}
