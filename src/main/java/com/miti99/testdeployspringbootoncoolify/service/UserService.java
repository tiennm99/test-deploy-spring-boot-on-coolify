package com.miti99.testdeployspringbootoncoolify.service;

import com.miti99.testdeployspringbootoncoolify.entity.User;
import com.miti99.testdeployspringbootoncoolify.repository.UserRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final String USER_CACHE_KEY = "USER_";

    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        redisTemplate.opsForValue().set(USER_CACHE_KEY + savedUser.getId(), savedUser.toString(), 10, TimeUnit.MINUTES);
        return savedUser;
    }

    public Optional<User> getUser(String id) {
        User user = (User) redisTemplate.opsForValue().get(USER_CACHE_KEY + id);
        if (user == null) {
            Optional<User> userFromDb = userRepository.findById(id);
            userFromDb.ifPresent(value -> redisTemplate.opsForValue().set(USER_CACHE_KEY + id, value, 10, TimeUnit.MINUTES));
            return userFromDb;
        }
        return Optional.of(user);
    }

    public User updateUser(String id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            User savedUser = userRepository.save(updatedUser);
            redisTemplate.opsForValue().set(USER_CACHE_KEY + savedUser.getId(), savedUser, 10, TimeUnit.MINUTES);
            return savedUser;
        }
        return null;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
        redisTemplate.delete(USER_CACHE_KEY + id);
    }
}
