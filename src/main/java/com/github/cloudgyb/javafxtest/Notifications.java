package com.github.cloudgyb.javafxtest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * @author cloudgyb
 * @since 2025/6/21 19:11
 */
public final class Notifications {
    public final static String TEST_RESULT_EVENT = "test_result_event";
    public final static String TEST_PROGRESS_EVENT = "test_progress_event";
    private final static Notifications instance = new Notifications();
    private final ConcurrentHashMap<String, List<Subscriber>> eventSubscribersMap = new ConcurrentHashMap<>();

    private Notifications() {
    }

    public static Notifications getInstance() {
        return instance;
    }

    public void publish(String eventName, Object event) {
        List<Subscriber> subscribers = eventSubscribersMap.get(eventName);
        if (subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                subscriber.eventHandler.accept(event);
            }
        }
    }

    public void subscribe(String eventName, Subscriber subscriber) {
        List<Subscriber> subscribers = eventSubscribersMap
                .computeIfAbsent(eventName, k -> new CopyOnWriteArrayList<>());
        subscribers.add(subscriber);
    }

    public void unsubscribe(String eventName, Subscriber subscriber) {
        List<Subscriber> subscribers = eventSubscribersMap.get(eventName);
        if (subscribers != null) {
            subscribers.remove(subscriber);
        }
    }

    public record Subscriber(Object subscriberObj, Consumer<Object> eventHandler) {
    }

}
