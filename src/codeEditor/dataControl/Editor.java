package codeEditor.dataControl;

import codeEditor.operation.Operation;

public interface Editor {
    void performOperation(Operation operation); 
   
    //Debugging Information
    int getSize();
    char charAt(int positionToErase);
}
