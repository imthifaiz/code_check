package com.track.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.track.db.object.DBTable;

public class ResultSetToObjectMap {
	
	public static void loadResultSetIntoObject(ResultSet rst, Object object)
	        throws IllegalArgumentException, IllegalAccessException, SQLException {
	    Class<?> zclass = object.getClass();
	    for (Field field : zclass.getDeclaredFields()) {
	        field.setAccessible(true);
	        DBTable column = field.getAnnotation(DBTable.class);
	        Object value = rst.getObject(column.columnName());
	        Class<?> type = field.getType();
	        if (isPrimitive(type)) {//check primitive type(Point 5)
	            Class<?> boxed = boxPrimitiveClass(type);//box if primitive(Point 6)
	            value = boxed.cast(value);
	        }
	        field.set(object, value);
	    }
	}

	public static boolean isPrimitive(Class<?> type) {
	    return (type == int.class || type == long.class || type == double.class || type == float.class
	            || type == boolean.class || type == byte.class || type == char.class || type == short.class);
	}

	public static Class<?> boxPrimitiveClass(Class<?> type) {
	    if (type == int.class) {
	        return Integer.class;
	    } else if (type == long.class) {
	        return Long.class;
	    } else if (type == double.class) {
	        return Double.class;
	    } else if (type == float.class) {
	        return Float.class;
	    } else if (type == boolean.class) {
	        return Boolean.class;
	    } else if (type == byte.class) {
	        return Byte.class;
	    } else if (type == char.class) {
	        return Character.class;
	    } else if (type == short.class) {
	        return Short.class;
	    } else {
	        String string = "class '" + type.getName() + "' is not a primitive";
	        throw new IllegalArgumentException(string);
	    }
	}

}
