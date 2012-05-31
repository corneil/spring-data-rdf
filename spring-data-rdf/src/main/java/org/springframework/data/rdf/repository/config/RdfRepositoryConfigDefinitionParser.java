/*
 * Copyright 2008-2011 the original author or authors.
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
package org.springframework.data.rdf.repository.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.data.rdf.repository.config.SimpleRdfRepositoryConfiguration.RdfRepositoryConfiguration;
import org.springframework.data.repository.config.AbstractRepositoryConfigDefinitionParser;
import org.w3c.dom.Element;

/**
 * Parser to create bean definitions for repositories namespace. Registers bean
 * definitions for repositories as well as
 * {@code PersistenceAnnotationBeanPostProcessor} and
 * {@code PersistenceExceptionTranslationPostProcessor} to transparently inject
 * entity manager factory instance and apply exception translation.
 * <p>
 * The definition parser allows two ways of configuration. Either it looks up
 * the manually defined repository instances or scans the defined domain package
 * for candidates for repositories.
 * 
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @author Gil Markham
 */
class RdfRepositoryConfigDefinitionParser extends AbstractRepositoryConfigDefinitionParser<SimpleRdfRepositoryConfiguration, RdfRepositoryConfiguration> {

    @Override
    protected SimpleRdfRepositoryConfiguration getGlobalRepositoryConfigInformation(Element element) {

        return new SimpleRdfRepositoryConfiguration(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.repository.config.
     * AbstractRepositoryConfigDefinitionParser
     * #postProcessBeanDefinition(org.springframework
     * .data.repository.config.SingleRepositoryConfigInformation,
     * org.springframework.beans.factory.support.BeanDefinitionBuilder,
     * org.springframework.beans.factory.support.BeanDefinitionRegistry,
     * java.lang.Object)
     */
    @Override
    protected void postProcessBeanDefinition(RdfRepositoryConfiguration ctx, BeanDefinitionBuilder builder, BeanDefinitionRegistry registry, Object beanSource) {

    }

    /**
     * Registers an additional
     * {@link org.springframework.orm.rdf.support.PersistenceAnnotationBeanPostProcessor}
     * to trigger automatic injextion of {@link javax.persistence.EntityManager}
     * .
     * 
     * @param registry
     * @param source
     */
    @Override
    protected void registerBeansForRoot(BeanDefinitionRegistry registry, Object source) {

        super.registerBeansForRoot(registry, source);

    }
}
