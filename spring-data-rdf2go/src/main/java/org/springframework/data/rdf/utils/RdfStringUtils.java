package org.springframework.data.rdf.utils;

public class RdfStringUtils {
    public static String stringFromObject(Object o) {
        if(o == null) {
            return null;
        }
        if(o instanceof String) {
            return (String) o;
        } else if(o instanceof Number) {
            return o.toString();
        } else if(o instanceof Boolean) {
            return o.toString();
        }
        // TODO implement other types
        throw new RuntimeException("Do not know how to convert object of type:" + o.getClass().getCanonicalName());
    }
}
