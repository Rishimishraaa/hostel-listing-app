package com.rk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rk.entity.Notification;
import com.rk.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
	List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
