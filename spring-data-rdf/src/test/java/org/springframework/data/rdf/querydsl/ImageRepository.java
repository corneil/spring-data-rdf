package org.springframework.data.rdf.querydsl;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, String>, QueryDslPredicateExecutor<Image> {
}
