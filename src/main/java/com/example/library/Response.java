package com.example.library;

public class Response {
    private boolean success;
    private Object data;
    public Response(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }
    // Getters and setters
}

