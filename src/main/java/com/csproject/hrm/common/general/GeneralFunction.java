package com.csproject.hrm.common.general;

import com.csproject.hrm.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.velocity.app.VelocityEngine;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.passay.DictionaryRule.ERROR_CODE;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralFunction {
  @Autowired public JavaMailSender emailSender;
  @Autowired private VelocityEngine engine;
  @Autowired EmployeeRepository employeeRepository;

  public String generateEmailEmployee(String id) {
    return id + DOMAIN_EMAIL;
  }

  public String generateIdEmployee(String fullName, int countList) {
    String[] splitSpace = fullName.split("\\s+");
    StringBuilder standForName = new StringBuilder(splitSpace[splitSpace.length - 1]);
    for (int index = 0; index < splitSpace.length - 1; index++) {
      standForName.append(splitSpace[index].charAt(ZERO_NUMBER));
    }
    int count = employeeRepository.countEmployeeSameStartName(standForName.toString()) + countList;
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

  public MimeMessage sendEmail(
      String id, String password, String from, String to, String subject, String attachment) {
    MimeMessage message = emailSender.createMimeMessage();

    boolean multipart = true;
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("name", id);
      model.put("code", password);
      MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);
      helper.setText(
          VelocityEngineUtils.mergeTemplateIntoString(engine, "email-body.vm", "UTF-8", model),
          "text/html");
      FileSystemResource file = new FileSystemResource(attachment);
      helper.addAttachment(file.getFilename(), file);
      return message;
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
