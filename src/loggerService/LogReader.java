package loggerService;

import java.io.FileInputStream;
import java.util.List;


public interface LogReader{
    void readAndPrintLogs(List<String> logFilesPath);
}