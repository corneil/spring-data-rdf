package org.springframework.data.rdf.repository.utils;

import java.lang.reflect.Field;

import com.viceversatech.rdfbeans.annotations.RDFSubject;

public class RdfMetaDataUtil {
    public static Object extractKey(Object o) {
        for (Field f : o.getClass().getFields()) {
            RDFSubject subject = f.getAnnotation(RDFSubject.class);
            if (subject != null) {
                f.setAccessible(true);
                try {
                    return f.get(o);
                } catch (Throwable e) {
                    throw new RuntimeException("Exception extracting key field:" + e, e);
                }
            }
        }
        return null;
    }
    public static Class<?> extractKeyType(Class<?> domainClass) {
        for (Field f : domainClass.getFields()) {
            RDFSubject subject = f.getAnnotation(RDFSubject.class);
            if (subject != null) {
                f.setAccessible(true);
                try {                    
                    return f.getType();
                } catch (Throwable e) {
                    throw new RuntimeException("Exception extracting key field type:" + e, e);
                }
            }
        }
        return null;
    }
}
