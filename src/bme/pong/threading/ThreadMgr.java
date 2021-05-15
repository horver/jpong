package bme.pong.threading;

import java.util.ArrayList;

public class ThreadMgr {
    private ArrayList<Thread> _threads;

    public ThreadMgr() {
        _threads = new ArrayList<>();
    }

    public void startThread(Runnable target, String threadName) {
        Thread t = new Thread(target, threadName);
        _threads.add(t);

        t.start();
    }

    public void nukeThread(String threadName) {
        for (Thread it : _threads) {
            if (it.getName() == threadName) {
                it.interrupt();
                _threads.remove(it);
                return;
            }
        }
    }

    public void nukeAll() {
        for (Thread t : _threads) {
            System.out.println("Nuking thread: " + t.getName());
            t.interrupt();
        }
        //_threads.stream().forEach(t -> t.interrupt());
    }
}
