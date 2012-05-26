/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.rdf.repository.support;

import java.io.Serializable;

import org.ontoware.rdf2go.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Special adapter for Springs
 * {@link org.springframework.beans.factory.FactoryBean} interface to allow easy
 * setup of repository factories via Spring configuration.
 * 
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @author Corneil du Plessis
 * @param <T> the type of the repository
 */
public class RdfRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends RepositoryFactoryBeanSupport<T, S, ID> {

    @Autowired
    protected ModelFactory modelFactory;


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new RdFRepositoryFactory(modelFactory);
    }

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    public void setModelFactory(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }
}
