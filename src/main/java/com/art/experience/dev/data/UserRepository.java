package com.art.experience.dev.data;

import com.art.experience.dev.model.User;
import org.hibernate.type.StringType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(final String username);

    @Query(value = "SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") final String username, @Param("email") final String email);

    Optional<User> findByEmail(final String email);

    @Query(value = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByEmailAndPassword(@Param("email") final String email, @Param("password") final String password);

    @Query(value = "SELECT u FROM User u WHERE u.socialNumber = :socialNumber AND u.password = :password")
    Optional<User> findBySocialNumberAndPassword(@Param("socialNumber") final Long socialNumber, @Param("password") final String password);

    @Query(value = "SELECT MAX(social_number) FROM users u", nativeQuery = true)
    Long getLatestSocialNumber();
}
