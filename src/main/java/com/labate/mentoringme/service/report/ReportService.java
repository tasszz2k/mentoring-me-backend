package com.labate.mentoringme.service.report;

import java.util.Optional;
import org.springframework.data.domain.Page;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateReportRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Report;

public interface ReportService {
  Page<Report> getAllReport(PageCriteria pageCriteria);

  Report createReport(CreateReportRequest createReportRequest, LocalUser localUser)
      throws Exception;

  Optional<Report> getDetailReport(Long id);

  void deleteReportById(Long id);
}
