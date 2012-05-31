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
package org.springframework.data.rdf.repository.support;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.rdf.template.RdfBeansTemplate;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.rdfbean.object.BeanQuery;
import com.mysema.rdfbean.object.Session;

/**
 * 
 * @author Oliver Gierke, Corneil du Plessis
 */
public class QueryDslRdfRepository<T, ID extends Serializable> extends SimpleRdfRepository<T, ID> implements QueryDslPredicateExecutor<T> {

    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

    private final EntityPath<T> path;
    private final PathBuilder<T> builder;

    /**
     * Creates a new {@link QueryDslRdfRepository} from the given domain class
     * and {@link EntityManager}. This will use the
     * {@link SimpleEntityPathResolver} to translate the given domain class into
     * an {@link EntityPath}.
     * 
     * @param entityInformation
     *            must not be {@literal null}.
     * @param entityManager
     *            must not be {@literal null}.
     */
    public QueryDslRdfRepository(RdfEntityInformation<T, ID> entityInformation, RdfBeansTemplate template) {

        this(entityInformation, template, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    /**
     * Creates a new {@link QueryDslRdfRepository} from the given domain class
     * and {@link EntityManager} and uses the given {@link EntityPathResolver}
     * to translate the domain class into an {@link EntityPath}.
     * 
     * @param entityInformation
     *            must not be {@literal null}.
     * @param entityManager
     *            must not be {@literal null}.
     * @param resolver
     *            must not be {@literal null}.
     */
    public QueryDslRdfRepository(RdfEntityInformation<T, ID> entityInformation, RdfBeansTemplate template, EntityPathResolver resolver) {
        super(entityInformation, template);
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#findOne(com
     * .mysema.query.types.Predicate)
     */
    public T findOne(Predicate predicate) {
        return createQuery(predicate).uniqueResult(path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#findAll(com
     * .mysema.query.types.Predicate)
     */
    public List<T> findAll(Predicate predicate) {
        return createQuery(predicate).list(path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#findAll(com
     * .mysema.query.types.Predicate,
     * com.mysema.query.types.OrderSpecifier<?>[])
     */
    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        return createQuery(predicate).orderBy(orders).list(path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#findAll(com
     * .mysema.query.types.Predicate, org.springframework.data.domain.Pageable)
     */
    public Page<T> findAll(Predicate predicate, Pageable pageable) {

        BeanQuery countQuery = createQuery(predicate);
        BeanQuery query = applyPagination(createQuery(predicate), pageable);

        return new PageImpl<T>(query.list(path), pageable, countQuery.count());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#count(com
     * .mysema.query.types.Predicate)
     */
    public long count(Predicate predicate) {
        return createQuery(predicate).count();
    }

    /**
     * Creates a new {@link JPQLQuery} for the given {@link Predicate}.
     * 
     * @param predicate
     * @return the Querydsl {@link JPQLQuery}.
     */
    protected BeanQuery createQuery(Predicate... predicate) {
        Session session = template.getSessionFactory().openSession();
        return session.from(builder).where(predicate);
    }

    /**
     * Applies the given {@link Pageable} to the given {@link JPQLQuery}.
     * 
     * @param query
     *            must not be {@literal null}.
     * @param pageable
     * @return the Querydsl {@link JPQLQuery}.
     */
    protected BeanQuery applyPagination(BeanQuery query, Pageable pageable) {

        if (pageable == null) {
            return query;
        }

        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        return applySorting(query, pageable.getSort());
    }

    /**
     * Applies sorting to the given {@link JPQLQuery}.
     * 
     * @param query
     *            must not be {@literal null}.
     * @param sort
     * @return the Querydsl {@link JPQLQuery}
     */
    protected BeanQuery applySorting(BeanQuery query, Sort sort) {

        if (sort == null) {
            return query;
        }

        for (Order order : sort) {
            query.orderBy(toOrder(order));
        }

        return query;
    }

    /**
     * Transforms a plain {@link Order} into a QueryDsl specific
     * {@link OrderSpecifier}.
     * 
     * @param order
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected OrderSpecifier<?> toOrder(Order order) {

        Expression<Object> property = builder.get(order.getProperty());

        return new OrderSpecifier(order.isAscending() ? com.mysema.query.types.Order.ASC : com.mysema.query.types.Order.DESC, property);
    }
}
