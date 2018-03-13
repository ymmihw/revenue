package com.whimmy.revenue.db.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.whimmy.revenue.db.entity.TradeEntity;

public interface ITradeRepo extends JpaRepository<TradeEntity, Long> {
  Page<TradeEntity> findByOwnerIdContaining(String ownerId, Pageable pageable);
}
