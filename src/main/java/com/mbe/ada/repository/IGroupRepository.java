package com.mbe.ada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mbe.ada.model.group.Group;

import jakarta.transaction.Transactional;

public interface IGroupRepository extends JpaRepository<Group, Long> {
	
	  //Busca grupos ativos
    List<Group> findByIsActiveTrue();

    // Busca pelo nome do grupo
    Group findByNameIgnoreCase(String name);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE group_person SET is_active = false WHERE group_id = :groupId AND person_id = :personId", nativeQuery = true)
    Integer deactivateUserInGroup(@Param("groupId") Long groupId, @Param("personId") Long personId);

    // Buscar grupos que ocorrem em um determinado dia (exemplo: segunda-feira)
    List<Group> findByMondayTrue();
    List<Group> findByTuesdayTrue();
    List<Group> findByWednesdayTrue();
    List<Group> findByThursdayTrue();
    List<Group> findByFridayTrue();
    List<Group> findBySaturdayTrue();
    List<Group> findBySundayTrue();

}
