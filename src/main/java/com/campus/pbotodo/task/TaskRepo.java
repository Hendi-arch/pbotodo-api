package com.campus.pbotodo.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<TaskItem, Long> {

    List<TaskItem> findByUsername(String username);

    List<TaskItem> findByDone(boolean done);

    TaskItem findByIdAndUsername(Long id, String username);

    boolean existsByIdAndUsername(Long id, String username);

    void deleteByIdAndUsername(Long id, String username);

}
