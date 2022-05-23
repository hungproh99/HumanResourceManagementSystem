package com.csproject.hrm.services;

import com.csproject.hrm.dto.request.ChangePasswordRequest;
import com.csproject.hrm.dto.request.LoginRequest;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.impl.LoginServiceImpl;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.csproject.hrm.common.utils.Constants.EMAIL_VALIDATION;
import static org.passay.DictionaryRule.ERROR_CODE;

@Service
public class LoginService implements LoginServiceImpl {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    public Authentication getAuthentication(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String username = employeeRepository.findIdByCompanyEmail(email);
        if (email == null || email.isEmpty()) {
            throw new CustomParameterConstraintException("Email can't not empty");
        } else if (!email.matches(EMAIL_VALIDATION)) {
            throw new CustomParameterConstraintException("Invalid email format");
        } else if (password == null || password.isEmpty()) {
            throw new CustomParameterConstraintException("Password can't not empty");
        } else if (username == null) {
            throw new CustomDataNotFoundException("Don't have any user with " + email);
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public int changePasswordByUsername(ChangePasswordRequest changePasswordRequest) {
        String email = changePasswordRequest.getEmail();
        String old_password = changePasswordRequest.getOld_password();
        String new_password = changePasswordRequest.getNew_password();
        String re_password = changePasswordRequest.getRe_password();
        String username = employeeRepository.findIdByCompanyEmail(email);
        String password = employeeRepository.findPasswordById(username);
        String encode_new_password = passwordEncoder.encode(new_password);
        if (email == null || email.isEmpty()) {
            throw new CustomParameterConstraintException("Email can't not empty");
        } else if (!email.matches(EMAIL_VALIDATION)) {
            throw new CustomParameterConstraintException("Invalid email format");
        } else if (username == null) {
            throw new CustomDataNotFoundException("Don't have any user with " + email);
        } else if (!passwordEncoder.matches(old_password, password)) {
            throw new CustomParameterConstraintException("Old-password is wrong");
        } else if (!new_password.equals(re_password)) {
            throw new CustomParameterConstraintException("Re-password must match with new-password");
        } else if (new_password.equals(old_password)) {
            throw new CustomParameterConstraintException("New-password don't same old-password");
        }
        return employeeRepository.updatePassword(encode_new_password, username);
    }

    public int forgotPasswordByUsername(String email) {
        String id = employeeRepository.findIdByCompanyEmail(email);
        if (email == null || email.isEmpty()) {
            throw new CustomParameterConstraintException("Email can't not empty");
        } else if (!email.matches(EMAIL_VALIDATION)) {
            throw new CustomParameterConstraintException("Invalid email format");
        }
        if (id == null) {
            throw new CustomDataNotFoundException("Don't have any user with " + email);
        }
        String generatePassword = generateCommonLangPassword();
        String encodePassword = passwordEncoder.encode(generatePassword);
        System.out.println(generatePassword);
        return employeeRepository.updatePassword(encodePassword, id);
    }

    public String generateCommonLangPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule, upperCaseRule, digitRule);
        return password;
    }
}
