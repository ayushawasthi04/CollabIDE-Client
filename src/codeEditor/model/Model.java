package codeEditor.model;

public interface Model {
    void insert(int position, char character);
    void erase(int position);
    char charAt(int position);
    int getSize();
    
//Created only for Debugging Purposes
    void getTree();
}
