package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.entity.OrderRecord;

public interface OrderRecordRepository extends JpaRepository<OrderRecord, Long> {

	List<OrderRecord> findAllByOrderByCreatedAtDesc();
}
