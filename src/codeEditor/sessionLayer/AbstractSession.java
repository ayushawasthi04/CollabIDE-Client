package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.networkLayer.NetworkHandler;
import codeEditor.transform.Transformation;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.NotificationObserver;
import codeEditor.operation.userOperations.UserOperations;
import config.Configuration;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractSession {
    
    protected final String docId;
    protected final String userId;
    
    protected volatile int lastSynchronized;
    protected final ReentrantLock updateState = new ReentrantLock();
    
    protected final Editor editorInstance;
    
    protected final Buffer requestBuffer;
    protected final Buffer operationBuffer;
   
    protected final ExecuteOperationsThread executeOperationThread;
    
    protected final Transformation transformation;
   
    protected final NetworkHandler requestHandlerThread; 
    protected final NetworkHandler pollingServiceThread;
    
    protected final NotificationSubject notificationService;
    
    
    public AbstractSession(String userId, String docId) {
        
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
        pollingServiceThread = sessionFactory.createPollingThread(userId, docId, operationBuffer, transformation, this); 
        executeOperationThread = sessionFactory.createExecuteOperationThread(editorInstance, operationBuffer);
       
    }
    
    //Starts and Stops the session
    public String startSession() {
        requestHandlerThread.start();
        pollingServiceThread.start();
        executeOperationThread.start();
        
        //Register the user on Doc
        new RegisterUser(userId, docId, executeOperationThread).registerUserOnDoc();
  
        return userId;
    }
    
    public void stopSession() {
        requestHandlerThread.interrupt();
        pollingServiceThread.interrupt();
        executeOperationThread.interrupt();
    }
       
    //Register the user for updates from remote
    public void register(NotificationObserver observer) {
        this.notificationService.addObserver(observer);
    }
        
    
    //Get the last time session is synchronized with remote
    public int getLastSynchronized() {
        return lastSynchronized;
    }

    public void setLastSynchronized(int lastSynchronized) {
        this.lastSynchronized = lastSynchronized;
    }
    
    
    //Lock and Unlock Session
    public void lock() throws InterruptedException {
        //flushes the operation buffer
        while (!operationBuffer.isEmpty());
        
        //guarantees that the no operation is done on session untile the session is unlocked again 
        updateState.lock();
    }
    
    public void unlock() {
        updateState.unlock();
    }
}

