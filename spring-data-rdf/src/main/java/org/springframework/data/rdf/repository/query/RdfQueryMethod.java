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

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.rdf.repository.Modifying;
import org.springframework.data.rdf.repository.Sparql;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * JPA specific extension of {@link QueryMethod}.
 * 
 * @author Oliver Gierke
 */
public class RdfQueryMethod extends QueryMethod {

    private final Method method;

    /**
     * Creates a {@link RdfQueryMethod}.
     * 
     * @param method
     *            must not be {@literal null}
     * @param extractor
     *            must not be {@literal null}
     * @param metadata
     *            must not be {@literal null}
     */
    public RdfQueryMethod(Method method, RepositoryMetadata metadata) {

        super(method, metadata);

        Assert.notNull(method, "Method must not be null!");

        this.method = method;

        Assert.isTrue(!(isModifyingQuery() && getParameters().hasSpecialParameter()), String.format("Modifying method must not contain %s!", Parameters.TYPES));
        assertParameterNamesInAnnotatedQuery();
    }

    private void assertParameterNamesInAnnotatedQuery() {

        String annotatedQuery = getAnnotatedQuery();

        if (!StringUtils.hasText(annotatedQuery)) {
            return;
        }

        for (Parameter parameter : getParameters()) {

            if (!parameter.isNamedParameter()) {
                continue;
            }

            if (!annotatedQuery.contains(String.format(":%s", parameter.getName()))) {
                throw new IllegalStateException(String.format("Using named parameters for method %s but parameter '%s' not found in annotated query '%s'!",
                        method, parameter.getName(), annotatedQuery));
            }
        }
    }

    /**
     * Returns whether the finder is a modifying one.
     * 
     * @return
     */
    @Override
    public boolean isModifyingQuery() {

        return null != method.getAnnotation(Modifying.class);
    }

    /**
     * Returns the actual return type of the method.
     * 
     * @return
     */
    Class<?> getReturnType() {

        return method.getReturnType();
    }

    /**
     * Returns the query string declared in a {@link Sparql} annotation or
     * {@literal null} if neither the annotation found nor the attribute was
     * specified.
     * 
     * @return
     */
    String getAnnotatedQuery() {

        String query = getAnnotationValue("value", String.class);
        return StringUtils.hasText(query) ? query : null;
    }

    /**
     * Returns the countQuery string declared in a {@link Sparql} annotation or
     * {@literal null} if neither the annotation found nor the attribute was
     * specified.
     * 
     * @return
     */
    String getCountQuery() {

        String countQuery = getAnnotationValue("countQuery", String.class);
        return StringUtils.hasText(countQuery) ? countQuery : null;
    }

    /**
     * Returns whether the backing query is a native one.
     * 
     * @return
     */
    boolean isNativeQuery() {
        return getAnnotationValue("nativeQuery", Boolean.class).booleanValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.repository.query.QueryMethod#getNamedQueryName()
     */
    @Override
    public String getNamedQueryName() {

        String annotatedName = getAnnotationValue("name", String.class);
        return StringUtils.hasText(annotatedName) ? annotatedName : super.getNamedQueryName();
    }

    /**
     * Returns the name of the {@link NamedQuery} that shall be used for count
     * queries.
     * 
     * @return
     */
    String getNamedCountQueryName() {

        String annotatedName = getAnnotationValue("countName", String.class);
        return StringUtils.hasText(annotatedName) ? annotatedName : getNamedQueryName() + ".count";
    }

    /**
     * Returns whether we should clear automatically for modifying queries.
     * 
     * @return
     */
    boolean getClearAutomatically() {

        return (Boolean) AnnotationUtils.getValue(method.getAnnotation(Modifying.class), "clearAutomatically");
    }

    /**
     * Returns the {@link Sparql} annotation's attribute casted to the given
     * type or default value if no annotation available.
     * 
     * @param attribute
     * @param type
     * @param defaultValue
     * @return
     */
    private <T> T getAnnotationValue(String attribute, Class<T> type) {

        Sparql annotation = method.getAnnotation(Sparql.class);
        Object value = annotation == null ? AnnotationUtils.getDefaultValue(Sparql.class, attribute) : AnnotationUtils.getValue(annotation, attribute);

        return type.cast(value);
    }
}
