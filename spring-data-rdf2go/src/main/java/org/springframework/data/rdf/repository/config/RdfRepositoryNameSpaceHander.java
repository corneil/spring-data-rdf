/**
 * 
 */
package org.springframework.data.rdf.repository.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author corneil
 * 
 */
public class RdfRepositoryNameSpaceHander extends NamespaceHandlerSupport {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {
        registerBeanDefinitionParser("repositories", new RdfRepositoryConfigDefinitionParser());
    }

}
