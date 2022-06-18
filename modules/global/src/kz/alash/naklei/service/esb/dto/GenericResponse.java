package kz.alash.naklei.service.esb.dto;

import java.io.Serializable;

public class GenericResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private String stackTrace;
    private Long timeElapsedMillis = 0L;
    private T result; //результирующий объект

    public GenericResponse() {
    }

    public GenericResponse(String code, String message, String stackTrace, Long timeElapsedMillis, T result) {
        this.code = code;
        this.message = message;
        this.stackTrace = stackTrace;
        this.timeElapsedMillis = timeElapsedMillis;
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Long getTimeElapsedMillis() {
        return timeElapsedMillis;
    }

    public void setTimeElapsedMillis(Long timeElapsedMillis) {
        this.timeElapsedMillis = timeElapsedMillis;
    }
}

