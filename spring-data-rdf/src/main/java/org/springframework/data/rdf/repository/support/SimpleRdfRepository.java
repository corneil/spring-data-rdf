/**
 * 
 */
package org.springframework.data.rdf.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.rdf.repository.RdfRepository;
import org.springframework.data.rdf.repository.utils.RdfMetaDataUtil;
import org.springframework.data.rdf.template.RdfBeansTemplate;
import org.springframework.data.rdf.utils.RdfStringUtils;

/**
 * @author Corneil du Plessis
 * 
 */
public class SimpleRdfRepository<T, ID extends Serializable> implements RdfRepository<T, ID> {

    protected RdfEntityInformation<T, ID> metadata;

    public SimpleRdfRepository(RdfEntityInformation<T, ID> metadata, RdfBeansTemplate template) {
        super();
        this.metadata = metadata;
        this.template = template;
    }

    protected RdfBeansTemplate template;

    @Override
    public T save(T entity) {
        template.save(entity);
        return entity;
    }

    @Override
    public Iterable<T> save(Iterable<? extends T> entities) {

        template.addAll(entities);
        List<T> result = new ArrayList<T>();
        for (T e : entities) {
            result.add(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findOne(ID id) {
        return (T) template.find(RdfMetaDataUtil.makeId(metadata.getJavaType(), id), metadata.getJavaType());
    }

    @Override
    public boolean exists(ID id) {
        String key = id instanceof String ? (String) id : id.toString();
        return template.exists(key, metadata.getJavaType());
    }

    @Override
    public Iterable<T> findAll() {
        return (Iterable<T>) template.getAll(metadata.getJavaType());
    }

    @Override
    public long count() {
        throw new RuntimeException("Method not implemented:count");
    }

    @Override
    public void delete(ID id) {
        template.delete(template.find(RdfMetaDataUtil.makeId(metadata.getJavaType(), id), metadata.getJavaType()));
        // TODO determine best way to delete using id
        throw new RuntimeException("Implementation required:SimpleRdfRepository.delete(ID)");
    }

    @Override
    public void delete(T entity) {
        assert entity != null;
        template.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        Collection<String> keys = new HashSet<String>();
        for (T e : entities) {
            ID id = metadata.getId(e);
            assert id != null;
            String key = RdfStringUtils.stringFromObject(id);
            keys.add(key);
        }
        template.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        delete(findAll());
    }
}
