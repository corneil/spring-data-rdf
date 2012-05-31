package org.springframework.data.rdf.simple;

import java.io.Serializable;

import com.mysema.rdfbean.annotations.ClassMapping;
import com.mysema.rdfbean.annotations.Predicate;
import com.mysema.rdfbean.model.FOAF;

@ClassMapping(ns = FOAF.NS)
public class Image extends Document implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8908001958990546695L;
    @Predicate
    private Image thumbnail;

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
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
