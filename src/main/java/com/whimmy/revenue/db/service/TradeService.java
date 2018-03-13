package com.whimmy.revenue.db.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.whimmy.revenue.db.entity.TradeEntity;
import com.whimmy.revenue.db.repo.ITradeRepo;

@Service
@Transactional
public class TradeService implements ITradeService {
  @Autowired
  private ITradeRepo repo;

  @Override
  public void save(TradeEntity entity) {
    repo.save(entity);
  }

  @Override
  public Page<TradeEntity> findByOwnerIdContaining(String ownerId, Pageable pageable) {
    return repo.findByOwnerIdContaining(ownerId, pageable);
  }
}
