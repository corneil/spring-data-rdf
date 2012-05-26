package org.springframework.data.rdf.simple;

import java.io.Serializable;
import java.util.Arrays;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFContainer;
import com.viceversatech.rdfbeans.annotations.RDFContainer.ContainerType;
import com.viceversatech.rdfbeans.annotations.RDFNamespaces;
import com.viceversatech.rdfbeans.annotations.RDFSubject;

@RDFNamespaces({ "foaf = http://xmlns.com/foaf/0.1/", "documents = http://tsctech.com/hri/documents/" })
@RDFBean("foaf:Document")
public class Document implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 8095675193517236984L;
    protected String id;
    private String primaryTopic;
    private String[] topic;

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

    @RDFSubject(prefix = "documents:")
    public String getId() {
        return id;
    }

    @RDF("foaf:primaryTopic")
    public String getPrimaryTopic() {
        return primaryTopic;
    }

    @RDF("foaf:topic")
    @RDFContainer(ContainerType.NONE)
    public String[] getTopic() {
        return topic;
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

    public void setTopic(String[] topic) {
        this.topic = topic;
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
            builder.append(Arrays.toString(topic));
        }
        builder.append("]");
        return builder.toString();
    }
}
