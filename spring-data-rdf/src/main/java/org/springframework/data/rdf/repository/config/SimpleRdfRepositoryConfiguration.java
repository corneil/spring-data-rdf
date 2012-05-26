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

import org.springframework.data.repository.config.AutomaticRepositoryConfigInformation;
import org.springframework.data.repository.config.ManualRepositoryConfigInformation;
import org.springframework.data.repository.config.RepositoryConfig;
import org.springframework.data.repository.config.SingleRepositoryConfigInformation;
import org.w3c.dom.Element;

/**
 * @author Oliver Gierke
 * @author Corneil du Plessis
 */
public class SimpleRdfRepositoryConfiguration extends
        RepositoryConfig<SimpleRdfRepositoryConfiguration.RdfRepositoryConfiguration, SimpleRdfRepositoryConfiguration> {

    private static final String FACTORY_CLASS = "org.springframework.data.rdf.repository.support.RdfRepositoryFactoryBean";
    private static final String MODEL_FACTORY_REF = "model-factory-ref";

    private static class AutomaticRdfRepositoryConfigInformation extends AutomaticRepositoryConfigInformation<SimpleRdfRepositoryConfiguration> implements
            RdfRepositoryConfiguration {

        public AutomaticRdfRepositoryConfigInformation(String interfaceName, SimpleRdfRepositoryConfiguration parent) {
            super(interfaceName, parent);
        }

        @Override
        public String getModelFactoryRef() {
            return getSource().getAttribute(MODEL_FACTORY_REF);
        }

    }

    private static class ManualRdfRepositoryConfigInformation extends ManualRepositoryConfigInformation<SimpleRdfRepositoryConfiguration> implements
            RdfRepositoryConfiguration {

        public ManualRdfRepositoryConfigInformation(Element element, SimpleRdfRepositoryConfiguration parent) {
            super(element, parent);
        }

        @Override
        public String getModelFactoryRef() {
            return getSource().getAttribute(MODEL_FACTORY_REF);
        }

    }

    interface RdfRepositoryConfiguration extends SingleRepositoryConfigInformation<SimpleRdfRepositoryConfiguration> {
        String getModelFactoryRef();
    }

    /**
     * @param repositoriesElement
     */
    public SimpleRdfRepositoryConfiguration(Element repositoriesElement) {
        super(repositoriesElement, FACTORY_CLASS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.jpa.repository.config.RepositoryConfigContext
     * #getManualRepositoryInformation(org.w3c.dom.Element,
     * org.springframework.data
     * .jpa.repository.config.CommonRepositoryInformation)
     */
    @Override
    public RdfRepositoryConfiguration createSingleRepositoryConfigInformationFor(Element element) {

        return new ManualRdfRepositoryConfigInformation(element, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.repository.config.GlobalRepositoryConfigInformation
     * #getAutoconfigRepositoryInformation(java.lang.String)
     */
    public RdfRepositoryConfiguration getAutoconfigRepositoryInformation(String interfaceName) {

        return new AutomaticRdfRepositoryConfigInformation(interfaceName, this);
    }

    @Override
    public String getNamedQueriesLocation() {
        return "classpath*:META-INF/rdf-named-queries.properties";
    }

    /**
     * Returns the name of the entity manager factory bean.
     * 
     * @return
     */
    public String getModelFactoryRef() {
        return getSource().getAttribute(MODEL_FACTORY_REF);
    }
}
