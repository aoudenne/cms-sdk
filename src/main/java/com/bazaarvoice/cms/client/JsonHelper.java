package com.bazaarvoice.cms.client;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonHelper {
    private static final ObjectMapper JSON = new MappingJsonFactory().getCodec();

    public static String toJson(Object obj)
            throws IOException, JsonParseException, JsonMappingException {
        return JSON.writeValueAsString(obj);
    }

    public static <T> T fromJson(String string, Class<T> type)
            throws IOException, JsonParseException, JsonMappingException {
        return JSON.readValue(string, type);
    }

    public static <T> T fromJson(File file, Class<T> type)
            throws IOException {
        return JSON.readValue(file, type);
    }
}