package com.labate.mentoringme.service.report;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateReportRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Report;
import com.labate.mentoringme.repository.ReportRepository;
import com.labate.mentoringme.service.gcp.GoogleCloudFileUpload;
import com.labate.mentoringme.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final ReportRepository reportRepository;

  private final GoogleCloudFileUpload googleCloudFileUpload;

  @Override
  public Page<Report> getAllReport(PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return reportRepository.findAll(pageable);
  }

  @Override
  public Report createReport(CreateReportRequest createReportRequest) throws Exception {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var report = ObjectMapperUtils.map(createReportRequest, Report.class);
    report.setFromUserId(localUser.getUser().getId());
    var images = createReportRequest.getImages();
    if (images == null)
      return reportRepository.save(report);
    if (images.length >= 1) {
      var imageUrl1 = googleCloudFileUpload.upload(images[0]);
      report.setImgUrl1(imageUrl1);
    }
    if (images.length >= 2) {
      var imageUrl2 = googleCloudFileUpload.upload(images[1]);
      report.setImgUrl2(imageUrl2);
    }
    if (images.length > 2) {
      var imageUrl3 = googleCloudFileUpload.upload(images[2]);
      report.setImgUrl3(imageUrl3);
    }
    return reportRepository.save(report);
  }

  @Override
  public Optional<Report> getDetailReport(Long id) {
    return reportRepository.findById(id);
  }

  @Override
  public void deleteReportById(Long id) {
    reportRepository.deleteById(id);
  }

}
