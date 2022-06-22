package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.ModelRegister.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken,Long> {
    VerificationToken findVerificationTokenByUserEmail(String email);
    VerificationToken findVerificationTokenByUserEmailAndToken(String email,String token);
}
