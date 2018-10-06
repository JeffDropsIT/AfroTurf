package com.example.a21__void.afroturf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/09/27.
 * for Pandaphic
 */
public class AfroJson {
    public static final String PATH_SEPARATOR = "\\.", INDEX_SEPARATOR = ":", ROOT = "aj";
    private final JsonElement root;

    public AfroJson(String rawJson){

        JsonParser parser = new JsonParser();

        JsonElement data  = parser.parse(rawJson);
        if(data.isJsonPrimitive())
            root = data;
        else{
            root = new JsonObject();
            root.getAsJsonObject().add(ROOT, data);
        }

    }

    public AfroJson get(String path){
        String[] pathParts = path.split(PATH_SEPARATOR);
        JsonElement currJson = root;

        for(int pos = 0; pos < pathParts.length; pos++){
            String pathPart = pathParts[pos];

            if(pathPart.contains(":")){
                String[] pathSubParts = pathPart.split(INDEX_SEPARATOR);
                JsonElement next = currJson.getAsJsonObject().get(pathSubParts[0]);

                for(int i = 1; i < pathSubParts.length; i++){
                    int index = Integer.parseInt(pathSubParts[i]);
                    next  = next.getAsJsonArray().get(index);
                }
                currJson = next;
            }else{
                currJson = currJson.getAsJsonObject().get(pathPart);
            }
        }
         return new AfroJson(currJson.toString());
    }

    public JsonElement toElement(){
        return this.root;
    }

    @Override
    public String toString() {
        return this.toElement().toString();
    }

    public static void main(String[] args){
        String raw = "{\"employees\":[\n" +
                "    { \"firstName\":\"John\", \"lastName\":\"Doe\" },\n" +
                "    { \"firstName\":\"Anna\", \"lastName\":\"Smith\" },\n" +
                "    { \"firstName\":\"Peter\", \"lastName\":\"Jones\" }\n" +
                "]}";

        AfroJson afroJson = new AfroJson(raw);
        AfroJson next = afroJson.get("aj.employees");

        System.out.println(next.get("aj:0.firstName").toElement().getAsString());

    }
}