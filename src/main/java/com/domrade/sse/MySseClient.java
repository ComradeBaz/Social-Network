package com.domrade.sse;

import com.domrade.jsf.common.UserSessionObject;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.time.Duration;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.client.Client;
import javax.ws.rs.sse.InboundSseEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

@Component
@Scope("session")
public class MySseClient {

    private String broadcastUrl;// = "http://localhost:8080/sse-0.0.1-SNAPSHOT/services/api/stats/broadcast";
    private final static Logger LOGGER = Logger.getLogger(MySseClient.class);

    ClientConfig config;
    private Client client;

    @Autowired
    private ManageServerSentEvents manageServerSentEvents;

    @Autowired
    private SseClientConfig sseClientConfig;

    @Autowired
    private UserSessionObject userSessionObject;

    public MySseClient() {
        LOGGER.log(Level.INFO, "Constructing MySseClient");
    }

    public String getBroadcastUrl() {
        return broadcastUrl;
    }

    public void setBroadcastUrl(String broadcastUrl) {
        this.broadcastUrl = broadcastUrl;
    }

    @PostConstruct
    public void init() {
        setBroadcastUrl(sseClientConfig.getBroadcastUrl());
    }

    public void startSseClient() {
        setBroadcastUrl(sseClientConfig.getBroadcastUrl());
        LOGGER.log(Level.INFO, "Starting client. broadcastUrl set to "
                + this.broadcastUrl + " for user " + userSessionObject.getLoggedInUser());

        config = new ClientConfig();
        client = ClientBuilder.newClient(config);

        new Thread(new Runnable() {
            @Override
            public void run() {
                WebTarget target = client.target(broadcastUrl);
                try (final SseEventSource eventSource = SseEventSource.target(target)
                        .reconnectingEvery(5, TimeUnit.SECONDS)
                        .build()) {
                    eventSource.register(onEvent, onError, onComplete);
                    eventSource.open();
                    LOGGER.log(Level.INFO, "Wainting for incoming event for user " + userSessionObject.getLoggedInUser());

                    //Consuming events for one hour
                    Thread.sleep(60 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                client.close();
                LOGGER.log(Level.INFO, "End");
            }

        }).start();
    }

    public void stopSseClient() {
        LOGGER.log(Level.INFO, "Stopping SSE Client for user " + userSessionObject.getLoggedInUser());
        client.close();
    }

    // A new event is received
    private Consumer<InboundSseEvent> onEvent = (inboundSseEvent) -> {
        String data = inboundSseEvent.readData();
        // entityId is not included in all SSE's
        // The default value will be included in calls to manageServerSentEvents.processSse(...)
        // if there is none in the current SSE
        long entityId = -1;
        LOGGER.log(Level.DEBUG, "SSE data: " + data);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(data);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            LOGGER.log(Level.DEBUG, jsonObject.get("messageType"));
            LOGGER.log(Level.DEBUG, jsonObject.get("userId"));
            String messageType = jsonObject.get("messageType").getAsString();
            String messageCategory = jsonObject.get("messageCategory").getAsString();
            long userIdFromEvent = jsonObject.get("userId").getAsLong();
            long networkIdFromEvent = jsonObject.get("networkId").getAsLong();
            try {
                entityId = jsonObject.get("entityId").getAsLong();
            } catch (Exception e) {
                LOGGER.log(Level.WARN, e.getMessage());
            }
            // Update collections for the client if the message is relevant to their userId
            manageServerSentEvents.processSse(messageType, messageCategory, userIdFromEvent, networkIdFromEvent, entityId);
        } else {
            LOGGER.log(Level.DEBUG, "Array? " + jsonElement.isJsonArray());
            LOGGER.log(Level.DEBUG, "Primitive? " + jsonElement.isJsonPrimitive());
            LOGGER.log(Level.DEBUG, "Null? " + jsonElement.isJsonNull());
        }
    };

    //Error
    private Consumer<Throwable> onError = (throwable) -> {
        throwable.printStackTrace();
    };

    //Connection close and there is nothing to receive
    private Runnable onComplete = () -> {
        LOGGER.log(Level.INFO, "Stopping SSE Client for user " + userSessionObject.getLoggedInUser());
    };

    /*@PreDestroy
    public void beforeClose() {
    LOGGER.log(Level.INFO, "Destroying MySseClient for user " + userSessionObject.getLoggedInUser());
    stopSseClient();
    }*/
}
