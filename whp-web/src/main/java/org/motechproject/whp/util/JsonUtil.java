package org.motechproject.whp.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private Gson gson = new Gson();

    public <T> List<T> fromJsonArray(String array, Type type){
        JsonArray jsonArray = (JsonArray) new JsonParser().parse(array);
        List<T> convertTedObjects= new ArrayList();
        for(JsonElement element : jsonArray){
            T object = gson.fromJson(element, type);
            convertTedObjects.add(object);
        }
        return convertTedObjects;
    }

    public <T> T fromJson(String element, Type type) {
        return gson.fromJson(element,type);
    }
}
