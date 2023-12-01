package com.AgusGalan.H2Commerce.persistence.repository;

import com.AgusGalan.H2Commerce.persistence.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
}
