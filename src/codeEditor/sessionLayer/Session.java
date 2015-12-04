package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.networkLayer.NetworkHandler;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.UserOperations;
import codeEditor.transform.TransformationThread;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.NotificationObserver;
import config.Configuration;
import codeEditor.networkLayer.Request;
import codeEditor.operation.EditOperations;
import codeEditor.operation.userOperations.RepositionOperation;
import static config.NetworkConfig.PUSH_OPERATIONS;
import static config.NetworkConfig.REGISTER;
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
    private final TransformationThread transformationThread;
   
    private final NetworkHandler requestHandlerThread; 
    private final NetworkHandler pollingServiceThread;
    
    public final NotificationSubject notificationService;
    
    private final Buffer requestBuffer, responseBuffer, operationBuffer;
    
    public static volatile int lastSyncStamp;
    
    public Session(String userId, String docId) {
        Configuration.getConfiguration();
        AbstractSessionFactory sessionFactory = new SessionFactory();
        notificationService = sessionFactory.createNotificationService();
        
        editorInstance = sessionFactory.createEditorInstance(userId, docId, notificationService);
        this.userId = userId;
        this.docId = docId;
        
        requestBuffer = sessionFactory.createBuffer();
        responseBuffer = sessionFactory.createBuffer();
        operationBuffer= sessionFactory.createBuffer();
        
        requestHandlerThread = sessionFactory.createRequestHandlerThread(userId, docId, requestBuffer);
        pollingServiceThread = sessionFactory.createPollingThread(userId, docId, responseBuffer); 
        executeOperationThread = sessionFactory.createExecuteOperationThread(editorInstance, operationBuffer);
        
        transformationThread = sessionFactory.createTranformationThread(userId, responseBuffer, operationBuffer);   
    
        //Register the user on Doc
        RegisterUser r = new RegisterUser(userId, docId, executeOperationThread);
        r.registerUserOnDoc();
    }
    
    public String startSession() {
        requestHandlerThread.start();
        pollingServiceThread.start();
        executeOperationThread.start();
        transformationThread.start();
        return userId;
    }
    
    public void stopSession() {
        requestHandlerThread.interrupt();
        pollingServiceThread.interrupt();
        executeOperationThread.interrupt();
        transformationThread.interrupt();
    }
       
    public void register(NotificationObserver observer) {
        this.notificationService.addObserver(observer);
    }
    
    public void pushOperation(UserOperations userOperation){
        
        //Spin lock to let operationBuffer or responseBuffer go empty before any operation can be pushed.
        while(!responseBuffer.isEmpty() || !operationBuffer.isEmpty());
        try {
            URLBuilder urlBuilder = new URLBuilder(); 
            urlBuilder.setServerAddress(SERVER_ADDRESS).setMethod(PUSH_OPERATIONS).toString();
            urlBuilder.addParameter("userId", userId).addParameter("docId", docId);
            String pushUrl = urlBuilder.toString();
        
            if (userOperation.getType() == EditOperations.INSERT){
                
                InsertOperation insertOperation = (InsertOperation) userOperation;
                insertOperation.lastSyncStamp = Session.lastSyncStamp;
                executeOperationThread.pushOperation((Operation) userOperation);
                transformationThread.addOperation(insertOperation);
                requestBuffer.put(new Request(pushUrl, insertOperation.serialize()));
            
            } else if (userOperation.getType() == EditOperations.ERASE){
                
                EraseOperation eraseOperation = (EraseOperation) userOperation;
                eraseOperation.lastSyncStamp = Session.lastSyncStamp;
                executeOperationThread.pushOperation((Operation) userOperation);
                transformationThread.addOperation(eraseOperation);
                requestBuffer.put(new Request(pushUrl, eraseOperation.serialize()));    
            
            } else if (userOperation.getType() == EditOperations.REPOSITION) {
            
                RepositionOperation repositionOperation = (RepositionOperation) userOperation;
                requestBuffer.put(new Request(pushUrl, repositionOperation.serialize()));    
            
            } else {
            
                throw new OperationNotExistException("Operation Doesnot Exist.");
            
            }
        } catch (OperationNotExistException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}