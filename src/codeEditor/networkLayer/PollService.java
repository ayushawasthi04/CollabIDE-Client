package codeEditor.networkLayer;

import static config.NetworkConfig.POLLING_THREAD_SLEEP_TIME;
import codeEditor.operation.Deserializer;
import codeEditor.dataControl.Editor;
import codeEditor.operation.Operation;
import codeEditor.sessionLayer.AbstractSession;
import codeEditor.transform.Transformation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import static config.NetworkConfig.DEBUG;
import static config.NetworkConfig.GET_OPERATIONS;
import static config.NetworkConfig.SERVER_ADDRESS;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import urlbuilder.URLBuilder;

public final class PollService extends Thread implements NetworkHandler{
    private String userId;
    private String docId;
    private Transformation tranformation;
    private Editor model;
    private AbstractSession session;
  
    @Override
    public void handleRequest(Request request) {
        try {
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());    
            handleResponse(response);
        } catch (IOException e) {
            System.err.println("Unable to send HTTP request. Connection Refused. Retrying...");
        }
    }
    
    @Override
    public void handleResponse(HttpResponse response){
        HttpEntity httpEntity = response.getEntity();
        try {
            InputStream inStream = httpEntity.getContent();
            String content = IOUtils.toString(inStream);
            if (content.equals("")) {
            } else {
                new Thread(()->{
                    try {
                        session.lock();

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Operation.class, new Deserializer());
                        Gson gson = gsonBuilder.create();

                        java.lang.reflect.Type listType = new TypeToken<ArrayList<Operation>>() {}.getType();
                        ArrayList<Operation> list = gson.fromJson(content, listType);

                        if (DEBUG) {
                            System.err.println();
                            System.err.println("Received = " + list);
                        }
                        
                        ArrayList<Operation> transformed = this.tranformation.transform(list);

                        if (DEBUG) {
                            System.err.println();
                            System.err.println("Transformed = " + transformed);
                        }
                        
                        for (Operation o: transformed) {
                            model.performOperation(o);
                        }
                    } catch (InterruptedException ex) {
                    } finally {
                        session.unlock();
                    } 
                }).start();
            }
        } catch (IOException | UnsupportedOperationException ex) {
            ex.printStackTrace(System.err);
        } 
    }
    
    @Override
    public void run(){ 
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(GET_OPERATIONS);
        urlBuilder.addParameter("userId", userId).addParameter("docId", docId);
        String getRequest = urlBuilder.toString();
        
        while (!this.isInterrupted()) {
            handleRequest(new Request(getRequest, ""));
            try {
                PollService.sleep(POLLING_THREAD_SLEEP_TIME);
            } catch (InterruptedException ex) {
                break;
            }
        }
        System.err.println("Poll Stopped");
    }

    //Setters for Builder
    public PollService setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public PollService setDocId(String docId) {
        this.docId = docId;
        return this;
    }

    public PollService setTranformation(Transformation tranformation) {
        this.tranformation = tranformation;
        return this;
    }

    public PollService setSession(AbstractSession session) {
        this.session = session;
        return this;
    }
    
    public PollService setEditor(Editor model){
        this.model = model;
        return this;
    }
    
}
