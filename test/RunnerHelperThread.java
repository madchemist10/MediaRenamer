import constants.Constants;
import errorHandle.ErrorHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 */
public class RunnerHelperThread implements Runnable {
    private final ByteArrayInputStream in;
    private final ByteArrayOutputStream out;
    public boolean directoryNull = false;

    public RunnerHelperThread(ByteArrayInputStream in, ByteArrayOutputStream out){
        this.in = in;
        this.out = out;
    }
    @Override
    public void run() {
        boolean continueThread = true;
        while(continueThread){
            String rawOutput = this.out.toString();
            this.out.reset();
            String output = rawOutput.split("\r\n")[0];
            switch(output){
                case Constants.DIRECTORY_NULL:
                    continueThread = false;
                    directoryNull = true;
                    break;
            }
        }
    }
}
