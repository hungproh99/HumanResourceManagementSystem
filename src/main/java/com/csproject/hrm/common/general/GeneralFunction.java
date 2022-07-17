package com.csproject.hrm.common.general;

import com.csproject.hrm.common.enums.EPolicyType;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.repositories.PolicyRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalTime;
import java.util.*;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.passay.DictionaryRule.ERROR_CODE;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralFunction {
  @Autowired public JavaMailSender emailSender;
  @Autowired ResourceLoader resourceLoader;
  @Autowired EmployeeRepository employeeRepository;
  @Autowired PolicyRepository policyRepository;

  public String generateEmailEmployee(String id) {
    return id + DOMAIN_EMAIL;
  }

  public String generateIdEmployee(String fullName, int countList) {
    String[] splitSpace = fullName.split("\\s+");
    StringBuilder standForName = new StringBuilder(splitSpace[splitSpace.length - 1]);
    for (int index = 0; index < splitSpace.length - 1; index++) {
      standForName.append(splitSpace[index].charAt(ZERO_NUMBER));
    }
    String temp =
        Normalizer.normalize(standForName.toString(), Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    int count = employeeRepository.countEmployeeSameStartName(temp) + countList;
    return temp + (count + 1);
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

  public void sendEmailForgotPassword(
      String id, String password, String from, String to, String subject) {
    MimeMessage message = emailSender.createMimeMessage();
    Resource resource = resourceLoader.getResource("classpath:email-forgot-password.vm");
    boolean multipart = true;
    try {
      InputStream inputStream = resource.getInputStream();
      byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
      String data = new String(bdata, StandardCharsets.UTF_8);
      MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);
      message.setContent(String.format(data, id, password), "text/html");
      emailSender.send(message);
    } catch (MessagingException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendEmailForNewEmployee(
      List<HrmPojo> hrmPojos, String from, String to, String subject) {
    int size = hrmPojos.size();
    MimeMessage[] messages = new MimeMessage[size];
    Resource resource = resourceLoader.getResource("classpath:email-add-employee.vm");
    boolean multipart = true;
    try {
      InputStream inputStream = resource.getInputStream();
      byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
      String data = new String(bdata, StandardCharsets.UTF_8);
      for (int i = 0; i < size; i++) {
        messages[i] = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(messages[i], multipart, "utf-8");
        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        messages[i].setContent(
            String.format(
                data,
                hrmPojos.get(i).getFullName(),
                hrmPojos.get(i).getCompanyName(),
                hrmPojos.get(i).getPassword()),
            "text/html");
      }
      emailSender.send(messages);
    } catch (MessagingException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendEmailCreateRequest(
      String createId, String receiveId, String from, String to, String subject) {
    MimeMessage message = emailSender.createMimeMessage();
    Resource resource = resourceLoader.getResource("classpath:email-create-request.vm");
    boolean multipart = true;
    try {
      InputStream inputStream = resource.getInputStream();
      byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
      String data = new String(bdata, StandardCharsets.UTF_8);
      MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);
      message.setContent(String.format(data, receiveId, createId), "text/html");
      emailSender.send(message);
    } catch (MessagingException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendEmailRemindRequest(
      String approveName,
      String createName,
      String createDate,
      List<String> checkBy,
      String requestId,
      String from,
      String to,
      String subject) {
    MimeMessage message = emailSender.createMimeMessage();
    Resource resource = resourceLoader.getResource("classpath:email-remind-request.vm");
    boolean multipart = true;
    String paragraph = null;
    if (checkBy != null) {
      String check = null;
      for (int i = 0; i < checkBy.size(); i++) {
        if (i == checkBy.size() - 1) {
          check += checkBy.get(i);
        } else {
          check += checkBy.get(i) + ", ";
        }
      }
      paragraph = "<div>It already checked by:  <strong>" + check + "</strong></div>";
    }
    try {
      InputStream inputStream = resource.getInputStream();
      byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
      String data = new String(bdata, StandardCharsets.UTF_8);
      MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);
      message.setContent(
          String.format(data, approveName, createName, createDate, paragraph, requestId),
          "text/html");
      emailSender.send(message);
    } catch (MessagingException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public WorkingTimeDataDto readWorkingTimeData() {
    WorkingTimeDataDto workingTimeDataDto = new WorkingTimeDataDto();
    Optional<PolicyDto> policyWorkingTimeDto =
        policyRepository.getPolicyDtoByPolicyType(EPolicyType.WORKING_TIME.name());
    if (policyWorkingTimeDto.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Policy working time is empty");
    }
    Set<Map.Entry<String, String>> hashMap =
        splitData(policyWorkingTimeDto.get().getData()).entrySet();

    for (Map.Entry<String, String> i : hashMap) {
      switch (i.getKey()) {
        case "Start_Time":
          workingTimeDataDto.setStartTime(LocalTime.parse(i.getValue()));
          break;
        case "End_Time":
          workingTimeDataDto.setEndTime(LocalTime.parse(i.getValue()));
          break;
        case "Punish":
          workingTimeDataDto.setListRange(splitRange(i.getValue()));
          break;
      }
    }
    return workingTimeDataDto;
  }

  public OvertimeDataDto readOvertimeData() {
    OvertimeDataDto overtimeDataDto = new OvertimeDataDto();
    Optional<PolicyDto> policyOvertimeDto =
        policyRepository.getPolicyDtoByPolicyType(EPolicyType.OT.name());
    if (policyOvertimeDto.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Policy overtime is empty");
    }
    Set<Map.Entry<String, String>> hashMap =
        splitData(policyOvertimeDto.get().getData()).entrySet();
    for (Map.Entry<String, String> i : hashMap) {
      switch (i.getKey()) {
        case "Year":
          overtimeDataDto.setYear(Integer.parseInt(i.getValue()));
          break;
        case "Month":
          overtimeDataDto.setMonth(Integer.parseInt(i.getValue()));
          break;
        case "OT_Type":
          Set<Map.Entry<String, String>> map = splitData(i.getValue()).entrySet();
          List<OvertimePoint> overtimePointList = new ArrayList<>();
          for (Map.Entry<String, String> j : map) {
            overtimePointList.add(new OvertimePoint(i.getKey(), Double.parseDouble(i.getValue())));
          }
          overtimeDataDto.setOvertimePointList(overtimePointList);
          break;
      }
    }

    return overtimeDataDto;
  }

  public HashMap<String, String> splitData(String data) {
    HashMap<String, String> hashMap = new HashMap<>();
    if (!isBlank(data)) {
      String[] splitBracket = StringUtils.substringsBetween(data, "[", "]");
      for (String split : splitBracket) {
        String[] splitSeparator = split.split(SEPARATOR, TWO_NUMBER);
        if (isInvalidSplit(splitSeparator)
            || splitSeparator[ZERO_NUMBER] == null
            || splitSeparator[ONE_NUMBER] == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
        hashMap.put(splitSeparator[ZERO_NUMBER], splitSeparator[ONE_NUMBER]);
      }
    } else {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    return hashMap;
  }

  public List<RangePolicy> splitRange(String data) {
    List<RangePolicy> rangePolicyList = new ArrayList<>();
    if (!isBlank(data)) {
      String[] splitBracket = StringUtils.substringsBetween(data, "[", "]");
      for (String split : splitBracket) {
        RangePolicy rangePolicy = null;
        String[] splitSeparator = split.split(SEPARATOR, TWO_NUMBER);
        if (isInvalidSplit(splitSeparator)
            || splitSeparator[ZERO_NUMBER] == null
            || splitSeparator[ONE_NUMBER] == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
        rangePolicy.setValue(BigDecimal.valueOf(Long.parseLong(splitSeparator[ONE_NUMBER])));
        String[] splitDash = splitSeparator[ZERO_NUMBER].split(DASH_CHARACTER, TWO_NUMBER);
        if (isInvalidSplit(splitDash)
            || splitDash[ZERO_NUMBER] == null
            || splitDash[ONE_NUMBER] == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
        rangePolicy.setMin(Long.parseLong(splitDash[ZERO_NUMBER]));
        rangePolicy.setMax(Long.parseLong(splitDash[ONE_NUMBER]));
      }
    } else {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    return rangePolicyList;
  }

  private boolean isInvalidSplit(String[] split) {
    return split.length != TWO_NUMBER || isBlank(split[ZERO_NUMBER]) || isBlank(split[ONE_NUMBER]);
  }
}