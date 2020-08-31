package org.pensatocode.loadtest.core.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesHandler {

    private static final String HEADER_PREFIX = "header.";
    private static final String INIT_PREFIX = "load-test.initializer.";
    private static final String PATH_PARAM_PREFIX = "request.path-param.";
    private static final String BODY_PREFIX = "request.body.";

    private static volatile PropertiesHandler instance;

    private final Map<String,String> headers;
    private final Map<String,String> initializers;
    private final Map<String,List<String>> pathParams;
    private final Map<String,List<String>> requestBodies;

    private PropertiesHandler() {
        Properties defaultProperties = loadProperties("default-headers.properties");
        Properties customProperties = loadProperties("custom-headers.properties");
        Properties initialProperties = loadProperties("application.properties");
        headers = loadMap(HEADER_PREFIX,defaultProperties);
        headers.putAll(loadMap(HEADER_PREFIX,customProperties));
        initializers = loadMap(INIT_PREFIX,initialProperties);
        pathParams = loadMultiMapPathParams(initialProperties);
        requestBodies = loadMultiMapRequestBodies(initialProperties);
    }

    public static PropertiesHandler getInstance() {
        if (instance == null) {
            synchronized(PropertiesHandler.class) {
                if (instance == null) {
                    instance = new PropertiesHandler();
                }
            }
        }
        return instance;
    }

    private Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return properties;
    }

    private Map<String,String> loadMap(String prefix, Properties properties) {
        Map<String,String> map = new HashMap<>();
        for(Map.Entry<Object,Object> entry: properties.entrySet()) {
            if(entry.getKey() instanceof String && entry.getValue()!=null && ((String) entry.getKey()).startsWith(prefix)) {
                String newKey = ((String) entry.getKey()).replaceFirst(prefix,"");
                map.put(newKey, entry.getValue().toString());
            }
        }
        return map;
    }

    private Map<String,List<String>> loadMultiMapPathParams(Properties properties) {
        Map<String,List<String>> multiMap = new HashMap<>();
        for(Map.Entry<Object,Object> entry: properties.entrySet()) {
            if(entry.getKey() instanceof String && entry.getValue()!=null && ((String) entry.getKey()).startsWith(PATH_PARAM_PREFIX)) {
                String newKey = ((String) entry.getKey()).replaceFirst(PATH_PARAM_PREFIX,"");
                List<String> values = new ArrayList<>(Arrays.asList(entry.getValue().toString().split(",")));
                multiMap.put(newKey, values);
            }
        }
        return multiMap;
    }

    private Map<String,List<String>> loadMultiMapRequestBodies(Properties properties) {
        Map<String,List<String>> multiMap = new HashMap<>();
        for(Map.Entry<Object,Object> entry: properties.entrySet()) {
            if(entry.getKey() instanceof String && entry.getValue()!=null && ((String) entry.getKey()).startsWith(BODY_PREFIX)) {
                String composedKey = ((String) entry.getKey()).replaceFirst(BODY_PREFIX,"");
                int endIndex = composedKey.indexOf('.');
                String key = composedKey.substring(0,endIndex);
                List<String> list = multiMap.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(entry.getValue().toString());
                multiMap.put(key, list);
            }
        }
        return multiMap;
    }

    public Map<String,String> getHeaders() {
        return headers;
    }

    public String getInitialParam(String key) {
        return initializers.get(key);
    }

    public int getInitialParamAsInt(String key) {
        return Integer.parseInt(initializers.get(key));
    }

    public boolean getInitialParamAsBoolean(String key) {
        return Boolean.parseBoolean(initializers.get(key));
    }

    public List<String> getPathParamsFor(String label) {
        return pathParams.get(label);
    }

    public List<String> getRequestBodyListFor(String label) {
        return requestBodies.get(label);
    }
}
