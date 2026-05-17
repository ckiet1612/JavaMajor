package com.orbital.repository;

import com.orbital.model.SpaceObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceObjectRepository extends JpaRepository<SpaceObject, Long> {
}
