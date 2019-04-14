package loggerService;

import java.io.FileInputStream;
import java.util.List;


public interface LogReader{
    void readLogFile(List<String> logFilesPath);
}