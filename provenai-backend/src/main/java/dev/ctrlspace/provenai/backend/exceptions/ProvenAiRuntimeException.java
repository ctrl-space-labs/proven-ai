package dev.ctrlspace.provenai.backend.exceptions;

import org.springframework.http.HttpStatus;

public class ProvenAiRuntimeException extends RuntimeException {

    private HttpStatus status;
    private String messageCode;
    private String message;

    public ProvenAiRuntimeException(HttpStatus status, String messageCode, String message) {
        this.status = status;
        this.messageCode = messageCode;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
