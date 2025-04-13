package com.project.messenger.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.messenger.entities.File;

public interface FileRepository extends JpaRepository<File, Integer> {
    
    public List<File> findAllByChat_id(int id);
}
