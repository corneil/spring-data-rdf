package org.springframework.data.rdf.query;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.rdf.repository.utils.RdfMetaDataUtil;

import com.mysema.rdfbean.annotations.ClassMapping;
import com.mysema.rdfbean.annotations.Id;
import com.mysema.rdfbean.annotations.Predicate;
import com.mysema.rdfbean.model.FOAF;
import com.mysema.rdfbean.model.ID;
import com.mysema.rdfbean.model.IDType;

@ClassMapping(ns = FOAF.NS)
public class Person implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8625018080522456584L;

    @Predicate
    private Date birthday;
    @Predicate
    private String email;
    @Predicate
    private String name;

    @Id(IDType.URI)
    public ID getId() {
        return RdfMetaDataUtil.makeId(getClass(), personId);
    }

    public void setId(ID id) {
        personId = id.asURI().getLocalName();
    }

    private String personId;
    
    @Predicate
    private Image image;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (personId == null) {
            if (other.personId != null)
                return false;
        } else if (!personId.equals(other.personId))
            return false;
        return true;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPersonId() {
        return personId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((personId == null) ? 0 : personId.hashCode());
        return result;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Person [");
        if (personId != null) {
            builder.append("personId=");
            builder.append(personId);
        }
        if (name != null) {
            builder.append(", ");
            builder.append("name=");
            builder.append(name);
        }
        if (email != null) {
            builder.append(", ");
            builder.append("email=");
            builder.append(email);
        }
        if (birthday != null) {
            builder.append(", ");
            builder.append("birthday=");
            builder.append(birthday);
        }
        if (image != null) {
            builder.append(", ");
            builder.append("image=");
            builder.append(image);
        }
        builder.append("]");
        return builder.toString();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image depiction) {
        this.image = depiction;
    }

}
