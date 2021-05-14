package bme.pong.networking;

import java.util.concurrent.atomic.AtomicBoolean;

public class AbortHandler implements AbortInterface {
    private AtomicBoolean _shouldExit = new AtomicBoolean(false);

    // Call this periodically to check exit condition
    public void check() throws AbortException {
        if (_shouldExit.get()) {
            throw new AbortException("");
        }
    }

    public void abort(String reason) throws AbortException {
        _shouldExit.set(true);
        throw new AbortException(reason);
    }
}
