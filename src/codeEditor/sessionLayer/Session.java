package codeEditor.sessionLayer;

import codeEditor.networkLayer.Request;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.RepositionOperation;
import static config.NetworkConfig.PUSH_OPERATIONS;
import static config.NetworkConfig.SERVER_ADDRESS;
import urlbuilder.URLBuilder;

public class Session extends AbstractSession {
    
    public Session(String userId, String docId) {
        super(userId, docId);
    }
    
    private String getPushUrl() {
        URLBuilder urlBuilder = new URLBuilder(); 
        urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(PUSH_OPERATIONS).toString();
        urlBuilder.addParameter("userId", userId).addParameter("docId", docId);
        return urlBuilder.toString();
    }
    
    public void pushOperation(InsertOperation insertOperation){
        insertOperation.lastSyncStamp = this.getLastSynchronized();
        executor.pushOperation((Operation) insertOperation);
        transformation.addOperation(insertOperation);
        pushBuffer.put(new Request(getPushUrl(), insertOperation.serialize()));
    }
    
    public void pushOperation(EraseOperation eraseOperation) {
        eraseOperation.lastSyncStamp = this.getLastSynchronized();
        executor.pushOperation((Operation) eraseOperation);
        transformation.addOperation(eraseOperation);
        pushBuffer.put(new Request(getPushUrl(), eraseOperation.serialize()));    
    }
    
    public void pushOperation(RepositionOperation repositionOperation) {
        pushBuffer.put(new Request(getPushUrl(), repositionOperation.serialize()));    
    }
}