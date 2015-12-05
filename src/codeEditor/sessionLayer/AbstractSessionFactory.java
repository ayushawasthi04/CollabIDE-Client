package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.networkLayer.NetworkHandler;
import codeEditor.transform.Transformation;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.eventNotification.NotificationService;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

public abstract class AbstractSessionFactory {
   
    public abstract Buffer createBuffer();
    
    public abstract NotificationService createNotificationService();
    
    public abstract NetworkHandler createPollingThread
        (String userId, String docId, Buffer responseBuffer, Transformation transformation, Session session);
    
    public abstract NetworkHandler createRequestHandlerThread
        (String userId, String docId, Buffer requestBuffer);
    
    public abstract Editor createEditorInstance
        (String userId, String docId, NotificationSubject notificationService);
    
    public abstract Transformation createTranformation
        (String userId);
    
    public abstract ExecuteOperationsThread createExecuteOperationThread
        (Editor editorCore, Buffer operationBuffer);
}
