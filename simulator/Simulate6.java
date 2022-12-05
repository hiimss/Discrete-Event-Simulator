package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.PQ;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Simulate6 {

    private final Integer totalServers;
    private final PQ<Event> pq;
    private final Shop shop;

    private static final String DONE = "DONE";
    private static final String WAIT = "WAIT";
    private static final double DECIMAL_ZERO = 0.0;

    public Simulate6(Integer totalServers, List<Pair<Double,
            Supplier<Double>>> customerArrivalTime, Integer qMax) {
        this.totalServers = totalServers;
        ImList<PQ<Event>> tempQueues = ImList.of();
        ImList<Server> servers = ImList.of();

        for (int i = 0; i < totalServers; i++) {
            Server server = new Server(i + 1, qMax);
            servers = servers.add(server);
            tempQueues = tempQueues.add(new PQ<>(new EventComparator()));
        }

        Shop shop = new Shop(servers, generateEmptyRestTime(customerArrivalTime.size()));
        EventStub eventStub;
        PQ<Event> currentPq = new PQ<Event>(new EventComparator());

        for (int i = 0; i < customerArrivalTime.size(); i++) {
            Customer customer = new Customer(i + 1, customerArrivalTime.get(i).first(),
                    customerArrivalTime.get(i).second());
            eventStub = new ArriveEvent(customer, customerArrivalTime.get(i).first(), false);
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

            if (curr.getEventName().equals(DONE) && !tempQueues
                    .get(curr.getServer().getId() - 1).isEmpty()) {
                Pair<Event, PQ<Event>> waitEventPair = tempQueues
                    .get(curr.getServer().getId() - 1).poll();
                Event waitNextEvent = waitEventPair.first();
                tempQueues = tempQueues.set(curr.getServer().getId() - 1, 
                        waitEventPair.second());
                finalPq = finalPq.add(waitNextEvent);

                Pair<Optional<Event>, Shop> servePair = waitNextEvent.execute(shop);
                PQ<Event> fin = currentPq;
                currentPq = servePair.first().map(v -> fin.add(v)).orElse(currentPq);
                shop = servePair.second();
            } else {
                PQ<Event> finalCurrentPq = currentPq;
                Optional<Event> optionalNextEvent = executePair.first();
                ImList<PQ<Event>> finalTempQueues = tempQueues;

                tempQueues = optionalNextEvent.filter(v -> v.getEventName()
                        .equals(WAIT)).map(v -> {
                            PQ<Event> newPQ = finalTempQueues.get(v.getServer().getId() - 1).add(v);
                            return finalTempQueues.set(v.getServer().getId() - 1, newPQ);
                        }).orElse(tempQueues);

                currentPq = optionalNextEvent.filter(v -> !v.getEventName()
                        .equals(WAIT)).map(v -> finalCurrentPq.add(v)).orElse(currentPq);
            }
        }
        for (PQ<Event> e : tempQueues) {
            while (!e.isEmpty()) {
                Pair<Event, PQ<Event>> currPair = e.poll();
                Event curr = currPair.first();
                e = currPair.second();
                finalPq = finalPq.add(curr);
                Pair<Optional<Event>, Shop> executePair = curr.execute(shop);
                shop = executePair.second();
                PQ<Event> fin = e;
                e = executePair.first().map(v -> fin.add(v)).orElse(e);
            }
        }
        this.pq = finalPq;
        this.shop = shop;
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
            result += (poll.first());
            currPQ = poll.second();
            result += "\n";
        }
        result += shop.getStatistic();
        return result;
    }

    public ImList<Double> generateEmptyRestTime(Integer total) {
        ImList<Double> tempRestTimes = ImList.of();
        for (int i = 0; i < total; i++) {
            tempRestTimes = tempRestTimes.add(DECIMAL_ZERO);
        }
        return tempRestTimes;
    }
}
