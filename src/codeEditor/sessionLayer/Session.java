package codeEditor.sessionLayer;

import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.RepositionOperation;
import config.Debug;
import static config.Debug.KEYLOG_DEBUG;

public class Session extends AbstractSession {
    
    public Session(String userId, String docId) {
        super(userId, docId);
    }
    
    public void pushOperation(InsertOperation insertOperation){
        Debug.log("Key Pressed Notified", KEYLOG_DEBUG);
        
        insertOperation.setSynTimeStamp(this.getLastSynchronized());
        executor.pushOperation((Operation) insertOperation);
        transformation.addOperation(insertOperation);
        pushBuffer.put(insertOperation);
    }
    
    public void pushOperation(EraseOperation eraseOperation) {
        Debug.log("Key Pressed Notified", KEYLOG_DEBUG);
        
        eraseOperation.setSynTimeStamp(this.getLastSynchronized());
        executor.pushOperation((Operation) eraseOperation);
        transformation.addOperation(eraseOperation);
        pushBuffer.put(eraseOperation);
    }
    
    public void pushOperation(RepositionOperation repositionOperation) {
        pushBuffer.put(repositionOperation);    
    }
}