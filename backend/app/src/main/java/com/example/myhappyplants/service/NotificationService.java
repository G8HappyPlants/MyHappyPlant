package com.example.myhappyplants.service;


import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.entity.UserPlant;
import com.example.myhappyplants.repository.UserPlantRepository;
import com.example.myhappyplants.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final UserRepository userRepository;
    private final UserPlantRepository userPlantRepository;
    private final EmailService emailService;

    public NotificationService(UserRepository userRepository, UserPlantRepository userPlantRepository,
                               EmailService emailService) {
        this.userRepository = userRepository;
        this.userPlantRepository = userPlantRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedDelayString = "${app.notification.interval-ms:10000}")
    public void sendWaterNotification() {

        LocalDate today = LocalDate.now();

        for (User user : userRepository.findAll()) {

            if (!user.isEmailVerified()) {
                continue;
            }

            if (user.getLastNotificationSendAt() != null) {
                LocalDate lastSent = user.getLastNotificationSendAt()
                        .atZone(ZoneId.systemDefault()).toLocalDate();

                if (lastSent.equals(today)) {
                    continue;
                }
            }

            List<UserPlant> duePlants = userPlantRepository.findAllByUserAndNextWaterDateLessThanEqual(user, today);

            if (duePlants.isEmpty()) {
                continue;
            }

            try {
                emailService.sendWaterNotification(user, duePlants);
                user.setLastNotificationSendAt(Instant.now());
                userRepository.save(user);
                log.debug("Watering notification sent to {}", user.getUsername());
            } catch (Exception e) {
                log.error("Failed to send watering notification to {}: {}", user.getUsername(), e.getMessage());
            }
        }

    }


}
