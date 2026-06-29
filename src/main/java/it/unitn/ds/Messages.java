package it.unitn.ds;

import java.io.Serializable;

import akka.actor.ActorRef;

public class Messages {
    public static class Clock{
        public final int epoch;
        public final int seqNum;

        public Clock(int _epoch, int _seqNum) {
            epoch = _epoch;
            seqNum = _seqNum;
        }

        // check if this UpdateId is newer than the other UpdateId, first compare the
        // epoch, if they are equal compare the id
        public boolean isNewerThan(Clock other) {
            if (this.epoch != other.epoch) {
                return this.epoch > other.epoch;
            }
            return this.seqNum > other.seqNum;
        }

        public void incrementSeqNum() {
            this.seqNum++;
        }

        public void incrementEpoch() {
            // when changing epoch, reset the seqNum to 0
            this.epoch++;
            this.seqNum = 0;
        }
    }

    public static class UpdateRequest implements Serializable {
        public final int index;
        public final int value;

        // keep track on who sent the message
        public final ActorRef client;

        public UpdateRequest(int _index, int _value, ActorRef _client) {
            index = _index;
            value = _value;
            client = _client;
        }
    }

    public static class ReadRequest implements Serializable {
        public final int index;

        // keep track on who sent the message
        public final ActorRef client;

        public ReadRequest(int _index, ActorRef _client) {
            index = _index;
            client = _client;
        }
    }


    public static class Update implements Serializable {
        public final int index;
        public final int value;

        public final Clock clock ;

        public Update(int _index, int _value, Clock _clock) {
            index = _index;
            value = _value;
            clock = _clock;
        }
    }
    public static class Ack implements Serializable {
        public final Clock clock;

        public Update(Clock _clock) {
            clock = _clock;
        }
    }
}
