package org.springframework.data.rdf.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

public interface RdfRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

}
