package codeEditor.operation.userOperations;

import com.google.gson.Gson;
import codeEditor.operation.EditOperations;
import codeEditor.operation.Operation;

public class InsertOperation extends Operation implements UserOperations {
    public int lastSyncStamp;
    public int position;
    public char charToInsert;
    
    public InsertOperation(String json) {
        deserialize(json);
    }
    
    public InsertOperation(String operationId, String userId, int position, char charToInsert){
        super(operationId, userId, EditOperations.INSERT);
        this.charToInsert = charToInsert;       
        this.position = position;
    }
    
    public InsertOperation(InsertOperation o) {
        super(o.operationId, o.userId,  o.type);
        this.charToInsert = o.charToInsert;
        this.position = o.position;    
    }
    
    @Override
    public String serialize() {
        return new Gson().toJson(this);
    }
    
    @Override
    public final void deserialize(String json) {
        Gson gson = new Gson();
        InsertOperation jsonEntity = gson.fromJson(json, InsertOperation.class);
        this.charToInsert = jsonEntity.charToInsert;
        this.position = jsonEntity.position;
        this.userId = jsonEntity.userId;
        this.operationId = jsonEntity.operationId;
        this.type = EditOperations.INSERT;
    }   

    public void setLastSyncStamp(int lastSyncStamp) {
        this.lastSyncStamp = lastSyncStamp;
    }
}