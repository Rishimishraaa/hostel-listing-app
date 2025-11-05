package com.rk.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rk.entity.Notification;
import com.rk.entity.User;
import com.rk.exception.AppException;
import com.rk.repository.NotificationRepository;
import com.rk.repository.UserRepository;
import com.rk.request.UserRegisterRequest;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Service
public class NotificationService {
	
	private final UserRepository userRepository;

	private final NotificationRepository notificationRepository;
	
	public List<Notification> getNotificationsForUser(User user) throws AppException{
		return notificationRepository.findByUserOrderByCreatedAtDesc(user);
	}
	
	
	public Notification createNotification(User user, String message)throws AppException {
		Notification notif = Notification.builder()
				.user(user)
				.message(message)
				.createdAt(LocalDateTime.now())
				.isRead(false)
				.build();
		Notification save = notificationRepository.save(notif);
		return save;
	}
	
	public Notification markAsRead(Long notificationId) throws AppException{
		Notification notif = notificationRepository.findById(notificationId).orElseThrow(() -> new AppException("Notification not found"));
		notif.setRead(true);
		return notificationRepository.save(notif);
	}
	
	public List<Notification> markAllAsRead(String email){
		User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException("user not found"));
		List<Notification> notify = notificationRepository.findByUserOrderByCreatedAtDesc(user);
		notify.forEach(n -> n.setRead(true));
		return notificationRepository.saveAll(notify);
	}
	
	
	
}
