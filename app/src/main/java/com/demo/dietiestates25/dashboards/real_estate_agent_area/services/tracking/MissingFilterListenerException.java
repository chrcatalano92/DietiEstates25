package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking;

public class MissingFilterListenerException extends IllegalStateException {
    public MissingFilterListenerException(String message) {
        super(message);
    }

    public MissingFilterListenerException(String message, Throwable cause) { super(message, cause); }
}
