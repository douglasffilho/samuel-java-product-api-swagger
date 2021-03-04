package br.com.douglasffilho.product.response;

import org.springframework.http.HttpStatus;

import java.util.List;

public abstract class Responses<T> {
    private final String reason;
    private final String message;
    private final HttpStatus status;
    private final List<T> results;

    public Responses(final String reason, final String message, final HttpStatus status, final List<T> results) {
        this.reason = reason;
        this.message = message;
        this.status = status;
        this.results = results;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<T> getResults() {
        return results;
    }
}
