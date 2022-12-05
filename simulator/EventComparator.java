package cs2030.simulator;

import java.util.Comparator;

class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        if (o1.getEventTime() == o2.getEventTime()) {
            if (o1.getCustomer().getId().equals(o2.getCustomer().getId())) {
                return Integer.compare(o1.getPriority(), o2.getPriority());
            }
            return Integer.compare(o1.getCustomer().getId(), o2.getCustomer().getId());
        }
        return Double.compare(o1.getEventTime(), o2.getEventTime());
    }
}
