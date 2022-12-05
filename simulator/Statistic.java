package cs2030.simulator;

import java.util.List;

class Statistic {

    private final Double totalWaiting;
    private final Integer totalServing;
    private final Integer totalLeave;
    private final Integer totalNormalServe;

    private static final double DECIMAL_ZERO = 0.0;
    private static final int ZERO = 0;

    Statistic(Double totalWaiting, Integer totalServing, 
            Integer totalLeave, Integer totalNormalServe) {
        this.totalWaiting = totalWaiting;
        this.totalServing = totalServing;
        this.totalLeave = totalLeave;
        this.totalNormalServe = totalNormalServe;
    }

    Statistic(Double totalWaiting, 
            Integer totalServing, Integer totalLeave) {
        this.totalWaiting = totalWaiting;
        this.totalServing = totalServing;
        this.totalLeave = totalLeave;
        this.totalNormalServe = 0;
    }

    public Integer getTotalServing() {
        return this.totalServing;
    }

    public Integer getTotalLeave() {
        return this.totalLeave;
    }

    public Double getTotalWaiting() {
        return this.totalWaiting;
    }

    public Double getAverageTotalWaiting() {
        if (totalServing.equals(ZERO)) {
            return DECIMAL_ZERO;
        }
        return this.getTotalWaiting() / this.getTotalServing();
    }

    public Integer getTotalNormalServe() {
        return totalNormalServe;
    }

    @Override
    public String toString() {
        List<String> values = List.of(String.format("%.3f", this.getAverageTotalWaiting()),
                String.valueOf(this.getTotalServing()),
                String.valueOf(this.getTotalLeave()));
        return "[" + String.join(" ", values) + "]";
    }
}
