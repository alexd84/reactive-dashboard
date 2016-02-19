package reactive.network;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.typesafe.config.ConfigFactory;
import org.eclipse.jetty.websocket.WebSocket;

import java.io.IOException;

public class MarketDataPublisher implements WebSocket.OnTextMessage {
    private Connection con;
    private final EventBus eventBus;

    public MarketDataPublisher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Subscribe
    public void dealNotification(String json) {
        try {
            con.sendMessage(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int arg0, String arg1) {
        eventBus.unregister(this);
    }

    @Override
    public void onMessage(String mes) {
    }

    @Override
    public void onOpen(Connection con) {
        con.setMaxIdleTime(ConfigFactory.load().getInt("socket.timeout.ms"));
        this.con = con;
        dealNotification("you just connected to realtime market data feed");
    }
}
