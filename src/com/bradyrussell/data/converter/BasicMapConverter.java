package com.bradyrussell.data.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.persistence.AttributeConverter;
import java.util.Map;

public class BasicMapConverter implements AttributeConverter<Map<String, Object>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        return new Gson().toJson(map);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String json) {
        return new Gson().fromJson(json,new TypeToken<Map<String, Object>>(){}.getType());
    }
}