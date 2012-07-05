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
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.Assert;

/**
 * Abstract base class to implement {@link RepositoryQuery}s.
 * 
 * @author Oliver Gierke
 */
public abstract class AbstractRdfRepositoryQuery implements RepositoryQuery {

    protected final RdfQueryMethod method;
    protected final RdfBeansTemplate template;

    /**
     * Creates a new {@link AbstractRdfRepositoryQuery} from the given
     * {@link JpaQueryMethod}.
     * 
     * @param method
     * @param em
     */
    public AbstractRdfRepositoryQuery(RdfQueryMethod method, RdfBeansTemplate template) {

        Assert.notNull(method);
        Assert.notNull(template);

        this.method = method;
        this.template = template;
    }

    @Override
    public QueryMethod getQueryMethod() {
        return method;
    }

}