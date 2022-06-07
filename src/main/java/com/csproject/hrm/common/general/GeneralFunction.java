package com.csproject.hrm.common.general;

import com.csproject.hrm.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.passay.DictionaryRule.ERROR_CODE;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralFunction {
  @Autowired public JavaMailSender emailSender;
  @Autowired EmployeeRepository employeeRepository;

  public String generateEmailEmployee(String id) {
    return id + DOMAIN_EMAIL;
  }

  public String generateIdEmployee(String fullName) {
    String[] splitSpace = fullName.split("\\s+");
    StringBuilder standForName = new StringBuilder(splitSpace[splitSpace.length - 1]);
    for (int index = 0; index < splitSpace.length - 1; index++) {
      standForName.append(splitSpace[index].charAt(ZERO_NUMBER));
    }
    int count = employeeRepository.countEmployeeSameStartName(standForName.toString());
    return standForName.toString() + (count + 1);
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

    CharacterData specialChars =
        new CharacterData() {
          public String getErrorCode() {
            return ERROR_CODE;
          }

          public String getCharacters() {
            return SPECIAL_CHARACTER;
          }
        };
    CharacterRule splCharRule = new CharacterRule(specialChars);
    splCharRule.setNumberOfCharacters(2);

    return gen.generatePassword(10, splCharRule, lowerCaseRule, upperCaseRule, digitRule);
  }

  public void sendEmail(String from, String to, String subject, String text) {
    MimeMessage message = emailSender.createMimeMessage();

    boolean multipart = true;

    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
      message.setContent(text, "text/html");
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);
      emailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
