package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.request.PageCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.stream.Collectors;

public class PageCriteriaPageableMapper {

  public static Pageable toPageable(PageCriteria criteria) {
    var orders =
        criteria.getSort().stream()
            .map(
                sortStr -> {
                  if (StringUtils.hasText(sortStr)) {
                    if (sortStr.startsWith(PageCriteria.DESC_SYMBOL) && sortStr.length() > 1) {
                      return Sort.Order.desc(sortStr.substring(1));
                    } else if (sortStr.startsWith(PageCriteria.ASC_SYMBOL)
                        && sortStr.length() > 1) {
                      return Sort.Order.asc(sortStr.substring(1));
                    } else {
                      return Sort.Order.asc(sortStr);
                    }
                  }
                  return null;
                })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    return PageRequest.of(criteria.getPage() - 1, criteria.getLimit(), Sort.by(orders));
  }
}
