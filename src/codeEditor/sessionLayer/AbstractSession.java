package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.Executor;
import codeEditor.eventNotification.EventSubject;
import codeEditor.transform.Transformation;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.EventObserver;
import codeEditor.networkLayer.PollService;
import codeEditor.networkLayer.PushService;
import config.Configuration;
import java.util.concurrent.locks.ReentrantLock;
import ui.AceEditor;

public abstract class AbstractSession {
    
    protected final String docId;
    protected final String userId;
    
    protected volatile int lastSynchronized;
    protected final ReentrantLock updateState = new ReentrantLock();
    
    protected final Editor model;
    
    protected final Buffer pushBuffer;
    protected final Buffer executeBuffer;
   
    protected final Executor executor;
    
    protected final Transformation transformation;
   
    protected final PushService pushService; 
    protected final PollService pollService;
    
    protected final EventSubject eventNotification;
    
    protected AceEditor ace;
    
    public AbstractSession(String userId, String docId) {
        
        Configuration.getConfiguration();
        
        this.userId = userId;
        this.docId = docId;
        
        AbstractSessionFactory sessionFactory = new SessionFactory();

        eventNotification = sessionFactory.createNotificationService();
        model = sessionFactory.createEditorInstance(userId, docId, eventNotification, this);
        pushBuffer = sessionFactory.createBuffer();
        executeBuffer = sessionFactory.createBuffer();
        transformation = sessionFactory.createTranformation(userId);
        pushService = sessionFactory.createPushService(userId, docId, pushBuffer);
        pollService = sessionFactory.createPollService()
                    .setUserId(userId)
                    .setDocId(docId)
                    .setEditor(model)
                    .setTranformation(transformation)
                    .setSession(this);
        executor = sessionFactory.createExecutor(model, executeBuffer);
    }
    
    //Starts and Stops the session
    public String startSession() {
        //Register user on doc
        new RegisterUser(userId, docId, executor).registerUserOnDoc();
  
        pushService.start();
        pollService.start();
        executor.start();
       
        return userId;
    }
    
    public void stopSession() {
        System.err.println("Stopping Push Thread");
        pushService.interrupt();
        System.err.println("Stopping Poll Thread ");
        pollService.interrupt();
        System.err.println("Stopping Executor Thread ");
        executor.interrupt();
        
        //Unregister user from doc
        new UnregisterUser(userId, docId, executor).unregisterUser();
    }
       
    //Register the user for updates from remote
    public void register(EventObserver observer) {
        this.ace = (AceEditor) observer;
        this.eventNotification.addObserver(observer);
    }
        
    
    //Get the last time session is synchronized with remote
    public synchronized int getLastSynchronized() {
        return lastSynchronized;
    }

    public synchronized void setLastSynchronized(int lastSynchronized) {
        this.lastSynchronized = lastSynchronized;
    }
    
 
    //Lock and Unlock Session
    public void lock() throws InterruptedException {
        //guarantees that the no operation is done on session untile the session is unlocked again 
        updateState.lock();
        ace.setReadOnly();
    }
    
    public void unlock() {
        ace.unsetReadOnly();
        updateState.unlock();
    }
}

