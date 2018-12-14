package com.ccbfintech.cim.redisclient.controllor;

import com.ccbfintech.cim.redisclient.model.Cstinfo;
import com.ccbfintech.cim.redisclient.repository.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class WebController {

    protected static final Logger logger = LoggerFactory.getLogger(WebController.class);

    private Socket socket;
    public WebController() {
        try {
            socket = new Socket( "128.160.185.2", 7006 );
        } catch ( Exception e ) {
            logger.error("Initialize Socket Failed!" + e.fillInStackTrace() );
        }
    }

    @Autowired
    private RedisRepository redisRepository;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/get")
    public @ResponseBody
    Cstinfo get(@RequestParam String key) {
        return redisRepository.findById(key).get();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> add(@RequestParam String key, @RequestParam String value) {

        Cstinfo cstinfo = new Cstinfo(key, value);

        redisRepository.save(cstinfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> del(@RequestParam String key) {
        redisRepository.deleteById(key);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/all")
    public @ResponseBody List<Cstinfo> all() {
        List<Cstinfo> cstinfos = new ArrayList<>();
        redisRepository.findAll().forEach(cstinfos::add);
        return cstinfos;
    }

    @RequestMapping("/values")
    public @ResponseBody
    Map<String, String> findAll() {
        List<Cstinfo> cstinfos = new ArrayList<>();
        redisRepository.findAll().forEach(cstinfos::add);
        Map<String, String> map = new HashMap<String, String>();
        for( Cstinfo cstinfo : cstinfos ){
            map.put( cstinfo.getId()+" -> "+cstinfo.getInfo(), getPosition(cstinfo.getId()) );
        }
        return map;
    }

    public String getPosition(String key) {

        try {
            if( socket == null ) {
                socket = new Socket( "128.160.185.2", 7006 );
            }

            OutputStream write = socket.getOutputStream();
            InputStream read = socket.getInputStream();

            String request = "*2\r\n$3\r\nGET\r\n$" + key.getBytes().length + "\r\n" + key + "\r\n";
            write.write( request.getBytes() );
            byte[] bytes = new byte[1024];
            read.read(bytes);
            String result = new String(bytes).trim();
            String shard = result.split(" ")[1];
            String host = result.split(" ")[2];
            return host + " / " + shard;
        } catch (IOException e) {
            logger.error( "Socket Request Failed! " + e.fillInStackTrace() );
        }
        return "";
    }
}
