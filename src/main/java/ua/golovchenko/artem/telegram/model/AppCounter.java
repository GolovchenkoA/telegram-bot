package ua.golovchenko.artem.telegram.model;

public enum AppCounter {
    MESSAGE_COUNTS("message.counts");

    private String name;

    AppCounter(String name) {
        this.name = name;
    }
}