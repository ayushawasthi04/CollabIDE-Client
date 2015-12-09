package codeEditor.buffer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SynchronizedBuffer implements Buffer {   
    BlockingQueue buffer = new LinkedBlockingQueue() {};
    
    @Override
    public void put(Object request) {
        try {
            buffer.put(request);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    @Override
    public Object take() {
        try {
            return buffer.take();
        } catch (InterruptedException ex) {
        }
        return null;
    }
    
    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }
    
}