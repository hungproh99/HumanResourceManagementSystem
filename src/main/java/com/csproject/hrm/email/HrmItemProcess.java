package com.csproject.hrm.email;

import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.request.HrmPojo;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static com.csproject.hrm.common.constant.Constants.*;

public class HrmItemProcess implements ItemProcessor<HrmPojo, MimeMessage> {
  @Autowired private JavaMailSender mailSender;
  @Autowired private GeneralFunction generalFunction;
  private String sender;
  private String attachment;

  public HrmItemProcess(String sender, String attachment) {
    this.sender = sender;
    this.attachment = attachment;
  }

  @Override
  public MimeMessage process(HrmPojo hrmPojo) throws Exception {

    return generalFunction.sendEmail(
        hrmPojo.getEmployeeId(),
        hrmPojo.getPassword(),
        FROM_EMAIL,
        TO_EMAIL,
        SEND_PASSWORD_SUBJECT,
        null);
  }
}
