package reactive;

import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import reactive.model.Deal;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MarketDataGenerator implements Runnable {
    private static final List<String> DEAL_TYPES = ImmutableList.of("deposit", "withdrawal", "transfer");
    private static final List<String> CURRENCIES = ImmutableList.of("USD", "EUR", "GBP");
    private static final int NUMBER_OF_DEALS_ON_MARKET = 100;

    private final Jedis jedisConnection;

    public MarketDataGenerator() {
        Config config = ConfigFactory.load();
        this.jedisConnection = new Jedis(config.getString("redis.host"), config.getInt("redis.port"));
    }

    @Override
    public void run() {
        while (true) {
            Deal deal = buildRandomDeal();
            jedisConnection.set(String.valueOf(deal.id), deal.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Deal buildRandomDeal() {
        int id = rand().nextInt(NUMBER_OF_DEALS_ON_MARKET) + 1;
        String type = DEAL_TYPES.get(rand().nextInt(3));
        String amount = String.valueOf(rand().nextInt(100500));
        String currency = CURRENCIES.get(rand().nextInt(3));
        String data = String.format("{\"type\": \"%s\", \"amount\": %s, \"currency\": %s", type, amount, currency);
        return new Deal(id, data);
    }

    private Random rand() {
        return ThreadLocalRandom.current();
    }
}
