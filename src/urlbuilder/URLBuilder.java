package urlbuilder;

import java.util.HashMap;
import java.util.Map;

public class URLBuilder { 
    private String serverAddress;
    private String method;
    private HashMap<String, String> query;
    
    public URLBuilder setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }
    
    public URLBuilder setMethod(String method) {
        this.method = method;
        return this;
    }
    
    public URLBuilder addParameter(String parameter, String argument) {
        query.put(parameter, argument);
        return this;
    }
    
    @Override
    public String toString() {
        String url = "";
        url += serverAddress;
        url += "/";
        url += method;
        url += "?";
        for (Map.Entry<String, String> entry: query.entrySet()) {
            url += entry.getKey() + "=" + entry.getValue();
            url += "&";
        }
        return url;
    }
}
