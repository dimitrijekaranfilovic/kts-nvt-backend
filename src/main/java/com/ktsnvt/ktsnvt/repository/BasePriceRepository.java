package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.BasePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface BasePriceRepository extends JpaRepository<BasePrice, Integer> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "select bp from BasePrice bp where bp.inventoryItem.id = :id and bp.endDate is null ")
    Optional<BasePrice> findActiveForInventoryItem(Integer id);

}
