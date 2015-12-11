package codeEditor.dataControl;

import codeEditor.buffer.SynchronizedBuffer;
import codeEditor.buffer.Buffer;
import codeEditor.model.Model;
import codeEditor.model.Treap;
import codeEditor.eventNotification.EventSubject;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.sessionLayer.AbstractSession;
import exception.OperationNotSupported;

public class EditorCore implements Editor {
    
    Buffer operationBuffer = new SynchronizedBuffer();
    
    private final Model dataModel;
    private final String userId;
    private final String docId;
    private final AbstractSession session;
    private final EventSubject notificationService; 
    
    public EditorCore(String userId, String docId, EventSubject notificationService, AbstractSession session) {
        this.userId = userId;
        this.docId = docId;    
        this.dataModel = new Treap();
        this.notificationService = notificationService;
        this.session = session;
    }
    
    private void insertCharacter(int positionToInsert, char character) {
        dataModel.insert(positionToInsert, character);
    }

    private void eraseCharacter(int positionToErase) {
        dataModel.erase(positionToErase);
    }
    
    @Override
    public char charAt(int positionToSeek) {
        return dataModel.charAt(positionToSeek);
    }
    
    @Override
    public int getSize() {
        return dataModel.getSize();
    }
    
    @Override
    public void performOperation(Operation operation) {
        try {
            switch(operation.getType().toString()) {
                case "INSERT":{
                    InsertOperation insertOperation = (InsertOperation) operation;
                    this.insertCharacter(insertOperation.position, insertOperation.charToInsert);   
                    /*System.err.println("Insert SynTimeStamp => " + insertOperation.getSynTimeStamp());
                    System.err.println("Performing = " + insertOperation);*/
                    session.setLastSynchronized(insertOperation.getSynTimeStamp());
                } break;
                case "ERASE": {
                    EraseOperation eraseOperation = (EraseOperation) operation;
                    this.eraseCharacter(eraseOperation.position);
                    /*System.err.println("Erase SynTimeStamp => " + eraseOperation.getSynTimeStamp());
                    System.err.println("Performing = " + eraseOperation);*/
                    session.setLastSynchronized(eraseOperation.getSynTimeStamp());
                } break;
                case "REPOSITION": {
                } break;    
                default: {
                    throw new OperationNotSupported(operation.getType() + " operation not supported.");
                }
            }
            if (operation.userId.equals(this.userId)) {
                //ignore if character belong to the same user.
            } else {
                notificationService.notifyObservers(operation);
            }
        } catch (OperationNotSupported e){
            e.printStackTrace(System.err);
        }
    }
}
