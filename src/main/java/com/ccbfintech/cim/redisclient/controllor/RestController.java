package com.ccbfintech.cim.redisclient.controllor;

import com.ccbfintech.cim.redisclient.javaclient.JedisClient;
import com.ccbfintech.cim.redisclient.javaclient.SocketClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RestController {

    public RestController() throws Exception {
        socketClient = new SocketClient(redisIp, redisPort);
        jedisClient = new JedisClient(redisIp, redisPort);
    }

    static String redisIp="128.160.185.2";

    static int redisPort=7000;

    public static SocketClient socketClient;

    public static JedisClient jedisClient;


    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @RequestMapping("/raw-get")
    public String get(@RequestParam String key) throws Exception {
        return socketClient.get(key);
    }

    @RequestMapping("/raw-set")
    public String set(@RequestParam String key, @RequestParam String value) throws Exception {
        return socketClient.set(key, value);
    }

    @RequestMapping("/jedis-get")
    public String jedisget(@RequestParam String key) {
        return jedisClient.get(key);
    }

    @RequestMapping("/jedis-set")
    public String jedisset(@RequestParam String key, @RequestParam String value) {
        return jedisClient.set(key, value);
    }
}
