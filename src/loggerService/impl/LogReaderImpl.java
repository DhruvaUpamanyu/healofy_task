package loggerService.impl;

import loggerService.LogReader;
import loggerService.Logger;
import loggerService.impl.LoggerImpl;
import model.Logs;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class LogReaderImpl implements LogReader {

    //threads in thread pool executor service
    private static final int threads = 5;

    private ExecutorService executor;

    private Logger<Logs> logger;


    public LogReaderImpl() {
        logger = new LoggerImpl();
        executor=Executors.newFixedThreadPool(threads);
    }


    private List<Logs> getLogsFromStream(String logStreamPath){
        List<Logs> logsList = new ArrayList<>();
        try{
            FileInputStream fileInputStream = new FileInputStream(logStreamPath);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream));
            String strLine;
            //not skipping first line, assuming column names are not present
            while ((strLine = br.readLine()) != null)   {
                String[] tokens = strLine.split(" ");
                Logs record = new Logs(Long.valueOf(tokens[0]),Long.valueOf(tokens[1]),tokens[2]);//process record , etc
                /**
                 * As array lists are not thread safe
                 * insert must happen only one thread at a time
                 */
                synchronized (logger) {
                    logger.insertLog(record);
                }
            }
            fileInputStream.close();
            dataInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return logsList;
    }

    @Override
    public void readLogFile(List<String> logFiles) {
        try {
            executor.invokeAll(createTasks(logFiles));
            logger.printLogs();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Callable<List<Logs>>> createTasks(List<String> logs){
        List<Callable<List<Logs>>> callables = new ArrayList<Callable<List<Logs>>>();
        logs.forEach( log -> {
            Callable<List<Logs>> task = () -> {
                return getLogsFromStream(log);
            };
            callables.add(task);
        });
        return callables;
    }
}
