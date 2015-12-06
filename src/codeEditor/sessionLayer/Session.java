package codeEditor.sessionLayer;

import codeEditor.networkLayer.Request;
import codeEditor.operation.EditOperations;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.RepositionOperation;
import codeEditor.operation.userOperations.UserOperations;
import static config.NetworkConfig.PUSH_OPERATIONS;
import static config.NetworkConfig.SERVER_ADDRESS;
import exception.OperationNotExistException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        executeOperationThread.pushOperation((Operation) insertOperation);
        transformation.addOperation(insertOperation);
        requestBuffer.put(new Request(getPushUrl(), insertOperation.serialize()));
    }
    
    public void pushOperation(EraseOperation eraseOperation) {
        eraseOperation.lastSyncStamp = this.getLastSynchronized();
        executeOperationThread.pushOperation((Operation) eraseOperation);
        transformation.addOperation(eraseOperation);
        requestBuffer.put(new Request(getPushUrl(), eraseOperation.serialize()));    
    }
    
    public void pushOperation(RepositionOperation repositionOperation) {
        requestBuffer.put(new Request(getPushUrl(), repositionOperation.serialize()));    
    }
}