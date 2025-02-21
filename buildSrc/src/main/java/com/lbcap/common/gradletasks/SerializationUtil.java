package com.lbcap.common.gradletasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;


public class SerializationUtil {

    public static <T> void instanceToJsonFile(T instanceToSave, String filePath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        FileUtil.saveFile(mapper.writeValueAsString(instanceToSave), filePath);
    }


    public static <T> T instanceFromJsonFile(String jsonFilePath, Class<T> containerClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String content = FileUtil.readFile(jsonFilePath);
        return mapper.readValue(content, containerClass);

    }

    public static <T> String instanceToJson(T instance) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return mapper.writeValueAsString(instance);
    }

    public static <T> T instanceFromJson(Class<T> containerClass, String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, containerClass);

    }
}