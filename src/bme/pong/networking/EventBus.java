package bme.pong.networking;

import bme.pong.networking.gameevents.IGameEvent;

import java.util.concurrent.LinkedBlockingQueue;

public class EventBus {
    final int MAX_QUEUE_EVENTS = 10;
    private LinkedBlockingQueue<IGameEvent> _incoming; // Events received from opponent
    private LinkedBlockingQueue<IGameEvent> _outgoing; // Events to be sent to the opponent

    public EventBus() {
        this._incoming = new LinkedBlockingQueue<IGameEvent>(MAX_QUEUE_EVENTS);
        this._outgoing = new LinkedBlockingQueue<IGameEvent>(MAX_QUEUE_EVENTS);
    }

    void pushOutgoing(IGameEvent event) {
        this._outgoing.add(event);
    }

    void pushIncoming(IGameEvent event) {
        this._incoming.add(event);
    }

    IGameEvent popIncoming() {
        return this._incoming.poll();
    }

    IGameEvent popOutgoing() {
        return this._outgoing.poll();
    }
}
