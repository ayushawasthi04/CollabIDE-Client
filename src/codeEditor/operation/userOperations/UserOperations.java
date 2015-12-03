package codeEditor.operation.userOperations;

//This is just a Marker Interface that marks UserOperations
import codeEditor.operation.EditOperations;

public interface UserOperations {
    EditOperations getType();
    String serialize();
    void deserialize(String json);
}
