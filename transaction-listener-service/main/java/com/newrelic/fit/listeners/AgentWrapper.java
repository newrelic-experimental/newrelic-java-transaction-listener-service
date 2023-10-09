package com.newrelic.fit.listeners;

import java.util.Map;

public interface AgentWrapper {
    public void addAttributes(Map<String, Object> attributes);
}
