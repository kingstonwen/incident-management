package com.kingstonwen.incident_management;

public enum IncidentPriority {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int priority;

    IncidentPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return String.valueOf(priority);
    }
}
