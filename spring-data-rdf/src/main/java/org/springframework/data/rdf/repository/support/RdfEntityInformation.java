/**
 * 
 */
package org.springframework.data.rdf.repository.support;

import java.io.Serializable;

import org.springframework.data.rdf.repository.utils.RdfMetaDataUtil;
import org.springframework.data.repository.core.EntityInformation;

/**
 * @author corneil
 * 
 */
public class RdfEntityInformation<T, ID extends Serializable> implements EntityInformation<T, ID> {
    public RdfEntityInformation(Class<T> javaType, Class<ID> idType) {
        super();
        this.javaType = javaType;
        this.idType = idType;
    }

    private Class<T> javaType;
    private Class<ID> idType;

    @Override
    public Class<T> getJavaType() {
        return javaType;
    }

    @Override
    public boolean isNew(T entity) {
        // TODO unsure is needed
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ID getId(T entity) {
        return (ID) RdfMetaDataUtil.extractKey(entity);
    }

    @Override
    public Class<ID> getIdType() {
        return idType;
    }
}
