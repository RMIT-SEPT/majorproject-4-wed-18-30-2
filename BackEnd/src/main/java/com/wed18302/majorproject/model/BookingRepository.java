package com.wed18302.majorproject.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT booking FROM Booking booking WHERE booking.ID=(:pId)")
    Booking findByID(@Param("pId") int id);
    @Query("SELECT booking FROM Booking booking WHERE booking.CUSTOMER=(:pUser)")
    List<Booking> findByCUSTOMER(@Param("pUser") User customer);
    @Query("SELECT booking FROM Booking booking WHERE booking.WORKER=(:pUser)")
    List<Booking> findByWORKER(@Param("pUser") User worker);
    @Query("SELECT booking FROM Booking booking WHERE booking.SERVICE=(:pService)")
    List<Booking> findbySERVICE(@Param("pService") Service service);

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking booking WHERE booking.ID=(:bId)")
    void deleteByID(@Param("bId") int bookingId);
}
