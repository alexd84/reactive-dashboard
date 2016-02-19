package reactive.network;

import com.google.common.eventbus.EventBus;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;

import javax.servlet.http.HttpServletRequest;

public class WebSocketManager extends WebSocketHandler {
    private final EventBus eventBus;

    public WebSocketManager(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest req, String arg1) {
        MarketDataPublisher marketDataPublisher = new MarketDataPublisher(eventBus);
        eventBus.register(marketDataPublisher);
        return marketDataPublisher;
    }
}
