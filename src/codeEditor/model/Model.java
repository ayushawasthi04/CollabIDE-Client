package codeEditor.model;

public interface Model {
    void insert(int position, char character);
    void erase(int position);
    char charAt(int position);
    
//Created only for Debugging Purposes
    int getSize();
    void getTree();
}
