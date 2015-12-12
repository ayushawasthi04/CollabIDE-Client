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
            //Perform The operation on the model
            switch(operation.getType().toString()) {
                case "INSERT":{
                    InsertOperation insertOperation = (InsertOperation) operation;
                    this.insertCharacter(insertOperation.position, insertOperation.charToInsert);   
                } break;
                case "ERASE": {
                    EraseOperation eraseOperation = (EraseOperation) operation;
                    this.eraseCharacter(eraseOperation.position);
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
                //update the lastSynchrnonized 
                switch(operation.getType().toString()) {
                    case "INSERT":{
                        InsertOperation insertOperation = (InsertOperation) operation;
                        session.setLastSynchronized(insertOperation.getSynTimeStamp()+1);
                    } break;
                    case "ERASE": {
                        EraseOperation eraseOperation = (EraseOperation) operation;
                        session.setLastSynchronized(eraseOperation.getSynTimeStamp()+1);
                    } break;
                    case "REPOSITION": {
                    } break;    
                    default: {
                        throw new OperationNotSupported(operation.getType() + " operation not supported.");
                    }
                }
                notificationService.notifyObservers(operation);
            }
        } catch (OperationNotSupported e){
            e.printStackTrace(System.err);
        }
    }
}
