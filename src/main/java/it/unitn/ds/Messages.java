package it.unitn.ds;

import java.io.Serializable;

import akka.actor.ActorRef;

public class Messages {
    public static class UpdateId {
        public final int epoch;
        public final int seqNum;

        public UpdateId(int _epoch, int _seqNum) {
            epoch = _epoch;
            seqNum = _seqNum;
        }

        // check if this UpdateId is newer than the other UpdateId, first compare the
        // epoch, if they are equal compare the id
        public boolean isNewerThan(UpdateId other) {
            if (this.epoch != other.epoch) {
                return this.epoch > other.epoch;
            }
            return this.seqNum > other.seqNum;
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

        public final UpdateId updateId;

        // keep track on who sent the message
        public final ActorRef client;

        public Update(int _index, int _value, UpdateId _updateId, ActorRef _client) {
            index = _index;
            value = _value;
            updateId = _updateId;
            client = _client;
        }
    }
}
