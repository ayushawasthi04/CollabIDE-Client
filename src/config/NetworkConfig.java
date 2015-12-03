package config;

import static config.ConfigFilePaths.NETWORK_CONF_FILE_PATH;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public abstract class NetworkConfig {
    //Default Values;
    public static String SERVER_URL = "http://127.0.0.1:3000/";
   
    //Registers the user for the first time
    public static String REGISTER_URL = SERVER_URL + "register";
    
    //Push and Get Operations
    public static String PUSH_OPERATIONS_URL = SERVER_URL + "push_operation";
    public static String GET_OPERATIONS_URL = SERVER_URL + "get_operation";
    
    //Signup and Authenticate
    public static String SIGNUP_URL = SERVER_URL + "signup";
    public static String LOGIN_URL = SERVER_URL + "login";
    public static String GET_USERINFO_URL = SERVER_URL + "get_info";
    
    //Project_View_and_Create
    public static String PROJECT_VIEW_URL = SERVER_URL + "view";
    public static String PROJECT_ADD_NODE_URL = SERVER_URL + "add_node";
    
    //Share Doc and View Users
    public static String SHARE_URL = SERVER_URL + "share";
    public static String GET_USERS_URL = SERVER_URL + "get_users";
    public static String SHARE_VIEW_URL = SERVER_URL + "get_shared_projects";
    
    //Notification Service
    public static String NOTIFICATIONS_URL = SERVER_URL + "get_notifications";
    public static String CLEAR_NOTIFICATION_URL = SERVER_URL + "clear_notifications";
    
    //Polling Thread Sleep Time
    public static int POLLING_THREAD_SLEEP_TIME = 500;
    public static int NOTIFICATIONS_THREAD_SLEEP_TIME = 60000;
    
    public static void getConfig(){
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(NETWORK_CONF_FILE_PATH));
            
            String SERVER_IP = properties.getProperty("SERVER_IP");
            String SERVER_PORT = properties.getProperty("SERVER_PORT");  
            SERVER_URL = "http://" + SERVER_IP + ":" + SERVER_PORT + "/";
            
            POLLING_THREAD_SLEEP_TIME  = Integer.parseInt(properties.getProperty("POLLING_THREAD_SLEEP_TIME"));
            NOTIFICATIONS_THREAD_SLEEP_TIME  = Integer.parseInt(properties.getProperty("NOTIFICATIONS_THREAD_SLEEP_TIME"));
            
            setAllConfig();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException. Error Reading Config File.");
            e.printStackTrace(System.err);
        } catch (IOException e) {
            System.out.println("IO Exception. Error Reading Config File.");
            e.printStackTrace(System.err);
        }
    }
    
    private static void setAllConfig() {
        PUSH_OPERATIONS_URL  = SERVER_URL + "push_operation";
        GET_OPERATIONS_URL   = SERVER_URL + "get_operation";
        REGISTER_URL         = SERVER_URL + "register";
        SIGNUP_URL           = SERVER_URL + "signup";
        LOGIN_URL            = SERVER_URL + "login";
        GET_USERINFO_URL     = SERVER_URL + "get_info";
        PROJECT_VIEW_URL     = SERVER_URL + "view";
        PROJECT_ADD_NODE_URL = SERVER_URL + "add_node";
        NOTIFICATIONS_URL    = SERVER_URL + "get_notifications";
        SHARE_VIEW_URL       = SERVER_URL + "get_shared_projects";
	CLEAR_NOTIFICATION_URL = SERVER_URL + "clear_notifications";
        GET_USERS_URL        = SERVER_URL + "get_users";
        SHARE_URL            = SERVER_URL + "share";
    }
}
