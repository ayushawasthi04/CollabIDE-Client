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
        
        System.err.println("Pushed");
        System.err.println(insertOperation);
        
        insertOperation.synTimeStamp = this.getLastSynchronized();
        executor.pushOperation((Operation) insertOperation);
        transformation.addOperation(insertOperation);
        pushBuffer.put(insertOperation);
    }
    
    public void pushOperation(EraseOperation eraseOperation) {
        
        System.err.println("Pushed");
        System.err.println(eraseOperation);
        
        eraseOperation.synTimeStamp = this.getLastSynchronized();
        executor.pushOperation((Operation) eraseOperation);
        transformation.addOperation(eraseOperation);
        pushBuffer.put(eraseOperation);    
    }
    
    public void pushOperation(RepositionOperation repositionOperation) {
        pushBuffer.put(repositionOperation);    
    }
}