package com.nikhil.userauthentication.repos;

import com.nikhil.userauthentication.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token, Integer>
{
    Token save(Token token);

    Optional<Token> findByTokenValue(String tokenValue);


    @Modifying
    @Query("update Token t set t.isDeleted = true where t.tokenValue = :tokenValue")
    void deleteToken(@Param("tokenValue") String value);


    @Modifying
    @Query("update Token t set t.isDeleted = true where t.user.id = :userId")
    void deleteAllToken(@Param("userId") int id);
}
