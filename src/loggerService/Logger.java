package loggerService;

public interface Logger<T> {
    void insertLog(T log);
    void printLogs();
}
