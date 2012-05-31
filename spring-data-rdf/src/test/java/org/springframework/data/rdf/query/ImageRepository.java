package org.springframework.data.rdf.query;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, String> {
    List<Image> findByTopic(String topic);
}
