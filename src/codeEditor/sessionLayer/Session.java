package codeEditor.sessionLayer;

import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.RepositionOperation;

public class Session extends AbstractSession {
    
    public Session(String userId, String docId) {
        super(userId, docId);
    }
    
    public void pushOperation(InsertOperation insertOperation){
        insertOperation.lastSyncStamp = this.getLastSynchronized();
        executor.pushOperation((Operation) insertOperation);
        transformation.addOperation(insertOperation);
        pushBuffer.put(insertOperation);
    }
    
    public void pushOperation(EraseOperation eraseOperation) {
        eraseOperation.lastSyncStamp = this.getLastSynchronized();
        executor.pushOperation((Operation) eraseOperation);
        transformation.addOperation(eraseOperation);
        pushBuffer.put(eraseOperation);    
    }
    
    public void pushOperation(RepositionOperation repositionOperation) {
        pushBuffer.put(repositionOperation);    
    }
}