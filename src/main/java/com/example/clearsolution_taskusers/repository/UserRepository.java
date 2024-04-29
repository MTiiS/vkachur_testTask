package com.example.clearsolution_taskusers.repository;

import com.example.clearsolution_taskusers.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u " +
            "WHERE  (cast(:dateFrom AS DATE) IS NULL OR u.dateOfBirth >= :dateFrom ) " +
            "AND (cast(:dateTo AS DATE) IS NULL OR u.dateOfBirth <= :dateTo)")
    List<User> findAllByFilter(
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            Sort sort
    );
}
