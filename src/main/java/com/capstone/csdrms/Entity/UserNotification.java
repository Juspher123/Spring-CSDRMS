package com.capstone.csdrms.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserNotification {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long userNotificationId;
	 
	 @ManyToOne
	 @JoinColumn(name = "userId", nullable = false)
	  private UserEntity user;
	 
	 @ManyToOne
	 @JoinColumn(name = "notificationId", nullable = false)
	 private NotificationEntity notification;

//	 @Column(name = "is_read", nullable = false)
	 private boolean viewed = false;

	 public UserNotification() {}

	    public UserNotification(UserEntity user, NotificationEntity notification) {
	        this.user = user;
	        this.notification = notification;
	        this.viewed = false;
	    }

		

		public Long getUserNotificationId() {
			return userNotificationId;
		}

		public void setUserNotificationId(Long userNotificationId) {
			this.userNotificationId = userNotificationId;
		}

		public UserEntity getUser() {
			return user;
		}

		public void setUser(UserEntity user) {
			this.user = user;
		}

		public NotificationEntity getNotification() {
			return notification;
		}

		public void setNotification(NotificationEntity notification) {
			this.notification = notification;
		}

		public boolean isViewed() {
			return viewed;
		}

		public void setViewed(boolean viewed) {
			this.viewed = viewed;
		}

		
	    
	 
}
