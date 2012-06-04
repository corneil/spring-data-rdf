package org.springframework.data.rdf.repository.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.rdf.utils.RdfStringUtils;
import org.springframework.util.StringUtils;

import com.mysema.rdfbean.annotations.Id;
import com.mysema.rdfbean.model.ID;
import com.mysema.rdfbean.model.IDType;
import com.mysema.rdfbean.model.UID;

public class RdfMetaDataUtil {
    public static String makePrefix(Class<?> cls) {
        List<String> names = Arrays.asList(StringUtils.split(cls.getPackage().getName(), "."));
        StringBuilder result = new StringBuilder();
        result.append("http://");
        boolean useDot = false;
        for (String name : names) {
            if (useDot) {
                result.append('.');
            }
            result.append(name);
            useDot = true;
        }
        result.append('/');
        result.append(cls.getSimpleName().toLowerCase());
        result.append('#');
        return result.toString();
    }

    public static String extractPrefix(Class<?> cls) {
        for (Field f : cls.getDeclaredFields()) {
            Id subject = f.getAnnotation(Id.class);
            if (subject != null) {
                return subject.ns();
            }
        }
        for (Field f : cls.getFields()) {
            Id subject = f.getAnnotation(Id.class);
            if (subject != null) {
                return subject.ns();
            }
        }
        for (Method m : cls.getMethods()) {
            Id subject = m.getAnnotation(Id.class);
            if (subject != null) {
                return subject.ns();
            }
        }
        return null;

    }

    public static IDType extractIdType(Class<?> cls) {
        for (Field f : cls.getDeclaredFields()) {
            Id subject = f.getAnnotation(Id.class);
            if (subject != null) {
                return subject.value();
            }
        }
        for (Field f : cls.getFields()) {
            Id subject = f.getAnnotation(Id.class);
            if (subject != null) {
                return subject.value();
            }
        }
        for (Method m : cls.getMethods()) {
            Id subject = m.getAnnotation(Id.class);
            if (subject != null) {
                return subject.value();
            }
        }
        return IDType.LOCAL;

    }

    public static ID makeId(Object o) {
        Object id = extractKey(o);
        return makeId(o.getClass(), id);
    }

    public static ID makeId(Class<?> cls, Object id) {
        IDType type = extractIdType(cls);
        if (type.equals(IDType.URI)) {
            String prefix = extractPrefix(cls);
            String key = RdfStringUtils.stringFromObject(id);
            return prefix == null ? new UID(key) : new UID(prefix, key);
        }
        return (ID) id;
    }

    public static Object extractKey(Object o) {
        for (Field f : o.getClass().getDeclaredFields()) {
            Id subject = f.getAnnotation(Id.class);
            if (subject != null) {
                f.setAccessible(true);
                try {
                    return f.get(o);
                } catch (Throwable e) {
                    throw new RuntimeException("Exception extracting key field:" + e, e);
                }
            }
        }
        for (Field f : o.getClass().getFields()) {
            Id subject = f.getAnnotation(Id.class);
            if (subject != null) {
                f.setAccessible(true);
                try {
                    return f.get(o);
                } catch (Throwable e) {
                    throw new RuntimeException("Exception extracting key field:" + e, e);
                }
            }
        }
        for (Method m : o.getClass().getMethods()) {
            Id subject = m.getAnnotation(Id.class);
            if (subject != null) {
                Class<?>[] params = m.getParameterTypes();
                if (params == null || params.length == 0) {
                    try {
                        return m.invoke(o, (Object[]) null);
                    } catch (Throwable e) {
                        throw new RuntimeException("Exception retrieving id:" + e);
                    }
                } else {
                    throw new RuntimeException("Id annotation found on method with parameters:" + m.toString());
                }
            }
        }
        return null;
    }

    public static Class<?> extractKeyType(Class<?> domainClass) {
        for (Field f : domainClass.getDeclaredFields()) {
            Id subject = f.getAnnotation(Id.class);
            if (subject != null) {
                f.setAccessible(true);
                try {
                    return f.getType();
                } catch (Throwable e) {
                    throw new RuntimeException("Exception extracting key field type:" + e, e);
                }
            }
        }
        for (Field f : domainClass.getFields()) {
            Id subject = f.getAnnotation(Id.class);
            if (subject != null) {
                f.setAccessible(true);
                try {
                    return f.getType();
                } catch (Throwable e) {
                    throw new RuntimeException("Exception extracting key field type:" + e, e);
                }
            }
        }
        for (Method m : domainClass.getMethods()) {
            Id subject = m.getAnnotation(Id.class);
            if (subject != null) {
                Class<?>[] params = m.getParameterTypes();
                if (params == null || params.length == 0) {
                    return m.getReturnType();
                } else {
                    throw new RuntimeException("Id annotation found on method with parameters:" + m.toString());
                }
            }
        }
        return null;
    }
}
