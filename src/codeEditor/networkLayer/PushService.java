package codeEditor.networkLayer;

import codeEditor.buffer.Buffer;
import java.io.IOException;
import org.apache.http.HttpResponse;

public final class PushService extends Thread implements NetworkHandler{
    public static boolean DO_RETRY = true;
    
    private Buffer buffer;
    
    public PushService(String userId, String docId, Buffer buffer){    
        setBuffer(buffer);
    }
    
    @Override
    public void setBuffer(Buffer buffer){
        this.buffer = buffer;
    }
    
    @Override
    public void handleRequest(Request request) {
        boolean retry = DO_RETRY;
        do {
            try {
                HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());
                handleResponse(response);
                retry = false;
            } catch (IOException e) {
                System.err.println("Unable to send HTTP request. Connection Refused. Retrying...");
            }
        } while (retry);
    }
   
    @Override
    public void handleResponse(HttpResponse response) {
    }
    
    @Override
    public void run(){
        while (!this.isInterrupted()) {
            Request request = (Request) buffer.take();
            handleRequest(request);
        }
    }
}
