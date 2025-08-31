package com.codegym.repository;

import com.codegym.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
}
