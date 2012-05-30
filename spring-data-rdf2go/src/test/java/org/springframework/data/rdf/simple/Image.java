package org.springframework.data.rdf.simple;

import java.io.Serializable;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFNamespaces;
import com.viceversatech.rdfbeans.annotations.RDFSubject;

@RDFNamespaces({ "foaf = http://xmlns.com/foaf/0.1/",
		"images = http://tsctech.com/hri/images/" })
@RDFBean("foaf:Image")
public class Image extends Document implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8908001958990546695L;
	private Document thumbnail;

	@RDFSubject(prefix = "images:")
	public String getId() {
		return super.getId();
	}

	@RDF("foaf:thumbnail")
	public Document getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Document thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Image [");
		builder.append(super.toString());

		if (thumbnail != null) {
			builder.append(", ");
			builder.append("thumbnail=");
			builder.append(thumbnail);
		}
		builder.append("]");
		return builder.toString();
	}

}
