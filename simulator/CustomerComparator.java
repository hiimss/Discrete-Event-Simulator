package cs2030.simulator;

import java.util.Comparator;

class CustomerComparator implements Comparator<Customer> {

    @Override
    public int compare(Customer o1, Customer o2) {
        return Double.compare(o1.getArrivalTime(), o2.getArrivalTime());
    }
}
