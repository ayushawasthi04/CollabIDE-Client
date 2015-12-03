package codeEditor.operation.userOperations;

import com.google.gson.Gson;
import codeEditor.operation.EditOperations;
import codeEditor.operation.Operation;

public final class EraseOperation extends Operation implements UserOperations {
    public int lastSyncStamp;
    public int position;
    
    public EraseOperation(String operationId, String userId, int position) {
        super(operationId,  userId, EditOperations.ERASE);
        this.position = position;
    }
    
    public EraseOperation(String json) {
        this.deserialize(json);
    }
    
    public EraseOperation(EraseOperation o) {
        super(o.operationId, o.userId, o.type);
        this.position = o.position;
    }
    
    @Override
    public String serialize() {
        return new Gson().toJson(this);
    }
    
    @Override
    public void deserialize(String json) {
        Gson gson = new Gson();
        EraseOperation jsonEntity = gson.fromJson(json, EraseOperation.class);
        this.position = jsonEntity.position;
        this.userId = jsonEntity.userId;
        this.operationId = jsonEntity.operationId;
        this.type = EditOperations.ERASE;
    }
    
    public void setLastSyncStamp(int lastSyncTimeStamp) {
        this.lastSyncStamp = lastSyncTimeStamp;
    }
}
