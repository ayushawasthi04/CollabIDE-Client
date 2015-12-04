package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface NotificationObserver {
    void notifyObserver(Operation operation);
}
