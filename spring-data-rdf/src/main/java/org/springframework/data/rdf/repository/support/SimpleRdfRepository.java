/**
 * 
 */
package org.springframework.data.rdf.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.springframework.data.rdf.repository.RdfRepository;
import org.springframework.data.rdf.repository.utils.RdfMetaDataUtil;
import org.springframework.data.rdf.template.RdfBeansTemplate;
import org.springframework.data.rdf.utils.RdfStringUtils;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * @author Corneil du Plessis
 * 
 */
public class SimpleRdfRepository<T, ID extends Serializable> implements RdfRepository<T, ID> {

    protected RepositoryMetadata metadata;

    public SimpleRdfRepository(RepositoryMetadata metadata, RdfBeansTemplate template) {
        super();
        this.metadata = metadata;
        this.template = template;
    }

    protected RdfBeansTemplate template;

    @Override
    public T save(T entity) {
        template.add(entity);
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
        return (T) template.get(RdfStringUtils.stringFromObject(id), metadata.getDomainClass());
    }

    @Override
    public boolean exists(ID id) {
        String key = id instanceof String ? (String) id : id.toString();
        return template.exists(key, metadata.getDomainClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<T> findAll() {
        List<T> result = new ArrayList<T>();
        ClosableIterator<T> iter = (ClosableIterator<T>) template.getAll(metadata.getDomainClass());
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        iter.close();
        return result;
    }

    @Override
    public long count() {
        throw new RuntimeException("Method not implemented:count");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(ID id) {
        String key = RdfStringUtils.stringFromObject(id);
        template.delete(key, (Class<T>) metadata.getDomainClass());

    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(T entity) {
        ID id = (ID) RdfMetaDataUtil.extractKey(entity);
        assert id != null;
        String key = RdfStringUtils.stringFromObject(id);
        template.delete(key, (Class<T>) metadata.getDomainClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(Iterable<? extends T> entities) {
        Collection<String> keys = new HashSet<String>();
        for (T e : entities) {
            ID id = (ID) RdfMetaDataUtil.extractKey(e);
            assert id != null;
            String key = RdfStringUtils.stringFromObject(id);
            keys.add(key);
        }
        template.deleteAll(keys, (Class<T>) metadata.getDomainClass());
    }

    @Override
    public void deleteAll() {
        delete(findAll());
    }
}
