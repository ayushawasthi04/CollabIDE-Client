package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface NotificationSubject {
    public void addObserver(NotificationObserver observer);
    public void notifyObservers(Operation operation);
}
