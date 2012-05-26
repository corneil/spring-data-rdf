package org.springframework.data.rdf.template;

import java.util.Collection;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.node.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rdf.exception.RdfException;

import com.viceversatech.rdfbeans.RDFBeanManager;
import com.viceversatech.rdfbeans.exceptions.RDFBeanException;

public class RdfBeansTemplate {
    @Autowired
    private ModelFactory modelFactory;

    private static ThreadLocal<Model> currentModel = new ThreadLocal<Model>();
    private static ThreadLocal<ModelSet> currentModelSet = new ThreadLocal<ModelSet>();

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    public RdfBeansTemplate() {
        super();
    }

    public RdfBeansTemplate(ModelFactory modelFactory) {
        super();
        this.modelFactory = modelFactory;
    }

    public void setModelFactory(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    public Model createModel() {
        // may have to provide for properties etc.
        return modelFactory.createModel();
    }

    public Model getModel() {
        Model model = currentModel.get();
        if (model == null) {
            model = createModel();
            currentModel.set(model);
        }
        return model;
    }

    public void setModel(Model model) {
        currentModel.set(model);
    }

    public ModelSet createModelSet() {
        // may have to provide for properties etc.
        return modelFactory.createModelSet();
    }

    public ModelSet getModelSet() {
        ModelSet modelSet = currentModelSet.get();
        if (modelSet == null) {
            modelSet = createModelSet();
            currentModelSet.set(modelSet);
        }
        return modelSet;
    }

    public void setModelSet(ModelSet modelSet) {
        currentModelSet.set(modelSet);
    }

    public void addAll(Iterable<?> entities) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            for (Object e : entities) {
                manager.add(e);
            }
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }

    public boolean exists(String key, Class<?> rdfBeanClass) {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            return manager.isResourceExist(manager.getResource(key, rdfBeanClass), rdfBeanClass);
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }

    public Resource add(Object o) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            return manager.add(o);
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }

    public Resource update(Object o) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            return manager.update(o);
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }

    public Object get(String key, Class<?> cls) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            return manager.get(key, cls);
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }

    public Object get(Resource r, Class<?> cls) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            return manager.get(r, cls);
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }

    public Object get(Resource r) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            return manager.get(r);
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }

    public ClosableIterator<?> getAll(final Class<?> rdfBeanClass) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            return manager.getAll(rdfBeanClass);
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            // Do not close because iterator will close. model.close();
        }
    }

    public void delete(Resource r) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            manager.delete(r);
        } finally {
            model.close();
        }
    }

    public void delete(String rid, Class<?> cls) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            manager.delete(rid, cls);
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }

    public void deleteAll(Collection<String> rids, Class<?> cls) throws RdfException {
        Model model = getModel();
        RDFBeanManager manager = new RDFBeanManager(model);
        try {
            model.open();
            for (String rid : rids) {
                manager.delete(rid, cls);
            }
        } catch (RDFBeanException x) {
            throw new RdfException(x);
        } finally {
            model.close();
        }
    }
}
