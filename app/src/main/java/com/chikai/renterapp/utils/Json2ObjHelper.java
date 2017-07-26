package com.chikai.renterapp.utils;

import com.google.gson.Gson;

/**
 * 数据类字段不能重复！
 */
public class Json2ObjHelper {
    public static final Gson gson = new Gson();

    public static Gson getGson() {

        return gson;
    }

    public static <T> T newInstance(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        }catch (Exception e){
            LogUtil.info(Json2ObjHelper.class,e.getMessage());

        }
       return  null;
    }

}