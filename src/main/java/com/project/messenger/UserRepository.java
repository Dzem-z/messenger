package com.project.messenger;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {
    
    List<User> findByNick(String nick);
}
