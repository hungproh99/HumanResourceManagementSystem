package com.csproject.hrm.jooq;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Data
public class DBConnection {
  Connection connection;

  public DBConnection(Connection connection) {
    this.connection = connection;
  }

  public DBConnection() {
    try {
      connection =
          DriverManager.getConnection(
              "jdbc:mysql://awseb-e-3tmtipzvke-stack-awsebrdsdatabase-eqfxaihqukay.cmdnwkpbg63s.us-west-2.rds.amazonaws.com:3306/human_resource_management?autoReconnect=true",
              "root",
              "Chuate1visao");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}