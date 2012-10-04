package com.xtuple.syslog
/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 12-09-26
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */


import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.SyslogServerSessionEventHandlerIF;
import org.productivity.java.syslog4j.util.SyslogUtility
import groovy.sql.Sql
import org.apache.log4j.Logger
import java.text.SimpleDateFormat;


class PostgresSyslogEventHandler implements SyslogServerSessionEventHandlerIF
{


  Logger logger = Logger.getLogger(PostgresSyslogEventHandler.class)

  Sql sql

  PostgresSyslogEventHandler(String host, String db, String user, String password)
  {
    sql = Sql.newInstance("jdbc:postgresql://localhost/${db}", user, password )
  }
  Object sessionOpened(SyslogServerIF syslogServer, SocketAddress socketAddress)
  {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  void event(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event)
  {
    try
    {
      Date eventDate =  event.getDate() == null ? new Date() :  event.getDate()
      java.sql.Date sqlDate = new java.sql.Date(eventDate.time)

      String facility = SyslogUtility.getFacilityString(event.getFacility());
      String level = SyslogUtility.getLevelString(event.getLevel());


      sql.execute("insert into log (date,facility,level,message) values ($sqlDate, $facility,$level, ${event.getMessage()} )" )
    }
    catch (Exception ex)
    {
      logger.debug "error inserting data"
    }
  }

  void exception(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  void sessionClosed(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, boolean timeout)
  {
    try
    {
      sql.close()
    }
    finally
    {}
  }

  void initialize(SyslogServerIF syslogServer)
  {
    return
  }

  void destroy(SyslogServerIF syslogServer)
  {
    try
    {
      sql.close()
    }
    finally
    {}
  }
}
