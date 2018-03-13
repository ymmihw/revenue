package com.whimmy.revenue.db.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.whimmy.revenue.db.entity.TradeEntity;

public interface ITradeService {
  void save(TradeEntity entity);

  Page<TradeEntity> findByOwnerIdContaining(String ownerId, Pageable pageable);
}
