package com.cqin.taskmanagerapi.features.taskmanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cqin.taskmanagerapi.features.usermanagement.User;

@Repository
public interface TaskManagementRepo extends JpaRepository<Task, Long> {
   List<Task> findAllByUser(User user);
}
