package codeEditor.eventNotification;

import codeEditor.operation.Operation;
import java.util.ArrayList;

public class EventNotification implements Subject{
    
    ArrayList<Observer> observerList = new ArrayList<>();  
    
    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObservers(Operation operation) {
        for (Observer observer: observerList) {
            observer.notifyObserver(operation);
        }
    }
}
