package cs2030.simulator;

import cs2030.util.Pair;

import java.util.Optional;

interface Event {

    public double getEventTime();

    public int getPriority();

    public Customer getCustomer();

    public String getEventName();

    public Server getServer();

    public Pair<Optional<Event>, Shop> execute(Shop shop);
}

