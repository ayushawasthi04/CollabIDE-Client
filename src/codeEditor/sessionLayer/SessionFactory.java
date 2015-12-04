package codeEditor.sessionLayer;

import codeEditor.buffer.SynchronizedBuffer;
import codeEditor.buffer.Buffer;
import codeEditor.dataControl.Editor;
import codeEditor.dataControl.EditorCore;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.eventNotification.NotificationService;
import codeEditor.networkLayer.NetworkHandler;
import codeEditor.networkLayer.PollService;
import codeEditor.networkLayer.PushService;
import codeEditor.transform.Transformation;

public class SessionFactory extends AbstractSessionFactory{

    @Override
    public NetworkHandler createPollingThread
        (String userIdentifier, String docIdentifier, Buffer operationBuffer, Transformation transformation) {
         return new PollService(userIdentifier, docIdentifier, operationBuffer, transformation); 
    }

    @Override
    public NetworkHandler createRequestHandlerThread(String userId, String docId, Buffer requestBuffer) {
        return  new PushService(userId, docId, requestBuffer); 
    }
    @Override
    public Editor createEditorInstance(String userId, String docId, NotificationSubject notificationService) {
        return new EditorCore(userId, docId, notificationService);
    }

    @Override
    public Buffer createBuffer() {
        return new SynchronizedBuffer();
    }

    @Override
    public Transformation createTranformation(String userId) {
        return new Transformation(userId);    
    }

    @Override
    public NotificationService createNotificationService() {
        return new NotificationService();
    }

    @Override
    public ExecuteOperationsThread createExecuteOperationThread(Editor editorCore, Buffer operationBuffer) {
        return new ExecuteOperationsThread(editorCore, operationBuffer);
    }
    
}
