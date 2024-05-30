package com.ekene.onlinebookstore.user.repository;

import com.ekene.onlinebookstore.user.model.ObsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ObsUser, Long> {
    Optional<ObsUser> findObsUserByEmailIgnoreCase(String email);
}
