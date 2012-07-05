/**
 * 
 */
package org.springframework.data.rdf.querydsl;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mysema.query.types.path.PathBuilder;
import com.mysema.rdfbean.model.Format;
import com.mysema.rdfbean.model.Repository;

/**
 * @author Corneil du Plessis
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:simple-rdf-querydsl.xml" })
public class QueryDSLRdfQueryTest {

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
        PathBuilder<Image> imageClass = new PathBuilder<Image>(Image.class, "image");
        Iterable<Image> images = imageRepository.findAll(imageClass.get("topic").eq("photo"));
        // Iterable<Image> images = imageRepository.findAll(QImage.image.topic.eq("photo"));
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
        PathBuilder<Person> personClass = new PathBuilder<Person>(Person.class, "person");
        Iterable<Person> persons = personRepository.findAll(personClass.get("name").eq("John Doe"));
        // Iterable<Person> persons = personRepository.findAll(QPerson.person.name.eq("John Doe"));
        Assert.assertNotNull(persons);
        Assert.assertTrue("Cannot find person", persons.iterator().hasNext());
        Person person = persons.iterator().next();
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
