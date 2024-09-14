package com.example.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.model.Authorities;

public interface AuthoritiesRepository<P> extends CrudRepository<Authorities, Long> {

}
