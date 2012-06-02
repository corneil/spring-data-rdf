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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.rdf.repository.utils.RdfMetaDataUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mysema.rdfbean.model.Format;
import com.mysema.rdfbean.model.Repository;

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
    Repository repository;

    @Before
    public void setUp() throws IOException {
        ClassPathResource foaf = new ClassPathResource("foaf.rdf");
        // ClassPathResource data = new ClassPathResource("person-data.rdf");

        repository.load(Format.RDFXML, foaf.getInputStream(), null, true);
        // repository.load(Format.RDFXML, data.getInputStream(), null, true);
    }

    @Test
    public void testCreatePerson() {

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
        repository.export(Format.RDFXML, null, System.out);
    }

    @Test
    public void testLoadImages() throws Exception {

        ClassPathResource data = new ClassPathResource("person-data.rdf");
        repository.load(Format.RDFXML, data.getInputStream(), null, true);

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

        ClassPathResource data = new ClassPathResource("person-data.rdf");
        repository.load(Format.RDFXML, data.getInputStream(), null, true);

        Person person = personRepository.findOne(RdfMetaDataUtil.makeId(Person.class, "john.doe").getId());
        Assert.assertNotNull("Cannot find person", person);
        System.out.println("person=" + person);
        Assert.assertEquals("John Doe", person.getName());
        Assert.assertNotNull("Image not found", person.getImage());
        Assert.assertNotNull("Topic not found", person.getImage().getTopic());
        Assert.assertEquals(2, person.getImage().getTopic().length);
        Assert.assertNotNull("Thumbnail not found", person.getImage().getThumbnail());
        Assert.assertNotNull("Thumbnail Topics not found", person.getImage().getThumbnail().getTopic());
        Assert.assertEquals(3, person.getImage().getThumbnail().getTopic().length);
    }
}
