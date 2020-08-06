package cn.z201.cloud.auth.utils;

import com.google.gson.Gson;

/**
 * @author z201.coding@gmail.com
 **/
public class JsonTool {

    public static String toJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

}
