package com.csproject.hrm.email;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.List;

public class MailBatchItemWriter implements ItemWriter<MimeMessage> {

  @Autowired private JavaMailSender mailSender;

  @Override
  public void write(List<? extends MimeMessage> list) throws Exception {
    list.stream().forEach((message) -> mailSender.send(message));
  }
}
