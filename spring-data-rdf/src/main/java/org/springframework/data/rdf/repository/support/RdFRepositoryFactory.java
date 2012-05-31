package org.springframework.data.rdf.repository.support;

import java.io.Serializable;

import org.springframework.data.rdf.repository.utils.RdfMetaDataUtil;
import org.springframework.data.rdf.template.RdfBeansTemplate;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.mysema.rdfbean.object.SessionFactory;

public class RdFRepositoryFactory extends RepositoryFactorySupport {

    protected SessionFactory factory;

    public RdFRepositoryFactory(SessionFactory factory) {
        super();
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, ID extends Serializable> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        EntityInformation<T, ID> entityInformation = new RdfEntityInformation<T, ID>(domainClass, (Class<ID>) RdfMetaDataUtil.extractKeyType(domainClass));
        return entityInformation;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Object getTargetRepository(RepositoryMetadata metadata) {
        SimpleRdfRepository<?, ?> repository = new SimpleRdfRepository(metadata, new RdfBeansTemplate(factory));
        return repository;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return SimpleRdfRepository.class;
    }

}
