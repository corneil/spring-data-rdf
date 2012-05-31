package org.springframework.data.rdf.repository.support;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rdf.repository.query.RdfQueryLookupStrategy;
import org.springframework.data.rdf.repository.utils.RdfMetaDataUtil;
import org.springframework.data.rdf.template.RdfBeansTemplate;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Object getTargetRepository(RepositoryMetadata metadata) {
        Class<?> domainClass = metadata.getDomainClass();
        RdfBeansTemplate template = new RdfBeansTemplate(factory);
        RdfEntityInformation entityInformation = new RdfEntityInformation(domainClass, RdfMetaDataUtil.extractKeyType(domainClass));
        if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
            return new QueryDslRdfRepository(entityInformation, template);
        } else {
            return new SimpleRdfRepository(entityInformation, template);
        }
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
            return QueryDslRdfRepository.class;
        } else {
            return SimpleRdfRepository.class;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.repository.support.RepositoryFactorySupport#
     * getQueryLookupStrategy
     * (org.springframework.data.repository.query.QueryLookupStrategy.Key)
     */
    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(Key key) {
        RdfBeansTemplate template = new RdfBeansTemplate(factory);
        return RdfQueryLookupStrategy.create(template, key);
    }
    private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
        return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }
}
