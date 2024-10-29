package com.project.abydos.saki.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.asm.TypeReference;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class JsonReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T read(String filepath, Class<T> clazz) throws IOException {
        return MAPPER.readValue(new ClassPathResource(filepath).getFile(), clazz);
    }

    public static <T> List<T> readList(String filepath, Class<T> clazz) throws IOException {
        return MAPPER.readValue(new ClassPathResource(filepath).getFile(),
                MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    }

}
