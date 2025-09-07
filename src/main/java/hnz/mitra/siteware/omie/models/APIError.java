package hnz.mitra.siteware.omie.models;

import okhttp3.Response;

import java.io.IOException;
import java.util.Date;

public class APIError {

    private Date occurrenceDate;
    private String id;
    private String request;
    private Integer errorCode;
    private String errorMessage;
    private String errorResponseBody;


    public APIError(String id, String request, Integer errorCode, String errorMessage, String errorResponseBody) {
        this.occurrenceDate = new Date();
        this.id = id;
        this.request = request;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorResponseBody = errorResponseBody;
    }

    public String getRequest() {
        return request;
    }
    public Date getOccurrenceDate() {
        return occurrenceDate;
    }

    public String getId() {
        return id;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorResponseBody() {
        return errorResponseBody;
    }

    @Override
    public String toString() {
        return "APIError{" +
                "occurrenceDate=" + occurrenceDate +
                ", id='" + id + '\'' +
                ", request='" + request + '\'' +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorResponseBody='" + errorResponseBody + '\'' +
                '}';
    }
}
