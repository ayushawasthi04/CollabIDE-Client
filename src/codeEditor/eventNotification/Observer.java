package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface Observer {
    void notifyObserver(Operation operation);
}
