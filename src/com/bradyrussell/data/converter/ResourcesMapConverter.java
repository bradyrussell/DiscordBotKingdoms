package com.bradyrussell.data.converter;

import com.bradyrussell.data.ResourceTypes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class ResourcesMapConverter implements AttributeConverter<Map<ResourceTypes,Long>, String> {
    @Override
    public String convertToDatabaseColumn(Map<ResourceTypes,Long> map) {
        return new Gson().toJson(map);
    }

    @Override
    public Map<ResourceTypes,Long> convertToEntityAttribute(String json) {
        return new Gson().fromJson(json,new TypeToken<Map<ResourceTypes,Long>>(){}.getType());
    }
}