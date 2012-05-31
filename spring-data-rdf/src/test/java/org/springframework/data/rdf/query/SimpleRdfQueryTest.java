/**
 * 
 */
package org.springframework.data.rdf.query;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mysema.rdfbean.model.Format;
import com.mysema.rdfbean.model.Repository;

/**
 * @author Corneil du Plessis
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:simple-rdf-query.xml" })
public class SimpleRdfQueryTest {

    @Autowired
    protected PersonRepository personRepository;
    @Autowired
    protected ImageRepository imageRepository;

    @Autowired
    Repository repository;

    @Before
    public void setUp() throws IOException {
        ClassPathResource foaf = new ClassPathResource("foaf.rdf");
        ClassPathResource data = new ClassPathResource("person-data.rdf");

        repository.load(Format.RDFXML, foaf.getInputStream(), null, true);
        repository.load(Format.RDFXML, data.getInputStream(), null, true);
    }

    @Test
    public void testLoadImages() throws Exception {

        ClassPathResource data = new ClassPathResource("person-data.rdf");
        repository.load(Format.RDFXML, data.getInputStream(), null, true);

        boolean foundImage = false;
        List<Image> images = imageRepository.findByTopic("photo");
        for (Image image : images) {
            foundImage = true;
            System.out.println("Image=" + image);
            Assert.assertTrue(image.getTopic().contains("photo"));
        }

        Assert.assertTrue("No images found", foundImage);
    }

    @Test
    public void testLoadPerson() throws Exception {

        ClassPathResource data = new ClassPathResource("person-data.rdf");
        repository.load(Format.RDFXML, data.getInputStream(), null, true);

        List<Person> persons = personRepository.findByName("John Doe");
        Assert.assertNotNull(persons);
        Assert.assertFalse("Cannot find person", persons.isEmpty());
        Person person = persons.get(0);
        Assert.assertNotNull("Cannot find person", person);
        System.out.println("person=" + person);
        Assert.assertEquals("John Doe", person.getName());
        Assert.assertNotNull("Image not found", person.getImage());
        Assert.assertNotNull("Thumbnail not found", person.getImage().getThumbnail());
        Assert.assertEquals(2, person.getImage().getTopic().size());
        Assert.assertNotNull("Thumbnail not found", person.getImage().getThumbnail());
        Assert.assertNotNull("Thumbnail Topics not found", person.getImage().getThumbnail().getTopic());
        Assert.assertEquals(3, person.getImage().getThumbnail().getTopic().size());
    }
}
