package codeEditor.sessionLayer;

import codeEditor.buffer.SynchronizedBuffer;
import codeEditor.buffer.Buffer;
import codeEditor.dataControl.DataControlLayer;
import codeEditor.dataControl.EditorCore;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.eventNotification.NotificationService;
import codeEditor.networkLayer.NetworkHandler;
import codeEditor.networkLayer.PollService;
import codeEditor.networkLayer.PushService;
import codeEditor.transform.TransformationThread;

public class SessionFactory extends AbstractSessionFactory{

    @Override
    public NetworkHandler createPollingThread(String userIdentifier, String docIdentifier, Buffer responseBuffer) {
         return new PollService(userIdentifier, docIdentifier, responseBuffer); 
    }

    @Override
    public NetworkHandler createRequestHandlerThread(String userId, String docId, Buffer requestBuffer) {
        return  new PushService(userId, docId, requestBuffer); 
    }
    @Override
    public DataControlLayer createEditorInstance(String userId, String docId, NotificationSubject notificationService) {
        return new EditorCore(userId, docId, notificationService);
    }

    @Override
    public Buffer createBuffer() {
        return new SynchronizedBuffer();
    }

    @Override
    public TransformationThread createTranformationThread(String userId, Buffer responseBuffer, Buffer operationBuffer) {
        return new TransformationThread(userId, responseBuffer, operationBuffer);    
    }

    @Override
    public NotificationService createNotificationService() {
        return new NotificationService();
    }

    @Override
    public ExecuteOperationsThread createExecuteOperationThread(DataControlLayer editorCore, Buffer operationBuffer) {
        return new ExecuteOperationsThread(editorCore, operationBuffer);
    }
    
}
