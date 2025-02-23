package com.outsera.awards.repository;

import com.outsera.awards.model.StudioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudioRepository extends JpaRepository<StudioModel, Long> {
    Optional<StudioModel> findByName(String name);
}
