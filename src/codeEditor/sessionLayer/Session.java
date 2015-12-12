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
        /*updateState.lock();
        try {*/
            insertOperation.setSynTimeStamp(this.getLastSynchronized());
            executor.pushOperation((Operation) insertOperation);
            transformation.addOperation(insertOperation);
            pushBuffer.put(insertOperation);
        /*} finally {
            updateState.unlock();
        }*/
    }
    
    public void pushOperation(EraseOperation eraseOperation) {
        /*updateState.lock();
        try {*/
            eraseOperation.setSynTimeStamp(this.getLastSynchronized());
            executor.pushOperation((Operation) eraseOperation);
            transformation.addOperation(eraseOperation);
            pushBuffer.put(eraseOperation);
        /*} finally {
            updateState.unlock();
        }*/
    }
    
    public void pushOperation(RepositionOperation repositionOperation) {
        pushBuffer.put(repositionOperation);    
    }
}