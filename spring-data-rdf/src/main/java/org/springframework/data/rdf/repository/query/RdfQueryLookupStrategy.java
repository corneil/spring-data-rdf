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
package org.springframework.data.rdf.repository.query;

import java.lang.reflect.Method;

import org.springframework.data.rdf.repository.Sparql;
import org.springframework.data.rdf.template.RdfBeansTemplate;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.RepositoryQuery;

/**
 * Sparql lookup strategy to execute finders.
 * 
 * @author Oliver Gierke
 */
public final class RdfQueryLookupStrategy {

    /**
     * Private constructor to prevent instantiation.
     */
    private RdfQueryLookupStrategy() {

    }

    /**
     * Base class for {@link QueryLookupStrategy} implementations that need
     * access to an {@link EntityManager}.
     * 
     * @author Oliver Gierke
     */
    private abstract static class AbstractQueryLookupStrategy implements QueryLookupStrategy {

        private final RdfBeansTemplate template;

        public AbstractQueryLookupStrategy(RdfBeansTemplate template) {

            this.template = template;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.springframework.data.repository.query.QueryLookupStrategy#
         * resolveQuery(java.lang.reflect.Method,
         * org.springframework.data.repository.core.RepositoryMetadata,
         * org.springframework.data.repository.core.NamedQueries)
         */
        public final RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, NamedQueries namedQueries) {

            return resolveQuery(new RdfQueryMethod(method, metadata), template, namedQueries);
        }

        protected abstract RepositoryQuery resolveQuery(RdfQueryMethod method, RdfBeansTemplate template, NamedQueries namedQueries);
    }

    /**
     * {@link QueryLookupStrategy} to create a query from the method name.
     * 
     * @author Oliver Gierke
     */
    private static class CreateQueryLookupStrategy extends AbstractQueryLookupStrategy {

        public CreateQueryLookupStrategy(RdfBeansTemplate template) {

            super(template);
        }

        @Override
        protected RepositoryQuery resolveQuery(RdfQueryMethod method, RdfBeansTemplate template, NamedQueries namedQueries) {
            try {
                return new PartTreeRdfRepositoryQuery(method, template);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("Could not create query metamodel for method %s!", method.toString()), e);
            }
        }
    }

    /**
     * {@link QueryLookupStrategy} that tries to detect a declared query
     * declared via {@link Sparql} annotation followed by a JPA named query
     * lookup.
     * 
     * @author Oliver Gierke
     */
    private static class DeclaredQueryLookupStrategy extends AbstractQueryLookupStrategy {

        public DeclaredQueryLookupStrategy(RdfBeansTemplate template) {

            super(template);
        }

        @Override
        protected RepositoryQuery resolveQuery(RdfQueryMethod method, RdfBeansTemplate template, NamedQueries namedQueries) {

            // TODO named or sparql

            throw new IllegalStateException(String.format("Did neither find a NamedQuery nor an annotated query for method %s!", method));
        }

    }

    /**
     * {@link QueryLookupStrategy} to try to detect a declared query first (
     * {@link org.springframework.data.Sparql.repository.Query}, JPA named
     * query). In case none is found we fall back on query creation.
     * 
     * @author Oliver Gierke
     */
    private static class CreateIfNotFoundQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final DeclaredQueryLookupStrategy strategy;
        private final CreateQueryLookupStrategy createStrategy;

        public CreateIfNotFoundQueryLookupStrategy(RdfBeansTemplate template) {

            super(template);
            this.strategy = new DeclaredQueryLookupStrategy(template);
            this.createStrategy = new CreateQueryLookupStrategy(template);
        }

        @Override
        protected RepositoryQuery resolveQuery(RdfQueryMethod method, RdfBeansTemplate template, NamedQueries namedQueries) {

            try {
                return strategy.resolveQuery(method, template, namedQueries);
            } catch (IllegalStateException e) {
                return createStrategy.resolveQuery(method, template, namedQueries);
            }
        }
    }

    /**
     * Creates a {@link QueryLookupStrategy} for the given {@link EntityManager}
     * and {@link Key}.
     * 
     * @param em
     * @param key
     * @return
     */
    public static QueryLookupStrategy create(RdfBeansTemplate template, Key key) {

        if (key == null) {
            return new CreateIfNotFoundQueryLookupStrategy(template);
        }

        switch (key) {
        case CREATE:
            return new CreateQueryLookupStrategy(template);
        case USE_DECLARED_QUERY:
            return new DeclaredQueryLookupStrategy(template);
        case CREATE_IF_NOT_FOUND:
            return new CreateIfNotFoundQueryLookupStrategy(template);
        default:
            throw new IllegalArgumentException(String.format("Unsupported query lookup strategy %s!", key));
        }
    }
}