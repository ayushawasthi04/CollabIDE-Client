package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.networkLayer.NetworkHandler;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.UserOperations;
import codeEditor.transform.Transformation;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.NotificationObserver;
import config.Configuration;
import codeEditor.networkLayer.Request;
import codeEditor.operation.EditOperations;
import codeEditor.operation.userOperations.RepositionOperation;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import static config.NetworkConfig.PUSH_OPERATIONS;
import static config.NetworkConfig.SERVER_ADDRESS;
import exception.OperationNotExistException;
import java.util.logging.Level;
import java.util.logging.Logger;
import urlbuilder.URLBuilder;

public class Session {
    
    private final String docId;
    private final String userId;
    
    public final Editor editorInstance;
    
    private final ExecuteOperationsThread executeOperationThread;
    private final Transformation transformation;
   
    private final NetworkHandler requestHandlerThread; 
    private final NetworkHandler pollingServiceThread;
    
    public final NotificationSubject notificationService;
    
    private final Buffer requestBuffer, operationBuffer;
    
    public static volatile int lastSynchronized;
    
    public static Mutex updateState = new Mutex();
    
    public Session(String userId, String docId) {
        Configuration.getConfiguration();
        AbstractSessionFactory sessionFactory = new SessionFactory();
        
        notificationService = sessionFactory.createNotificationService();
        
        editorInstance = sessionFactory.createEditorInstance(userId, docId, notificationService);
        this.userId = userId;
        this.docId = docId;
        
        requestBuffer = sessionFactory.createBuffer();
        operationBuffer= sessionFactory.createBuffer();
        
        transformation = sessionFactory.createTranformation(userId);   
    
        requestHandlerThread = sessionFactory.createRequestHandlerThread(userId, docId, requestBuffer);
        pollingServiceThread = sessionFactory.createPollingThread(userId, docId, operationBuffer, transformation); 
        executeOperationThread = sessionFactory.createExecuteOperationThread(editorInstance, operationBuffer);
       
        //Register the user on Doc
        RegisterUser r = new RegisterUser(userId, docId, executeOperationThread);
        r.registerUserOnDoc();
    }
    
    public String startSession() {
        requestHandlerThread.start();
        pollingServiceThread.start();
        executeOperationThread.start();
        return userId;
    }
    
    public void stopSession() {
        requestHandlerThread.interrupt();
        pollingServiceThread.interrupt();
        executeOperationThread.interrupt();
    }
       
    public void register(NotificationObserver observer) {
        this.notificationService.addObserver(observer);
    }
    
    @SuppressWarnings("empty-statement")
    public void pushOperation(UserOperations userOperation){
        
        try {
            try {
                updateState.acquire();
                //Spin lock to let operationBuffer go empty before any operation can be pushed.
                while(!operationBuffer.isEmpty());
        
                URLBuilder urlBuilder = new URLBuilder(); 
                urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(PUSH_OPERATIONS).toString();
                urlBuilder.addParameter("userId", userId).addParameter("docId", docId);
                String pushUrl = urlBuilder.toString();

                if (userOperation.getType() == EditOperations.INSERT){

                    InsertOperation insertOperation = (InsertOperation) userOperation;
                    insertOperation.lastSyncStamp = Session.lastSynchronized;
                    executeOperationThread.pushOperation((Operation) userOperation);
                    transformation.addOperation(insertOperation);
                    requestBuffer.put(new Request(pushUrl, insertOperation.serialize()));

                } else if (userOperation.getType() == EditOperations.ERASE){

                    EraseOperation eraseOperation = (EraseOperation) userOperation;
                    eraseOperation.lastSyncStamp = Session.lastSynchronized;
                    executeOperationThread.pushOperation((Operation) userOperation);
                    transformation.addOperation(eraseOperation);
                    requestBuffer.put(new Request(pushUrl, eraseOperation.serialize()));    

                } else if (userOperation.getType() == EditOperations.REPOSITION) {

                    RepositionOperation repositionOperation = (RepositionOperation) userOperation;
                    requestBuffer.put(new Request(pushUrl, repositionOperation.serialize()));    

                } else {
                    throw new OperationNotExistException("Operation Doesnot Exist.");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                updateState.release();
            }
        } catch (OperationNotExistException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}