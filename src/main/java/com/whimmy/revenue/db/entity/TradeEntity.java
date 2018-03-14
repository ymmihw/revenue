package com.whimmy.revenue.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Trade")
@NoArgsConstructor
public class TradeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String estateId;
  private String location;
  private String area;
  private String owner;
  private String ownerId;

  public TradeEntity(String[] strings) {
    estateId = strings[1];
    location = strings[2];
    area = strings[3];
    owner = strings[4];
    ownerId = strings[5];
  }
}
