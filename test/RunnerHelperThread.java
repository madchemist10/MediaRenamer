import constants.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 */
class RunnerHelperThread implements Runnable {

    enum Wait{
        DIRECTORY_NULL,
        COPY_COMPLETE,
        NO_FILES_TO_RENAME
    }

    private final ByteArrayInputStream in;
    private final ByteArrayOutputStream out;
    boolean directoryNull = false;
    boolean copyComplete = false;
    boolean noFilesToRename = false;
    private Wait waitCondition = Wait.DIRECTORY_NULL;

    RunnerHelperThread(ByteArrayInputStream in, ByteArrayOutputStream out){
        this.in = in;
        this.out = out;
    }

    void setWaitCondition(Wait waitCondition) {
        this.waitCondition = waitCondition;
    }

    @Override
    public void run() {
        while(true){
            String rawOutput = this.out.toString();
            this.out.reset();
            String[] outputArray = rawOutput.split("\r\n");
            String output = "";
            if(outputArray.length > 0){
                output = outputArray[0];
            }

            if(Constants.DIRECTORY_NULL.equals(output) && waitCondition == Wait.DIRECTORY_NULL) {
                directoryNull = true;
                return;
            }

            if(Constants.MEDIA_COPY_COMPLETE.equals(output) && waitCondition == Wait.COPY_COMPLETE) {
                copyComplete = true;
                return;
            }

            if(Constants.NO_FILES_TO_RENAME.equals(output) && waitCondition == Wait.NO_FILES_TO_RENAME) {
                noFilesToRename = true;
                return;
            }
        }
    }
}
