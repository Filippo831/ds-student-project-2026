package it.unitn.ds;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Replica extends AbstractReplica {
    private Map<Integer, ActorRef> group;
    private int coordinatorId;

    private int epoch;
    private int seqNum;


    public Replica(int id) {
        this(id, AbstractReplica.MIN_LATENCY, AbstractReplica.MAX_LATENCY, AbstractReplica.COORDINATOR_BEAT_INTERVAL,
                Optional.empty());
    }

    public Replica(int id, int minLatency, int maxLatency, int coordinatorBeatInterval, Optional<ActorRef> listener) {
        super(id, minLatency, maxLatency, coordinatorBeatInterval, listener);
        // TODO: implement
        this.group = new HashMap<>();
        this.coordinatorId = -1;
        this.epoch = 0;
        this.seqNum = 0;
    }

    public static Props props(int id, int minLatency, int maxLatency, int coordinatorBeatInterval) {
        return Props.create(Replica.class,
                () -> new Replica(id, minLatency, maxLatency, coordinatorBeatInterval, Optional.empty()));
    }

    // Props method for automated tests
    public static Props propsWithListener(int id, int minLatency, int maxLatency, int coordinatorBeatInterval,
            ActorRef listener) {
        return Props.create(Replica.class,
                () -> new Replica(id, minLatency, maxLatency, coordinatorBeatInterval, Optional.ofNullable(listener)));
    }

    private final void handleUpdateRequest(Messages.UpdateRequest _msg) throws Exception {
        System.out.println("received UpdateRequest from client");
        if (this.id == coordinatorId) {
            // if is the coordinator who received the updateRequest, send an UPDATE to the
            // replicas
            System.out.println("I am the coordinator, sending UPDATE to replicas");
            for (Map.Entry<Integer, ActorRef> entry : group.entrySet()) {
                if (entry.getKey() != this.id) {
                    entry.getValue().tell(new Messages.Update(msg.index, msg.value, UpdateId(this.epoch, this.seqNum), getSelf()),
                            getSelf());
                }
            }
        }
    }

    @Override
    public int getSystemNumberOfActors() {
        return group.size();
    }

    @Override
    public void crash(AbstractReplica.Crash how_to_crash) {
        // TODO: implement
    }

    @Override
    public void initSystem(InitSystem sysInit) {
        // TODO: implement
        this.group = sysInit.group;
        this.coordinatorId = sysInit.coordinator_id;
        System.out.println("replica init");
    }

    @Override
    public final Receive createReceive() {
        return createBaseReceiveBuilder()
                // TODO: add your message handlers here .match(, )
                .match(AbstractReplica.InitSystem.class, this::initSystem)
                .match(Messages.UpdateRequest.class, this::handleUpdateRequest)
                .build();
    }

}
