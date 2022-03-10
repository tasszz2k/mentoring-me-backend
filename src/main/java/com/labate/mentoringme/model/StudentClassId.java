package com.labate.mentoringme.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class StudentClassId implements Serializable {

  private Long studentId;
  private Long classId;
}
