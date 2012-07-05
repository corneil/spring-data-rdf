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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.PathBuilderFactory;

/**
 * Query creator to create a {@link CriteriaQuery} from a {@link PartTree}.
 * 
 * @author Oliver Gierke
 */
public class RdfQueryCreator extends AbstractQueryCreator<RDFQuery, Predicate> {

    private final PathBuilder<?> builder;
    private final Iterator<Object> params;

    /**
     * Create a new {@link RdfQueryCreator}.
     * 
     * @param tree
     * @param domainClass
     * @param accessor
     * @param em
     */
    public RdfQueryCreator(PartTree tree, ParameterAccessor parameters, Class<?> domainClass) {
        super(tree, parameters);
        this.builder = new PathBuilderFactory().create(domainClass);
        this.params = parameters.iterator();
    }

    @Override
    protected Predicate create(Part part, Iterator<Object> iterator) {
        return new PredicateBuilder(part, builder).build();
    }

    @Override
    protected Predicate and(Part part, Predicate base, Iterator<Object> iterator) {
        if (base instanceof BooleanExpression) {
            BooleanExpression booleanExpression = (BooleanExpression) base;
            return booleanExpression.and(create(part, iterator));
        }
        throw new RuntimeException("Don't know how to handle 'and' for:" + base);

    }

    @Override
    protected Predicate or(Predicate base, Predicate criteria) {
        if (base instanceof BooleanExpression) {
            BooleanExpression booleanExpression = (BooleanExpression) base;
            return booleanExpression.or(criteria);
        }
        throw new RuntimeException("Don't know how to handle 'or' for:" + base);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected RDFQuery complete(Predicate criteria, Sort sort) {

        if (sort != null) {
            Iterator<Order> orders = sort.iterator();
            List<OrderSpecifier<Comparable>> specifiers = new ArrayList<OrderSpecifier<Comparable>>();
            while (orders.hasNext()) {
                Order order = orders.next();
                com.mysema.query.types.Order direction = Direction.DESC.equals(order.getDirection()) ? com.mysema.query.types.Order.DESC
                        : com.mysema.query.types.Order.ASC;
                PathBuilder<?> prop = builder.get(order.getProperty());
                Class<Comparable> type = (Class<Comparable>) prop.getMetadata().getPathType().getTypes().get(0);
                OrderSpecifier<Comparable> specifier = new OrderSpecifier<Comparable>(direction, builder.getComparable(order.getProperty(), type));
                specifiers.add(specifier);
            }
            return new RDFQuery(builder, criteria, specifiers);
        } else {
            return new RDFQuery(builder, criteria);
        }
    }

    @SuppressWarnings("unchecked")
    static Expression<Object> toExpressionRecursively(PathBuilder<?> path, PropertyPath property) {

        PathBuilder<?> result = path.get(property.getSegment());
        return (Expression<Object>) (property.hasNext() ? toExpressionRecursively(result, property.next()) : result);
    }

    private class PredicateBuilder {

        private final Part part;
        private final PathBuilder<?> root;

        /**
         * Creates a new {@link PredicateBuilder} for the given {@link Part} and
         * {@link Root}.
         * 
         * @param part
         *            must not be {@literal null}.
         * @param root
         *            must not be {@literal null}.
         */
        public PredicateBuilder(Part part, PathBuilder<?> root) {

            Assert.notNull(part);
            Assert.notNull(root);
            this.part = part;
            this.root = root;
        }

        /**
         * Builds a JPA {@link Predicate} from the underlying {@link Part}.
         * 
         * @return
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public Predicate build() {

            PropertyPath property = part.getProperty();
            ComparablePath<Comparable> prop = new ComparablePath(property.getType(), root, property.getSegment());
            switch (part.getType()) {
            case BETWEEN: {
                Comparable first = (Comparable) params.next();
                Comparable second = (Comparable) params.next();
                return prop.between(first, second);
            }
            case GREATER_THAN: {
                Comparable param = (Comparable) params.next();
                return prop.gt(param);
            }
            case GREATER_THAN_EQUAL: {
                Comparable param = (Comparable) params.next();
                return prop.goe(param);
            }
            case LESS_THAN: {
                Comparable param = (Comparable) params.next();
                return prop.lt(param);
            }
            case LESS_THAN_EQUAL: {
                Comparable param = (Comparable) params.next();
                return prop.loe(param);
            }
            case IS_NULL:
                return prop.isNull();
            case IS_NOT_NULL:
                return prop.isNotNull();
                /*
                 * case NOT_IN: { ParameterMetadata<Collection> param =
                 * (ParameterMetadata<Collection>) provider.next(part,
                 * Collection.class); return
                 * prop.in(param.getExpression()).not(); } case IN: {
                 * ParameterMetadata<Collection> param =
                 * (ParameterMetadata<Collection>) provider.next(part,
                 * Collection.class); return prop.in(param.getExpression()); }
                 */
                /*
                 * case LIKE: case NOT_LIKE: // TODO: throw exception not
                 * supported Expression<String> stringPath = getTypedPath(root,
                 * part); Expression<String> propertyExpression =
                 * upperIfIgnoreCase(stringPath); Expression<String>
                 * parameterExpression = upperIfIgnoreCase(provider.next(part,
                 * String.class).getExpression()); StringPath like = new
                 * StringPath(property); Predicate like =
                 * prop.builder.like(propertyExpression, parameterExpression);
                 * return part.getType() == Type.NOT_LIKE ? like.not() : like;
                 */
            case TRUE: {
                BooleanPath booleanProp = new BooleanPath(root, property.getSegment());
                return booleanProp.isTrue();
            }
            case FALSE: {
                BooleanPath booleanProp = new BooleanPath(root, property.getSegment());
                return booleanProp.isFalse();
            }
            case SIMPLE_PROPERTY: {
                Comparable param = (Comparable) params.next();
                return prop.eq(param);
            }
            case NEGATING_SIMPLE_PROPERTY: {
                Comparable param = (Comparable) params.next();
                return prop.ne(param);
            }

            default:
                throw new IllegalArgumentException("Unsupported keyword " + part.getType());
            }
        }
    }
}
