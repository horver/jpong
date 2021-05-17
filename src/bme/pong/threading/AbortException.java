package bme.pong.threading;

public class AbortException extends Exception {
    public AbortException(String msg) {
        super(msg);
    }
}