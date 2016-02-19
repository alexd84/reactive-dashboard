package reactive;

import com.google.common.eventbus.EventBus;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import reactive.network.WebSocketManager;

public class Launcher {
    public static void main(String[] args) throws Exception {
        Config config = ConfigFactory.load();
        Server server = new Server(config.getInt("web.port"));

        EventBus eventBus = new EventBus();

        new Thread(new RedisEventsListener(eventBus)).start();
        new Thread(new MarketDataGenerator()).start();

        WebAppContext webapp = new WebAppContext();
        String webPath = Thread.currentThread().getContextClassLoader().getResource("web").toExternalForm();
        webapp.setResourceBase(webPath);
        webapp.setConfigurations(new Configuration[] { new AnnotationConfiguration(), new WebXmlConfiguration() });

        ResourceHandler wshandler = new ResourceHandler();
        WebSocketManager wsm = new WebSocketManager(eventBus);
        wshandler.setHandler(wsm);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { wshandler, webapp });
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
