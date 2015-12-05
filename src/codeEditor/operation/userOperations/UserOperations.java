package codeEditor.operation.userOperations;

import codeEditor.operation.EditOperations;

public interface UserOperations {
    EditOperations getType();
    String serialize();
    void deserialize(String json);
}
