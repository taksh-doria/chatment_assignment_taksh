package com.taksh.chatment_assignment_taksh.repository;

import com.taksh.chatment_assignment_taksh.model.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Logs,Integer>
{
}
