package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.PQ;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Simulate8 {

    private final Integer totalServers;
    private final PQ<Event> pq;
    private final Shop shop;

    private static final String DONE = "DONE";
    private static final String WAIT = "WAIT";

    public Simulate8(Integer totalServers, Integer selfServers, 
            List<Pair<Double, Supplier<Double>>> customerArrivalTime, 
            Integer qMax, Supplier<Double> restTime) {
        this.totalServers = totalServers;
        ImList<PQ<Event>> tempQueues = ImList.of();
        ImList<Server> servers = ImList.of();
        ImList<Double> restTimes = ImList.of();

        for (int i = 0; i < totalServers; i++) {
            Server server = new Server(i + 1, qMax);
            servers = servers.add(server);
            tempQueues = tempQueues.add(new PQ<>(new EventComparator()));
        }

        for (int i = 0; i < selfServers; i++) {
            SelfServer server = new SelfServer(totalServers + 1 + i, qMax);
            servers = servers.add(server);
        }
        tempQueues = tempQueues.add(new PQ<>(new EventComparator()));

        EventStub eventStub;
        PQ<Event> currentPq = new PQ<Event>(new EventComparator());

        for (int i = 0; i < customerArrivalTime.size(); i++) {
            Customer customer = new Customer(i + 1, customerArrivalTime.get(i).first(),
                    customerArrivalTime.get(i).second());
            eventStub = new ArriveEvent(customer, customerArrivalTime.get(i).first(), true);
            currentPq = currentPq.add(eventStub);
            restTimes = restTimes.add(restTime.get());
        }

        Shop shop = new Shop(servers, restTimes);

        PQ<Event> finalPq = new PQ<>(new EventComparator());
        while (!currentPq.isEmpty()) {
            Pair<Event, PQ<Event>> currPair = currentPq.poll();
            Event curr = currPair.first();
            currentPq = currPair.second();
            finalPq = finalPq.add(curr);

            Pair<Optional<Event>, Shop> executePair = curr.execute(shop);
            shop = executePair.second();

            Integer index = curr.getServer().getId();
            Server currServer = curr.getServer();
            if (!curr.getEventName().equals("ARRIVE") && !curr.getEventName().equals("LEAVE")) {
                currServer = shop.getServers().get(curr.getServer().getId() - 1);
            }

            if (currServer.isSelfServed()) {
                index = totalServers + 1;
            }

            if (curr.getEventName().equals(DONE) && !currServer.getIsResting() && 
                    !tempQueues.get(index - 1).isEmpty()) {
                Pair<Event, PQ<Event>> waitEventPair = tempQueues.get(index - 1).poll();
                Event currWaitEvent = waitEventPair.first();
                Event waitNextEvent = new WaitEvent(currWaitEvent.getCustomer(), 
                        currWaitEvent.getEventTime(), currServer, index);
                tempQueues = tempQueues.set(index - 1, waitEventPair.second());
                finalPq = finalPq.add(waitNextEvent);

                Pair<Optional<Event>, Shop> servePair = waitNextEvent.execute(shop);
                PQ<Event> fin = currentPq;
                currentPq = servePair.first().map(v -> fin.add(v)).orElse(currentPq);
                shop = servePair.second();
            } else if (curr.getEventName().equals("REST") && !tempQueues.get(index - 1).isEmpty()) {
                Pair<Event, PQ<Event>> waitEventPair = tempQueues.get(index - 1).poll();
                Event waitNextEvent = waitEventPair.first();
                tempQueues = tempQueues.set(index - 1, waitEventPair.second());
                finalPq = finalPq.add(waitNextEvent);

                Pair<Optional<Event>, Shop> servePair = waitNextEvent.execute(shop);
                PQ<Event> fin = currentPq;
                currentPq = servePair.first().map(v -> fin.add(v)).orElse(currentPq);
                shop = servePair.second();
            } else {
                PQ<Event> finalCurrentPq = currentPq;
                Optional<Event> optionalNextEvent = executePair.first();
                ImList<PQ<Event>> finalTempQueues = tempQueues;
                tempQueues = optionalNextEvent.filter(v -> v.getEventName().equals(WAIT))
                    .map(v -> {
                        Integer idx = v.getServer().getId() - 1;
                        if (v.getServer().isSelfServed()) {
                            idx = totalServers;
                        }
                        PQ<Event> newPQ = finalTempQueues.get(idx).add(v);
                        return finalTempQueues.set(idx, newPQ);
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
            if (!poll.first().getEventName().equals("REST")) {
                result += (poll.first() + "\n");
            }
            currPQ = poll.second();
        }
        result += shop.getStatistic();

        return result;
    }
}
