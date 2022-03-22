package com.labate.mentoringme.service.report;

import org.springframework.data.domain.Page;
import com.labate.mentoringme.dto.request.CreateReportRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Report;

public interface ReportService {
  Page<Report> getAllReport(PageCriteria pageCriteria);

  Report createReport(CreateReportRequest createReportRequest) throws Exception;
}
