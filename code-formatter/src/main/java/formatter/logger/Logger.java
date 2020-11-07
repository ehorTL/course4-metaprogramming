package formatter.logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class Logger {

    private String logFileName = null;

    /**
     * @param logFilePath the absolute path to the error logging file
     * */
    public Logger(String logFilePath) throws IOException {
        this.logFileName = logFilePath;

        File logfile = new File(this.logFileName);
        if (!logfile.createNewFile()) {
            System.out.println("Logfile already exists.");
            throw new FileAlreadyExistsException("Logfile " + this.logFileName + " already exists");
        }
    }

    public void log(LogType logType){

    }

    public void log(LogType logType, int row, int col){

    }

    private String getFileName(){
        return this.logFileName;
    }
}
