package com.example.proyectofinalencuesta_bdubicuo.data.local.constants;

import androidx.room.TypeConverter;

import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ListadoFocalizados;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class TypeConverterCens {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromViviendaList(List<ListadoFocalizados> viviendas) {
        if (viviendas == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ListadoFocalizados>>() {}.getType();
        return gson.toJson(viviendas, type);
    }

    @TypeConverter
    public static List<ListadoFocalizados> toListadoFocalizadosList(String viviendasString) {
        if (viviendasString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ListadoFocalizados>>() {}.getType();
        return gson.fromJson(viviendasString, type);
    }
}
