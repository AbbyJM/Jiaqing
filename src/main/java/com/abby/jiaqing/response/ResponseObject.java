package com.abby.jiaqing.response;

public class ResponseObject {
    private int status;
    private String message;

    public void setStatus(int status){
        this.status=status;
    }

    public int getStatus(){
        return status;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }
}
