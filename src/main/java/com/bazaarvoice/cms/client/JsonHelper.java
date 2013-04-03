package com.bazaarvoice.cms.client;

import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonHelper {
    private static final ObjectMapper JSON = new MappingJsonFactory().getCodec();

    public static String toJson(Object obj){
        try {
            return JSON.writeValueAsString(obj);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> T fromJson(String string, Class<T> type) {
        try {
            return JSON.readValue(string, type);
        } catch (IOException e) {
            throw new AssertionError(e);  // Shouldn't get IO errors reading from a string
        }
    }

    public static <T> T fromJson(File file, Class<T> type)
            throws IOException {
        return JSON.readValue(file, type);
    }
}