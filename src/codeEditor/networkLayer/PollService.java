package codeEditor.networkLayer;

import static config.NetworkConfig.GET_OPERATIONS_URL;
import static config.NetworkConfig.POLLING_THREAD_SLEEP_TIME;
import codeEditor.operation.Deserializer;
import codeEditor.buffer.Buffer;
import codeEditor.operation.Operation;
import codeEditor.sessionLayer.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class PollService extends Thread implements NetworkHandler{
    private final String userId;
    private final String docId;
    
    private Buffer buffer;
    
    public PollService(String userId, String docId, Buffer buffer){    
        this.userId = userId;
        this.docId = docId;
        setBuffer(buffer);
    }
    
    @Override
    public void setBuffer(Buffer buffer){
        this.buffer = buffer;
    }
    
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
                JSONObject jsonObject = new JSONObject(content);
                Session.lastSyncStamp = (Integer) jsonObject.get("last_sync");
                JSONArray operations = (JSONArray) jsonObject.get("operations");
                
                //System.out.println(lastSyncStamp);
                //System.out.println(operations.toString());
                
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Operation.class, new Deserializer());
                Gson gson = gsonBuilder.create();
                
                java.lang.reflect.Type listType = new TypeToken<ArrayList<Operation>>() {}.getType();
                ArrayList<Operation> list = gson.fromJson(operations.toString(), listType);
                buffer.put(list);
            }
        } catch (IOException | UnsupportedOperationException ex) {
            ex.printStackTrace(System.err);
        } catch (JSONException ex) {
            Logger.getLogger(PollService.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @Override
    public void run(){
        String getMessage = GET_OPERATIONS_URL + "?userId=" + userId + "&docId=" + docId;
        while (!this.isInterrupted()) {
            handleRequest(new Request(getMessage, ""));
            try {
                Thread.sleep(POLLING_THREAD_SLEEP_TIME);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    @Override
    public void interrupt() {
        this.interrupt();
    }
}
