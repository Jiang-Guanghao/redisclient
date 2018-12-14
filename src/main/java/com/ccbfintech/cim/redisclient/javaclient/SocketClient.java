package com.ccbfintech.cim.redisclient.javaclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {

    protected static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private static Socket socket;
    private static OutputStream write;
    private static InputStream read;
    private static String defaultIp;
    private static int defaultPort;

    public SocketClient(String ip, int port) throws Exception {
        defaultIp = ip;
        defaultPort = port;
    }

    public String set(String key, String val) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("*3").append("\r\n");//代表3个参数

        sb.append("$3").append("\r\n");//第一个参数的长度
        sb.append("SET").append("\r\n");//第一个参数的内容

        sb.append("$").append(key.getBytes().length).append("\r\n");//第二个参数长度
        sb.append(key).append("\r\n");//第二个参数内容

        sb.append("$").append(val.getBytes().length).append("\r\n");//第三个参数长度
        sb.append(val).append("\r\n");//第三个参数内容

        String requestStr = sb.toString();
        String resultsStr = sendRequest(defaultIp, defaultPort, requestStr);

        if ( resultsStr.trim().matches("(-MOVED|-ASK).*") ) {
            logger.info("First Request Result: " + resultsStr);
            //logger.info("Redirect to: " + resultsStr.split(" ")[2] );
            String newIpPort[] = resultsStr.split(" ")[2].split(":");
            resultsStr =  sendRequest(newIpPort[0].trim(), Integer.parseInt( newIpPort[1].trim()), requestStr );
        }

        logger.info("Final Request Result: " + resultsStr);
        return resultsStr;
    }

    public String get(String key) throws Exception {

        StringBuffer sb = new StringBuffer();
        sb.append("*2").append("\r\n");//代表2个参数

        sb.append("$3").append("\r\n");//第一个参数长度
        sb.append("GET").append("\r\n");//第一个参数的内容

        sb.append("$").append(key.getBytes().length).append("\r\n");//第二个参数长度
        sb.append(key).append("\r\n");//第二个参数内容

        String requestStr = sb.toString();
        String resultsStr = sendRequest(defaultIp, defaultPort, requestStr);

        if ( resultsStr.trim().matches("(-MOVED|-ASK).*") ) {
            logger.info("First Request Result: " + resultsStr);
            //logger.info("Redirect to: " + resultsStr.split(" ")[2] );
            String newIpPort[] = resultsStr.split(" ")[2].split(":");
            resultsStr = sendRequest( newIpPort[0].trim(), Integer.parseInt(newIpPort[1].trim()), requestStr );
        }

        logger.info("Final Request Result: " + resultsStr);
        return resultsStr;
    }

    public String sendRequest(String ip, int port, String content) throws Exception {
        //logger.info("Request Content: " + content);
        socket = new Socket( ip, port );
        write = socket.getOutputStream();
        read = socket.getInputStream();

        write.write( content.getBytes() );
        byte[] bytes = new byte[1024];
        read.read(bytes);
        return new String(bytes).trim();
    }
}
