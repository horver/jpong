package bme.pong.threading;

import java.util.concurrent.atomic.AtomicBoolean;

public class AbortHandler implements AbortInterface {
    private AtomicBoolean _shouldExit = new AtomicBoolean(false);
    private String _reason = ""; // No need for atomic reference

    // Call this periodically to check exit condition
    public void check() throws AbortException {
        if (_shouldExit.get()) {
            throw new AbortException(_reason);
        }
    }

    public void abort(String reason) {
        _reason = reason;
        _shouldExit.set(true);
    }
}
