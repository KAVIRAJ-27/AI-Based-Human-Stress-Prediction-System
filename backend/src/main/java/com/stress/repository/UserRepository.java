package com.stress.repository;

import com.stress.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByRole(User.Role role);
    List<User> findByActive(Boolean active);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    long countUsers();
}
