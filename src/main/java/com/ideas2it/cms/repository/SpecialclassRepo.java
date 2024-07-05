package com.ideas2it.cms.repository;

import java.util.List;

import com.ideas2it.cms.model.SpecialClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpecialclassRepo extends JpaRepository<SpecialClass, Integer> {
    @Query("SELECT sc FROM SpecialClass sc WHERE sc.specialClassId IN :specialClassIds")
    List<SpecialClass> findBySpecialClassId(@Param("specialClassIds") List<Integer> specialClassIds);
}
