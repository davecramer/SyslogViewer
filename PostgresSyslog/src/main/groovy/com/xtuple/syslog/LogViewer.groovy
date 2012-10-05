package com.xtuple.syslog

import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.http.*
import groovy.sql.Sql
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject

/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 12-10-01
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
class LogViewer {

  static Vertx vertx
  static  server

  static public void main(String []args)
  {
    vertx = Vertx.newVertx()

    new LogViewer().startServer()
    println "listening on port 8080"

    while(true)
    {
      try {
        sleep(1000)
      }
      catch(Throwable th)
      {}
    }
  }

  void startServer ()
  {
    def routeMatcher = new RouteMatcher()

    routeMatcher.get("/logs/postgresql/:file") { req ->
      def file = req.params['file']
      if ( file ==null ) file= 'index.html'
      req.response.sendFile "web/$file"

    }
    routeMatcher.getWithRegEx("/logs/postgresql/(.*)") { req ->
      def file = req.params.param0
      if ( file ==null ) file= 'index.html'
      req.response.sendFile "web/$file"

    }


    server = vertx.createHttpServer()
    server.requestHandler(routeMatcher.asClosure())

    server.websocketHandler{ ServerWebSocket ws ->
      ws.dataHandler {  data ->
        def params = data.toString().split(':')
        Sql sql = Sql.newInstance('jdbc:postgresql://localhost/syslog','test','')
        JsonArray jsonArray = new JsonArray()


          sql.eachRow('select * from log') {row ->
            JsonObject jsonObject = new JsonObject()

            jsonObject.putNumber('id', row.id)
            jsonObject.putString('date', row.date.toString())
            jsonObject.putString('facility', row.facility)
            jsonObject.putString('level', row.level)
            jsonObject.putString('message', row.message)
            jsonArray.add(jsonObject)
          }


        sql.close()
        ws.writeTextFrame(jsonArray.encode())
      }

    }.listen(8080)
  }
}
