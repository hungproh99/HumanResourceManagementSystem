package com.csproject.hrm.jooq;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.*;

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
              // "jdbc:mysql://hrm-database.c3lkjkkyx1it.ap-southeast-1.rds.amazonaws.com/human_resource_management",
              //              "root", "hrmroot01");
              "jdbc:mysql://localhost:3306/human_resource_management", "ndh4899", "hungnd");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}