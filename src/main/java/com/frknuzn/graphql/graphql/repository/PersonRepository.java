package com.frknuzn.graphql.graphql.repository;

import com.frknuzn.graphql.graphql.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository  extends CrudRepository<Person,Long> {

    Person findByEmail(String email);

    Person findByName(String name);
}
