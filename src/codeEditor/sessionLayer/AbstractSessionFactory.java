package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.networkLayer.NetworkHandler;
import codeEditor.transform.TransformationThread;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.eventNotification.NotificationService;

public abstract class AbstractSessionFactory {
    public abstract Buffer createBuffer();
    public abstract NotificationService createNotificationService();
    public abstract NetworkHandler createPollingThread
        (String userId, String docId, Buffer responseBuffer);
    public abstract NetworkHandler createRequestHandlerThread
        (String userId, String docId, Buffer requestBuffer);
    public abstract Editor createEditorInstance
        (String userId, String docId, NotificationSubject notificationService);
    public abstract TransformationThread createTranformationThread
        (String userId, Buffer responseBuffer, Buffer operationBuffer);
    public abstract ExecuteOperationsThread createExecuteOperationThread
        (Editor editorCore, Buffer operationBuffer);
}
