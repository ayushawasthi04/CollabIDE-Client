package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface Subject {
    public void addObserver(Observer observer);
    public void notifyObservers(Operation operation);
}
