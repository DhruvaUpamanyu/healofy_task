package model;

import java.util.Date;

public class Logs implements Comparable<Logs>{
    private long requestId;
    private long requestDate;
    private String exception;

    public Logs(long requestId, long requestDate, String exception) {
        this.requestId = requestId;
        this.requestDate = requestDate;
        this.exception = exception;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(long requestDate) {
        this.requestDate = requestDate;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "Logs{" +
                "requestId=" + requestId +
                ", requestDate=" + requestDate +
                ", exception='" + exception + '\'' +
                '}';
    }

    @Override
    public int compareTo(Logs o) {
        return this.requestDate-o.getRequestDate()>0?1:-1;
    }
}
