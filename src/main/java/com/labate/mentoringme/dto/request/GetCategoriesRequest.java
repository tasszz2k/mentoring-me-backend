package com.labate.mentoringme.dto.request;

import lombok.Value;

@Value
public class GetCategoriesRequest {
  Boolean isParent;
  Integer parentCategoryId;
}
