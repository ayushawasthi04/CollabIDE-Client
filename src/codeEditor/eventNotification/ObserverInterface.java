package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface ObserverInterface {
    void notifyObserver(Operation operation);
}
