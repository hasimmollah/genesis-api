package com.hasim.genesis.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hasim.genesis.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
