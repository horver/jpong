package bme.pong.networking;

import java.util.concurrent.LinkedBlockingQueue;

import bme.pong.networking.gameevents.*;

public class EventBus {
    final int MAX_QUEUE_EVENTS = 10;
    private LinkedBlockingQueue<IGameEvent> _incoming; // Events received from opponent
    private LinkedBlockingQueue<IGameEvent> _outgoing; // Events to be sent to the opponent

    public EventBus() {
        this._incoming = new LinkedBlockingQueue<IGameEvent>(MAX_QUEUE_EVENTS);
        this._outgoing = new LinkedBlockingQueue<IGameEvent>(MAX_QUEUE_EVENTS);
    }

    public void pushOutgoing(IGameEvent event) {
        this._outgoing.add(event);
    }

    public void pushIncoming(IGameEvent event) {
        this._incoming.add(event);
    }

    public IGameEvent popIncoming() {
        return this._incoming.poll();
    }

    public IGameEvent popIncomingBlocking() throws InterruptedException {
        return this._incoming.take();
    }

    public IGameEvent popOutgoing() {
        return this._outgoing.poll();

    }

    public IGameEvent popOutgoingBlocking() throws InterruptedException {
        return this._outgoing.take();
    }
}
