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
package org.springframework.data.rdf.repository.query;

import org.springframework.data.rdf.template.RdfBeansTemplate;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.parser.PartTree;

/**
 * A {@link AbstractRdfRepositoryQuery} implementation based on a
 * {@link PartTree}.
 * 
 * @author Oliver Gierke
 */
public class PartTreeRdfRepositoryQuery extends AbstractRdfRepositoryQuery {

    private final Class<?> domainClass;
    private final PartTree tree;
    private final Parameters parameters;

    /**
     * Creates a new {@link PartTreeRdfRepositoryQuery}.
     * 
     * @param method
     * @param em
     */
    public PartTreeRdfRepositoryQuery(RdfQueryMethod method, RdfBeansTemplate template) {

        super(method, template);

        this.domainClass = method.getEntityInformation().getJavaType();
        this.tree = new PartTree(method.getName(), domainClass);
        this.parameters = method.getParameters();
    }

    /**
     * @param values
     *            the parameters supplied when invoking the query
     */
    @Override
    public Object execute(Object[] values) {
        ParametersParameterAccessor accessor = new ParametersParameterAccessor(parameters, values);
        RdfQueryCreator creator = new RdfQueryCreator(tree, accessor, domainClass);
        RDFQuery query = creator.createQuery(accessor.getSort());
        query.setTemplate(template);
        return query.execute();
    }

}
