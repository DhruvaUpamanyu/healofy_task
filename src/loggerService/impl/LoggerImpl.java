package loggerService.impl;

import loggerService.Logger;
import model.Logs;

import java.text.SimpleDateFormat;
import java.util.*;

public class LoggerImpl implements Logger<Logs> {

    //bucket size in milliseconds
    private static final long groupSize  = 15l*60l*1000l;

    private List<Logs> logsList;

    private HashMap<Long,HashMap<String,Integer>> buckets;

    public LoggerImpl() {
        this.logsList = new ArrayList<>();;
        this.buckets = new HashMap<>();
    }

    /**
     * Helper class to get bucket to which incoming timestamp belongs
     * @param key
     * @param first
     * @return
     */
    private static long getBucket(long key, long first){
        Double bucket =  Math.floor((key-first)/LoggerImpl.groupSize)+1d;
        return bucket.longValue();
    }

    /**
     * Get Value of timestamp of bucket
     * @param bucket
     * @param first
     * @return
     */
    private static long getBase(long bucket, long first){
        return first+ LoggerImpl.groupSize *(bucket-1);
    }
    @Override
    public void insertLog(Logs logs) {
        logsList.add(logs);
    }

    @Override
    public void printLogs() {
        if(logsList.isEmpty()){
            System.out.println("No logs found");
        }
        /**
         * To print grouped by logs:
         * Step 1: Sort the list
         * Step 2: Iterate over the sorted list and add each element to a hashmap which has time bucket as keys and
         *         map of exception name and count of exception
         * Step 3: When a bucket changes print the previous bucket
         */
        Collections.sort(logsList);
        Logs first = logsList.get(0);
        Logs last = logsList.get(logsList.size()-1);
        Long previousBucket=-1l;
        for(Logs logs:logsList){
            Long keyBucket = getBucket(logs.getRequestDate(),first.getRequestDate());
            if (previousBucket.equals(-1l)){
                previousBucket=keyBucket;
            }
            HashMap<String,Integer> errorList = buckets.get(keyBucket);
            if(errorList==null){
                if(previousBucket!=keyBucket){
                    printBucket(previousBucket,first.getRequestDate());
                    previousBucket=keyBucket;
                }
                errorList=new HashMap<>();
                errorList.put(logs.getException(),1);
                buckets.put(keyBucket,errorList);
            }
            else{
                Integer count = errorList.get(logs.getException());
                if(count!=null){
                    errorList.put(logs.getException(),count+1);
                }
                else{
                    errorList.put(logs.getException(),1);
                }
                buckets.put(keyBucket,errorList);
            }
        }
        printBucket(previousBucket,first.getRequestDate());
    }

    /**
     * Print the logs
     * @param bucket
     * @param first
     */
    private void printBucket(Long bucket,Long first){
        for(String exception:buckets.get(bucket).keySet()){
            System.out.println(getFormattedLogs(getBase(bucket,first), exception,buckets.get(bucket).get(exception)));
        }
    }

    private static String getFormattedLogs(long start, String exception, Integer count){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(start))+" "+simpleDateFormat.format(new Date(LoggerImpl.groupSize +start))
                +" "+exception+" "+count;
    }


}
