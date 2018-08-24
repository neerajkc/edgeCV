package application;

import facerecog.FrameProcess;
import sapphire.app.AppEntryPoint;
import sapphire.app.AppObjectNotCreatedException;
import static sapphire.runtime.Sapphire.*;
import sapphire.common.AppObjectStub;

public class FaceRecognitionStart implements AppEntryPoint {

    @Override
    public AppObjectStub start() throws AppObjectNotCreatedException {
            return (AppObjectStub) new_(FrameProcess.class);
    }
}
