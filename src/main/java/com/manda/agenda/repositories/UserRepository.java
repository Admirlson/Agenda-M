package com.manda.agenda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manda.agenda.models.User1;

public interface UserRepository extends JpaRepository<User1, Integer> {

    /**
     * @param login
     * @param password
     * @return
     */
    @Query("SELECT u FROM User1 u WHERE u.username= :username AND u.password= :password")
    public User1 userByPassword(String username, String password);

    User1 findByUsername(String username);

    @Modifying
    @Query("Update User1 u set u.password= :password WHERE u.username= :username")
    int updatePassword(@Param("password") String password, @Param("username") String username);
}
