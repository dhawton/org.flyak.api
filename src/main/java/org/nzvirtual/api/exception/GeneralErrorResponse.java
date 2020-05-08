package org.nzvirtual.api.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GeneralErrorResponse
{
    private String details;
    private String message;

    public GeneralErrorResponse(String message, String details) {
        super();
        this.message = message;
        this.details = details;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
