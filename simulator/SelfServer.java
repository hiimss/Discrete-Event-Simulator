package cs2030.simulator;

class SelfServer extends Server {

    public SelfServer(Integer id, Integer maxQueue) {
        super(id, maxQueue, true);
    }
}
