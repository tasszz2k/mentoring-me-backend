package com.labate.mentoringme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
