package com.csproject.hrm.email.config;

import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.email.HrmItemProcess;
import com.csproject.hrm.email.MailBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.mail.internet.MimeMessage;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

  @Autowired public JobBuilderFactory jobBuilderFactory;

  @Autowired public StepBuilderFactory stepBuilderFactory;

  @Value("${codeurjc.batch.data}")
  public String data;

  @Value("${spring.mail.username}")
  private String sender;

  @Value("${codeurjc.batch.attachment}")
  private String attachment;

  @Value("${codeurjc.batch.notifications.email}")
  private String email;

  @Bean
  public FlatFileItemReader<HrmPojo> reader() {
    FlatFileItemReader<HrmPojo> reader = new FlatFileItemReader<>();
    reader.setResource(new FileSystemResource(data));
    reader.setLinesToSkip(1);
    reader.setLineMapper(
        new DefaultLineMapper<HrmPojo>() {
          {
            setLineTokenizer(
                new DelimitedLineTokenizer() {
                  {
                    setNames(new String[] {"fullname", "code", "email"});
                  }
                });
            setFieldSetMapper(
                new BeanWrapperFieldSetMapper<HrmPojo>() {
                  {
                    setTargetType(HrmPojo.class);
                  }
                });
          }
        });
    return reader;
  }

  @Bean
  public HrmItemProcess processor() {
    return new HrmItemProcess(sender, attachment);
  }

  @Bean
  public MailBatchItemWriter writer() {
    MailBatchItemWriter writer = new MailBatchItemWriter();
    return writer;
  }

  @Bean
  public Job importUserJob() {
    return jobBuilderFactory
        .get("importUserJob")
        .incrementer(new RunIdIncrementer())
        .flow(step1())
        .end()
        .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory
        .get("step1")
        .<HrmPojo, MimeMessage>chunk(100)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .build();
  }
}
