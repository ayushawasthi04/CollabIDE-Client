package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface EventObserver {
    void notifyObserver(Operation operation);
}
