package org.springframework.data.rdf.repository.query;

import java.util.List;

import org.springframework.data.rdf.template.RdfBeansTemplate;
import org.springframework.util.Assert;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.rdfbean.object.BeanQuery;
import com.mysema.rdfbean.object.Session;

@SuppressWarnings("rawtypes")
public class RDFQuery {
    private BeanQuery query;
    private RdfBeansTemplate template;

    public RdfBeansTemplate getTemplate() {
        return template;
    }

    public void setTemplate(RdfBeansTemplate template) {
        this.template = template;
    }

    private EntityPath<?> path;
    private Predicate criteria;

    private List<OrderSpecifier<Comparable>> orders;

    public RDFQuery(EntityPath<?> path, Predicate criteria) {
        super();
        this.path = path;
        this.criteria = criteria;
    }

    public RDFQuery(EntityPath<?> path, Predicate criteria, List<OrderSpecifier<Comparable>> orders) {
        super();
        this.path = path;
        this.criteria = criteria;
        this.orders = orders;
    }

    protected BeanQuery create() {
        if (query == null) {
            Assert.notNull(template);
            Session session = template.getSessionFactory().openSession();
            query = session.from(path).where(criteria);
            if (orders != null) {
                query.orderBy(orders.toArray(new OrderSpecifier[orders.size()]));
            }
        }
        return query;
    }

    public List<?> execute() {
        return create().list(path);
    }

    public Object singleResult() {
        return create().singleResult(path);
    }

    public Long count() {
        return create().count();
    }

    public Long countDistinct() {
        return create().countDistinct();
    }
}
