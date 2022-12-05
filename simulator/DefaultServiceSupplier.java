package cs2030.simulator;

import java.util.function.Supplier;

class DefaultServiceSupplier implements Supplier<Double> {

    private static final double DECIMAL_ONE = 1.0;

    @Override
    public Double get() {
        return DECIMAL_ONE;
    }
}
