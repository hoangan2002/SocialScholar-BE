package com.social.app.repository;

import com.social.app.model.PasswordReset;

public interface PasswordResetDAO {
    PasswordReset findByEmail(String email);
    void save( PasswordReset passwordReset);
    void delete( PasswordReset passwordReset);
    Boolean isExist(String email);

}