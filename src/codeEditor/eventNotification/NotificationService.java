package codeEditor.eventNotification;

import codeEditor.operation.Operation;
import java.util.ArrayList;

public class NotificationService implements NotificationSubject{
    
    ArrayList<NotificationObserver> observerList = new ArrayList<>();  
    
    @Override
    public void addObserver(NotificationObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObservers(Operation operation) {
        for (NotificationObserver observer: observerList) {
            observer.notifyObserver(operation);
        }
    }
}
