package com.whimmy.revenue.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {
  private String ownerId;
  private int start;
  private int length;
}
