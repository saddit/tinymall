package jmu.shijh.tinymall.shiro;

import org.crazycake.shiro.IRedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Component
public class MyRedisManager implements IRedisManager {
    private final RedisTemplate<byte[],byte[]> redis;

    public MyRedisManager(@Qualifier("shiroRedisTemplate") RedisTemplate<byte[],byte[]> template) {
        this.redis = template;
    }

    @Override
    public byte[] get(byte[] bytes) {
        return redis.opsForValue().get(bytes);
    }

    @Override
    public byte[] set(byte[] key, byte[] val, int expireTime) {
        redis.opsForValue().set(key,val,Duration.ofSeconds(expireTime));
        return val;
    }

    @Override
    public void del(byte[] bytes) {
        redis.delete(bytes);
    }

    @Override
    public Long dbSize(byte[] bytes) {
        return redis.opsForValue().size(bytes);
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        return redis.keys(pattern);
    }
}
