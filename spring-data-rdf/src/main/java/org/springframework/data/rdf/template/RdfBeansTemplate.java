package org.springframework.data.rdf.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rdf.exception.RdfException;

import com.mysema.rdfbean.model.ID;
import com.mysema.rdfbean.model.LID;
import com.mysema.rdfbean.object.Session;
import com.mysema.rdfbean.object.SessionFactory;

public class RdfBeansTemplate {
    @Autowired
    private SessionFactory sessionFactory;

    public RdfBeansTemplate() {
        super();
    }

    public RdfBeansTemplate(SessionFactory sessionFactory) {
        super();
        this.sessionFactory = sessionFactory;
    }

    public void addAll(Iterable<?> entities) throws RdfException {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {

            for (Object e : entities) {
                session.save(e);
            }
        } catch (Exception x) {
            throw new RdfException(x);
        } finally {
            session.close();
        }
    }

    public void delete(Object o) throws RdfException {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {
            session.delete(o);
        } finally {
            session.close();
        }
    }

    public void deleteAll(Iterable<?> objects) throws RdfException {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {
            for (Object o : objects) {
                session.delete(o);
            }
        } catch (Exception x) {
            throw new RdfException(x);
        } finally {
            session.close();
        }
    }

    public boolean exists(String key, Class<?> rdfBeanClass) {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {
            return false; // TODO how to determine if object exists
        } catch (Exception x) {
            throw new RdfException(x);
        } finally {
            session.close();
        }
    }

    public Object find(ID id, Class<?> cls) throws RdfException {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {
            return session.get(cls, id);
        } catch (Exception x) {
            throw new RdfException(x);
        } finally {
            session.close();
        }
    }

    public Object find(LID id, Class<?> cls) throws RdfException {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {
            return session.get(cls, id);
        } catch (Exception x) {
            throw new RdfException(x);
        } finally {
            session.close();
        }
    }

    public Object get(String key, Class<?> cls) throws RdfException {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {
            return session.getById(key, cls);
        } catch (Exception x) {
            throw new RdfException(x);
        } finally {
            session.close();
        }
    }

    public <T> List<T> getAll(final Class<T> rdfBeanClass) throws RdfException {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {
            return session.findInstances(rdfBeanClass);
        } catch (Exception x) {
            throw new RdfException(x);
        } finally {
            session.close();
        }
    }

    public LID save(Object o) throws RdfException {
        Session session = sessionFactory.openSession();
        assert session != null;
        try {
            return session.save(o);
        } catch (Exception x) {
            throw new RdfException(x);
        } finally {
            session.close();
        }
    }
}
