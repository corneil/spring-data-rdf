/**
 * 
 */
package org.springframework.data.rdf.simple;

import java.io.IOException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.rdf.template.RdfBeansTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Corneil du Plessis
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:simple-rdf-crud.xml" })
public class SimpleRdfCrudTest {

    @Autowired
    protected PersonRepository personRepository;
    @Autowired
    protected ImageRepository imageRepository;
    @Autowired
    protected ModelFactory modelFactory;

    protected RdfBeansTemplate template;

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    public void setModelFactory(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        template = new RdfBeansTemplate(modelFactory);
    }

    public RdfBeansTemplate getTemplate() {
        Assert.assertNotNull("modelFactory is required", modelFactory);
        if (template == null) {
            template = new RdfBeansTemplate(modelFactory);
        }
        return template;
    }

    public void setTemplate(RdfBeansTemplate template) {
        this.template = template;
    }

    @Before
    public void setUp() throws ModelRuntimeException, IOException {
        ClassPathResource foaf = new ClassPathResource("foaf.rdf");
        ClassPathResource data = new ClassPathResource("person-data.rdf");
        Model model = getTemplate().getModel();
        model.open();
        model.readFrom(foaf.getInputStream());
        model.readFrom(data.getInputStream());
        model.close();
    }

    @Test
    public void testCreatePerson() throws ModelRuntimeException, IOException {
        getTemplate().setModel(null); // ensure the model is blank
        Person person = new Person();
        person.setName("John Doe");
        person.setEmail("john.doe@nowhere.net");
        person.setPersonId("john.doe");
        Image image = new Image();
        image.setId(UUID.randomUUID().toString());
        image.setPrimaryTopic("photo");
        image.setTopic(new String[] { "photo", "person" });
        Image thumb = new Image();
        thumb.setId(UUID.randomUUID().toString());
        thumb.setPrimaryTopic("thumb");
        thumb.setTopic(new String[] { "thumb", "photo", "person" });
        image.setThumbnail(thumb);
        person.setImage(image);
        imageRepository.save(thumb);
        imageRepository.save(image);
        personRepository.save(person);
        Model model = template.getModel();
        model.open();
        model.writeTo(System.out);
        model.close();
    }

    @Test
    public void testLoadImages() throws Exception {

        boolean foundImage = false;
        Iterable<Image> images = imageRepository.findAll();
        for (Image image : images) {
            foundImage = true;
            System.out.println("Image=" + image);
        }

        Assert.assertTrue("No images found", foundImage);
    }

    @Test
    public void testLoadPerson() throws Exception {
        ClassPathResource foaf = new ClassPathResource("foaf.rdf");
        ClassPathResource data = new ClassPathResource("person-data.rdf");
        Model model = getTemplate().getModel();
        model.open();
        model.readFrom(foaf.getInputStream());
        model.readFrom(data.getInputStream());
        model.close();
        Person person = personRepository.findOne("john.doe");
        System.out.println("person=" + person);
        Assert.assertEquals("John Doe", person.getName());
        Assert.assertNotNull("Image not found", person.getImage());
        Assert.assertNotNull("Thumbnail not found", person.getImage().getThumbnail());
        Assert.assertEquals(2, person.getImage().getTopic().length);
        Assert.assertNotNull("Thumbnail not found", person.getImage().getThumbnail());
        Assert.assertNotNull("Thumbnail Topics not found", person.getImage().getThumbnail().getTopic());
        Assert.assertEquals(3, person.getImage().getThumbnail().getTopic().length);
    }

}
