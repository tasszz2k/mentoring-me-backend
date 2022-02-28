package com.labate.mentoringme.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageCriteria { // TODO: Change Default -> find all
  public static final String ASC_SYMBOL = "+";
  public static final String DESC_SYMBOL = "-";

  @Min(value = 1, message = "page must be greater than 0")
  private Integer page = 1;

  @Min(value = 1, message = "limit must be greater than 0")
  @Max(value = 100, message = "limit must be less than or equal to 100")
  private Integer limit = 30;

  private List<String> sort = new ArrayList<>();

  public void setLimit(Integer limit) {
    if (limit != null) {
      this.limit = limit;
    }
  }

  public void setPage(Integer page) {
    if (page != null) {
      this.page = page;
    }
  }
}
