package com.wed18302.majorproject.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import javax.transaction.Transactional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("SELECT service FROM Service service WHERE service.ID=(:pId)")
    Service findByID(@Param("pId") int id);
    @Query("SELECT service FROM Service service WHERE service.NAME=(:pName)")
    Service findByName(@Param("pName") String name);
    @Query("SELECT service FROM Service service WHERE service.ADMIN=(:pUser)")
    List<Service> findByBusiness(@Param("pUser") User admin);
    @Query("SELECT service FROM Service service WHERE service.TYPE=(:pType)")
    List<Service> findByType(@Param("pType") String type);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Service service WHERE service.ID=(:sId)")
    void deleteByServiceId(@Param("sId") int serviceId);
}
