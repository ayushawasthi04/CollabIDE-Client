package codeEditor.transform;

import codeEditor.operation.Operation;
import codeEditor.transform.operationalTransform.CompoundOT;
import java.util.ArrayList;

public class Transformation{
    private final String userId;
    
    public Transformation(String userId) {
        this.userId = userId;
    }  
    
    private final ArrayList<Operation> localOperations = new ArrayList<>();
  
    public ArrayList<Operation> transform(ArrayList<Operation> remoteOperations) {
        ArrayList<Operation> transformedOperations;
        if (localOperations.isEmpty()) {
            transformedOperations = remoteOperations;
        } else {
            transformedOperations = CompoundOT.performTransform(userId, localOperations, remoteOperations);
        }
        return transformedOperations;
    }
    
    public void addOperation(Operation operation) {
        localOperations.add(operation);
    }
}
