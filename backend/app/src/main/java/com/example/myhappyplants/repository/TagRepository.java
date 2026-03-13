package com.example.myhappyplants.repository;

import com.example.myhappyplants.entity.Tag;
import com.example.myhappyplants.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUser(User user);

    Optional<Tag> findByNameAndUser(String name, User user);

}
