package bme.pong.networking;

// Interface for aborting long-running operations, e.g. infinite loops
public interface AbortInterface {
    public void abort(String reason) throws AbortException;
    public void check() throws AbortException;
}
