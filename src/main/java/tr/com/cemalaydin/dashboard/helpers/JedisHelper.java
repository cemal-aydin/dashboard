package tr.com.cemalaydin.dashboard.helpers;

import tr.com.cemalaydin.dashboard.base.BaseClass;
import tr.com.cemalaydin.dashboard.config.AppConfig;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class JedisHelper extends BaseClass {

    public static final String RENEW_TOKEN_KEY = "jedisRenewJwtKey";
    public static String TOKEN_KEY = "jedisJwtKey";
    public static String FORGOT_KEY = "forgotPassword";
    private static Jedis jedis = null;
    private static AppConfig appConfig = null;
    public static void setAppConfig(AppConfig appConfig) {
        JedisHelper.appConfig = appConfig;
    }


    private static Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis(appConfig.getRedisServerHost());
            jedis.auth(appConfig.getRedisServerPassword());
        } else {
            if (!jedis.isConnected() || jedis.getClient().isBroken()) {
                jedis = new Jedis(appConfig.getRedisServerHost());
                jedis.auth(appConfig.getRedisServerPassword());

            }
        }
        return jedis;
    }

    public static void jedisSadd(String key, String value) {
        getJedis().sadd(key, value);
    }

    public static Set<String>  jedisSmembers(String key) {
        return getJedis().smembers(key);
    }

    public static boolean jedisSmembersExists(String key, String value) {
        return getJedis().sismember(key, value);
    }

    public static void jedisSrem(String key, String value) {
        getJedis().srem(key, value);
    }

    public static  void jedisHset(String hashKey,String key, String value) {
        getJedis().hset(hashKey,key,value);
    }

    public static  String jedisHget(String hashKey,String key) {
       return getJedis().hget(hashKey,key);
    }
    public static  String jedisHdel(String hashKey,String key) {
       return getJedis().hdel(hashKey,key).toString();
    }


}
