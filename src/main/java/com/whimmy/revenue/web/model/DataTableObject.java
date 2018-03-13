package com.whimmy.revenue.web.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTableObject<T> {

  private long recordsTotal;
  private long recordsFiltered;
  private List<T> data;

}
