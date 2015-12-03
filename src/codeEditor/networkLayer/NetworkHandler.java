package codeEditor.networkLayer;

import codeEditor.buffer.Buffer;
import org.apache.http.HttpResponse;

public interface NetworkHandler {
    void setBuffer(Buffer buffer);
    void handleRequest(Request request);
    void handleResponse(HttpResponse response);
    
    void start();
    void interrupt();
}
