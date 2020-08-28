package org.pensatocode.loadtest.core.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesHandler {

    private static final String HEADER_PREFIX = "header.";

    private static volatile PropertiesHandler instance;

    private final Map<String,String> headers;

    private PropertiesHandler() {
        Properties customHeaders = new Properties();
        Properties defaultHeaders = new Properties();
        try {
            InputStream customInputStream = getClass().getClassLoader().getResourceAsStream("custom-headers.properties");
            if (customInputStream != null) {
                defaultHeaders.load(customInputStream);
            }
            InputStream defaultInputStream = getClass().getClassLoader().getResourceAsStream("default-headers.properties");
            if (defaultInputStream != null) {
                defaultHeaders.load(defaultInputStream);
            }
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        headers = new HashMap<>();
        loadHeaders(defaultHeaders);
        loadHeaders(customHeaders);
    }

    public static PropertiesHandler get() {
        if (instance == null) {
            synchronized(PropertiesHandler.class) {
                if (instance == null) {
                    instance = new PropertiesHandler();
                }
            }
        }
        return instance;
    }

    private void loadHeaders(Properties properties) {
        for(Map.Entry<Object,Object> entry: properties.entrySet()) {
            if(entry.getKey() instanceof String && entry.getValue()!=null && ((String) entry.getKey()).startsWith(HEADER_PREFIX)) {
                String newKey = ((String) entry.getKey()).replaceFirst(HEADER_PREFIX,"");
                headers.put(newKey, entry.getValue().toString());
            }
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
