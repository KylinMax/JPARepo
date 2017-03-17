package com.kylin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kylin.entities.Field;

public interface FieldDao extends JpaRepository<Field, Long>{
	
}
