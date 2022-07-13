package com.zyp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ReggieProject01ApplicationTests {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("name","zyp");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println((String) name);
    }

}
