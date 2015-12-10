package codeEditor.dataControl;

import codeEditor.operation.Operation;
import codeEditor.buffer.Buffer;

public class Executor extends Thread{
    Editor editorCore;
    Buffer operationBuffer;
    
    public Executor(Editor editorCore, Buffer operationBuffer) {
        this.editorCore = editorCore;
        this.operationBuffer = operationBuffer; 
    }
    
    public void pushOperation(Operation operation) {
        operationBuffer.put(operation);
    }
    
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            Operation operation = (Operation) operationBuffer.take();
            if (operation != null) {
                editorCore.performOperation(operation);
            } else { // can happen when thread is interrupted
                break;
            } 
        }
        System.err.println("Executor Stopped");
    }
}
