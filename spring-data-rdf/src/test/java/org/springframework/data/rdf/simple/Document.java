package org.springframework.data.rdf.simple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.mysema.rdfbean.annotations.ClassMapping;
import com.mysema.rdfbean.annotations.Container;
import com.mysema.rdfbean.annotations.ContainerType;
import com.mysema.rdfbean.annotations.Id;
import com.mysema.rdfbean.annotations.Predicate;
import com.mysema.rdfbean.model.FOAF;

@ClassMapping(ns = FOAF.NS, ln = "foaf:Document")
public class Document implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 8095675193517236984L;
    @Id
    protected String id;
    @Predicate
    private String primaryTopic;

    @Predicate
    @Container(ContainerType.NONE)
    private String[] topic;

    public String[] getTopic() {
        return topic;
    }

    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Document other = (Document) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getId() {
        return id;
    }

    public String getPrimaryTopic() {
        return primaryTopic;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrimaryTopic(String primaryTopic) {
        this.primaryTopic = primaryTopic;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Document [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (primaryTopic != null) {
            builder.append("primaryTopic=");
            builder.append(primaryTopic);
            builder.append(", ");
        }
        if (topic != null) {
            builder.append("topic=");
            builder.append(topic);
        }
        builder.append("]");
        return builder.toString();
    }

}
