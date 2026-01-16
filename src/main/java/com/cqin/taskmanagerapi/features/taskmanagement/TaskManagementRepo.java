package com.cqin.taskmanagerapi.features.taskmanagement;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cqin.taskmanagerapi.features.usermanagement.User;

@Repository
public interface TaskManagementRepo extends JpaRepository<Task, Long> {
   Slice<Task> findAllByUser(User user, Pageable pageable);

   Optional<Task> findByIdAndUserId(long id, long userId);

   long deleteByIdAndUserId(long taskId, long userId);
}
