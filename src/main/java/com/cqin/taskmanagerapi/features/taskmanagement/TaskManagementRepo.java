package com.cqin.taskmanagerapi.features.taskmanagement;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cqin.taskmanagerapi.features.usermanagement.User;

@Repository
public interface TaskManagementRepo extends JpaRepository<Task, Long> {
   @Query("SELECT t FROM Task t WHERE t.user = :user AND (:status IS NULL OR t.status = :status)")
   Slice<Task> findAllByUserAndStatusOptional(
         @Param("user") User user,
         @Param("status") TaskStatus status,
         Pageable pageable);

   Optional<Task> findByIdAndUserId(long id, long userId);

   long deleteByIdAndUserId(long taskId, long userId);
}
