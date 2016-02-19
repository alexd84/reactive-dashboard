package reactive;

import com.google.common.eventbus.EventBus;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisEventsListener implements Runnable {
    private final EventBus eventBus;
    private final Jedis jedisListener;
    private final Jedis jedisConnection;

    @Override
    public void run() {
        jedisListener.psubscribe(new RedisSubscriber(), "__key*__:*");
    }

    private class RedisSubscriber extends JedisPubSub {
        @Override
        public void onPMessage(String pattern, String channel, String key) {
            String newDealValue = jedisConnection.get(key);
            String dealUpdateNotificationMessage = String.format("%s: %s", "new deal", newDealValue);

            eventBus.post(dealUpdateNotificationMessage);
        }
    }

    public RedisEventsListener(EventBus eventBus) {
        Config config = ConfigFactory.load();
        this.eventBus = eventBus;
        this.jedisListener = new Jedis(config.getString("redis.host"), config.getInt("redis.port"));
        this.jedisConnection = new Jedis(config.getString("redis.host"), config.getInt("redis.port"));
    }
}
