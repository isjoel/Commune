
package com.commune.commune_backend.service;

import com.commune.commune_backend.model.User;
import com.commune.commune_backend.repository.PostRepository;
import com.commune.commune_backend.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UnverifiedAccountCleanupService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UnverifiedAccountCleanupService(
            UserRepository userRepository,
            PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void deleteExpiredUnverifiedAccounts() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);

        List<User> expiredUsers =
                userRepository.findByVerifiedFalseAndCreatedAtBefore(cutoff);

        for (User user : expiredUsers) {
            // Keep the account if it has posts linked to it.
            if (!postRepository.existsByUser(user)) {
                userRepository.delete(user);
            }
        }
    }
}
