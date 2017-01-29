package com.mediacallz.server.db.config;

/**
 * Created by Mor on 1/17/2017.
 */
public interface DbConfig {

      String getHost();

      int getPort();

      String getDbName();

      String getUsername();

      String getPassword();

      int getMaxPoolSize();

      int getAcquireIncrement();

      Boolean getTestConnectionOnCheckIn();

      int getIdleConnectionTestPeriod();

      int getMaxIdleTimeExcessConnections();
}
