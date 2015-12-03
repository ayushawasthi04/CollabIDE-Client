package notificationService;

import codeEditor.networkLayer.SendPostRequest;
import static config.NetworkConfig.CLEAR_NOTIFICATION_URL;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import notificationService.notifyObservers.NotificationObserver;
import notificationService.notifyObservers.Notify;
import notificationService.notifyObservers.NotificationSubject;
import org.apache.http.HttpResponse;
import utility.Request;

public class NotificationManager {
    private String userId;
    ArrayList<Notification> notificationPool = new ArrayList<>();
    NotificationSubject notify = new Notify();
    NotificationService  notificationService;
    
    public NotificationManager(String userId) {
        this.userId = userId;
        notificationService = new NotificationService(userId, this);
    }
    
    public void start() {
        notificationService.start();
    }
    
    public void register(NotificationObserver observer) {
        notify.addObserver(observer);
    }
    
    public void addNotifications(ArrayList<Notification> notifications) {
        notificationPool.addAll(notifications);
        notify.notifyObservers(notifications);
    }

    public void clearNotifications(String userId) throws ConnectivityFailureException {
        String url = CLEAR_NOTIFICATION_URL + "?userId=" + userId;
        try {
            SendPostRequest.sendPostRequest(url, "");
        } catch (IOException ex) {
            throw new ConnectivityFailureException("Unable to Connect");
        }
        
    }

}
