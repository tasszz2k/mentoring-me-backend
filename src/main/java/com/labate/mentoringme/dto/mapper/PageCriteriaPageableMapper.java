package com.labate.mentoringme.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.labate.mentoringme.dto.request.PageCriteria;

public class PageCriteriaPageableMapper {

  public static Pageable toPageable(PageCriteria criteria) {
    List<Sort.Order> orders = new ArrayList<>();
    if (!CollectionUtils.isEmpty(criteria.getSort())) {
      String sortStr = criteria.getSort().get(criteria.getSort().size() - 1);
      if (StringUtils.hasText(sortStr)) {
        if (sortStr.startsWith(PageCriteria.DESC_SYMBOL) && sortStr.length() > 1) {
          orders.add(Sort.Order.desc(sortStr.substring(1)));
        } else if (sortStr.startsWith(PageCriteria.ASC_SYMBOL) && sortStr.length() > 1) {
          orders.add(Sort.Order.asc(sortStr.substring(1)));
        } else {
          orders.add(Sort.Order.asc(sortStr));
        }
      }
    }
    return PageRequest.of(criteria.getPage() - 1, criteria.getLimit(),
        Sort.by(orders).and(Sort.by("created").descending()));
  }
}
