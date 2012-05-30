package org.springframework.data.rdf.repository.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;

import org.springframework.data.repository.cdi.CdiRepositoryBean;

public class RdfRepositoryBean<T> extends CdiRepositoryBean<T> {

    public RdfRepositoryBean(Set<Annotation> qualifiers, Class<T> repositoryType, BeanManager beanManager) {
        super(qualifiers, repositoryType, beanManager);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Class<? extends Annotation> getScope() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected T create(CreationalContext<T> creationalContext, Class<T> repositoryType) {
        // TODO Auto-generated method stub
        return null;
    }

}
