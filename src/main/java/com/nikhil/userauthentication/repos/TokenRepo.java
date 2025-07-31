package com.nikhil.userauthentication.repos;

import com.nikhil.userauthentication.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Token, Integer>
{
    Token save(Token token);
}
