package com.ccbfintech.cim.redisclient.javaclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class JedisClient {

    protected static final Logger logger = LoggerFactory.getLogger(JedisClient.class);

    private static String defaultIp;
    private static int defaultPort;
    private static Set<HostAndPort> jedisClusterNodes;
    private static JedisCluster jedisCluster;

    public JedisClient(String ip, int port) throws Exception {
        defaultIp = ip;
        defaultPort = port;
        jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort(defaultIp, defaultPort));
        jedisCluster = new JedisCluster(jedisClusterNodes);
    }

    public String get(String key) {
        logger.info("Get: " + key);
        return jedisCluster.get(key);
    }

    public String set(String key, String val) {
        logger.info("Set: " + key + " -> " + val);
        return jedisCluster.set(key, val);
    }
}
