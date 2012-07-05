package org.springframework.data.rdf.querydsl;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.mysema.query.annotations.QueryEntity;


public interface PersonRepository extends CrudRepository<Person, String>, QueryDslPredicateExecutor<Person> {
}
