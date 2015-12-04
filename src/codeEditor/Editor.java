package codeEditor;

import exception.ConnectivityFailureException;
import java.util.ArrayList;
import notificationService.Notification;
import notificationService.NotificationManager;
import notificationService.notifyObservers.NotificationObserver;
import org.json.JSONException;
import projectManager.Collections;
import share.Share;

public class Editor {
    public static void main(String[] args) throws JSONException, ConnectivityFailureException {
        //ArrayList<Collections> collectionList = Share.getSharedProjects("abcdef");
        
        /*Execute e = new Execute();
        e.execute();*/
        
        /*ArrayList<User> userList = Share.getUsers();
        for (User user: userList) {
            System.out.println(user.getName());
            System.out.println(user.getEmailId());
        }
        
        ArrayList<String> shareList = new ArrayList<>();
        shareList.add("abcd");
        shareList.add("efgh");
        Share.shareWith(RandomGen.getRandom(), shareList);*/
        /*String JSON = "{\"last_sync\" : 5, \"operations\": [{\"type\": \"INSERT\"}]}";
        JSONObject jsonObject = new JSONObject(JSON);
        System.out.println(jsonObject.get("last_sync"));
        System.out.println(jsonObject.get("operations"));*/
        
        
        /*Collections collection = CollectionFactory.getCollection("swapnil");
        ArrayList<Node> nodeList = collection.getContent();
        for (Node node : nodeList) {
            System.out.println("name:" + node.getName());
            System.out.println("path:" + node.getPath());
            if (node.getType() == Type.COLLECTION) {
                Collections subCollection = (Collections)node;
                subCollection.getContent();
            } else if (node.getType() == Type.DOC) {
                Doc doc = (Doc) node;
                System.out.println(doc.getIdentifier());
            }
        }
        collection.createNode("Collection4", Type.COLLECTION);
        */
        
        /*User user = new User();
        user.setName("swapnil");
        user.setUsername("dragonslayerx");
        user.setPassword("root");
        user.setEmailId("swapnilsaxena@live.in");
        user.setUserIdentifier(RandomGen.getRandom());
        
        Status status;
        try {
            status = Signup.doSignUp(user);
            System.out.println(status.statusMessage);
        } catch (ConnectivityFaliureException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            user = Login.doLogin("dragonslayerx", "root");
        } catch (IncorrectPasswordException ex) {
            System.err.println(ex.getMessage());
        } catch (ConnectivityFaliureException ex) {
            System.err.println(ex.getMessage());    
        }
        
        System.out.println(user.getName());
        System.out.println(user.getEmailId());
        System.out.println(user.getUserIdentifier());
         
        //Session session = new Session(RandomGen.getRandom(), RandomGen.getRandom());
        */
     }  
}

class Execute implements NotificationObserver{
    void execute() {
        /*Session session = new Session("dragonlslayerx", "abc");
        session.startSession();
        session.notificationService.addObserver(this);
        RepositionOperation reposition = new RepositionOperation(RandomGen.getRandom(), "dragonslayerx", 5);
        session.pushOperation(reposition);*/
        
        NotificationManager manager = new NotificationManager("abcde");
        manager.start();
        manager.register(this);
    }
    
    @Override
    public void notifyObserver(ArrayList<Notification> notificationList) {
        for (Notification notification: notificationList) {
            System.out.println(notification.getNotificationMessage());
            System.out.println(notification.getProject().getName());
            System.out.println(notification.getProject().getPath());
            System.out.println(notification.getProject().getType());
        }
    }
}