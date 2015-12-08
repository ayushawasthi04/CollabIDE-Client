package codeEditor.networkLayer;

import codeEditor.buffer.Buffer;
import codeEditor.operation.Operation;
import com.google.gson.Gson;
import static config.NetworkConfig.PUSH_OPERATIONS;
import static config.NetworkConfig.SERVER_ADDRESS;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import urlbuilder.URLBuilder;

public final class PushService extends Thread implements NetworkHandler{
    public static boolean DO_RETRY = true;
    
    private Buffer buffer;
    private final String userId;
    private final String docId;
    
    public PushService(String userId, String docId, Buffer buffer){    
        this.userId = userId;
        this.docId = docId;
        setBuffer(buffer);
    }
    
    private String getPushUrl() {
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(PUSH_OPERATIONS).toString();
        urlBuilder.addParameter("userId", userId).addParameter("docId", docId);
        return urlBuilder.toString();
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
    
    public void setBuffer(Buffer buffer){
        this.buffer = buffer;
    }
    
    @Override
    public void run(){
        ArrayList<Operation> operationsToPush = new ArrayList<>();
        while (!this.isInterrupted()) {
            do {
               Operation operation = (Operation) buffer.take();
               operationsToPush.add(operation);
            } while (!buffer.isEmpty() && operationsToPush.size() < 5);
            Request request = new Request(getPushUrl(), new Gson().toJson(operationsToPush));
            handleRequest(request);
            operationsToPush.clear();
        }
    }
    
}
