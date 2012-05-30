package org.springframework.data.rdf.simple;

import java.io.Serializable;
import java.util.Date;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFNamespaces;
import com.viceversatech.rdfbeans.annotations.RDFSubject;

@RDFNamespaces({ "foaf = http://xmlns.com/foaf/0.1/",
		"persons = http://tsctech.com/hri/persons/" })
@RDFBean("foaf:Person")
public class Person implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8625018080522456584L;

	private Date birthday;
	private String email;
	private String name;
	private String personId;
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
	@RDF("foaf:birthday")
	public Date getBirthday() {
		return birthday;
	}
	@RDF("foaf:email")
	public String getEmail() {
		return email;
	}

	@RDF("foaf:name")
	public String getName() {
		return name;
	}

	@RDFSubject(prefix = "persons:")
	public String getPersonId() {
		return personId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((personId == null) ? 0 : personId.hashCode());
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
	@RDF("foaf:image")
	public Image getImage() {
		return image;
	}
	public void setImage(Image depiction) {
		this.image = depiction;
	}

}
