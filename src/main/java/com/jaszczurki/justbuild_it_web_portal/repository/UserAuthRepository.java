package com.jaszczurki.justbuild_it_web_portal.repository;

import com.jaszczurki.justbuild_it_web_portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<User, Long> {

    User save(User newUser);

    @Query("SELECT u FROM User u JOIN FETCH u.authorities WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);
}
