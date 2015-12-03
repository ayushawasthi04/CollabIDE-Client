package codeEditor.operation;

public abstract class Operation {
    public String operationId;
    public String userId;
    public EditOperations type;
    
    public Operation() {    
    }
    
    public Operation(String operationId, String userIdentifier, EditOperations type) {
        this.operationId = operationId;
        this.type = type;
        this.userId = userIdentifier;
    }
    
    public EditOperations getType() {
        return type;
    }
    
    public String getOperationId() {
        return operationId;
    }
    
    public abstract String serialize();
}
