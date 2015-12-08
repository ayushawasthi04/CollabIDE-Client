package codeEditor.operation;

public enum OperationType {
    INSERT("INSERT"),
    ERASE("ERASE"),
    REGISTER("REGISTER"),
    REPOSITION("REPOSITION");
      
    private final String operation;
    
    private OperationType(String operation){
        this.operation = operation;
    }
    
    @Override
    public String toString(){
        return this.operation;
    }
}