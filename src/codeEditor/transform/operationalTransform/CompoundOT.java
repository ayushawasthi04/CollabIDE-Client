package codeEditor.transform.operationalTransform;

import codeEditor.operation.Operation;
import java.util.ArrayList;

public class CompoundOT {
    public static ArrayList<Operation> performTransform(String userId, ArrayList<Operation> localOperations, ArrayList<Operation> remoteOperations) {    
            ArrayList<Operation> transformedOperations = new ArrayList<>();
            for (Operation remote: remoteOperations) {
                //System.out.println("My userId : " + userId);
                //System.out.println("Operation userId :" + remote.userId);
                if (remote.userId.equals(userId)) {
                    //System.out.println("Same User");
                    for (int i = 0; i < localOperations.size(); i++) {
                        if (localOperations.get(i).operationId.equals(remote.operationId)) {
                            //System.out.println("Removed : " + remote.operationId);
                            localOperations.remove(i);
                            break;
                        }
                    }
                } else {
                    //System.out.println("Different User");
                    ArrayList<Operation> newLocalOperations = new ArrayList<>();
                    for (Operation local: localOperations) {
                        //System.out.println("local remote " + local.operationId + " " + remote.operationId);
                        Operation transformed1 = OperationalTransform.transform(local, remote, true);
                        Operation transformed2 = OperationalTransform.transform(remote, local, false);
                        remote = transformed1;
                        newLocalOperations.add(transformed2);
                    }
                    localOperations.clear();
                    for (Operation operation: newLocalOperations) {
                        localOperations.add(operation);
                    }
                    transformedOperations.add(remote);
                }
            }
            return transformedOperations;
    }
}