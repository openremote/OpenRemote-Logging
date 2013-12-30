/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2014, OpenRemote Inc.
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Level;
import java.text.MessageFormat;

import org.testng.Assert;
import org.testng.annotations.Test;



/**
 * Basic tests to check nothing gets messed up in the logging facade.
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */
public class LoggerTest
{


  enum TestHierarchy implements Hierarchy
  {
    TEST("test"),
    ERROR_HANDLER(TEST.getCanonicalLogHierarchyName() + ".errorhandler"),
    WARN_HANDLER(TEST.getCanonicalLogHierarchyName() + ".warnhandler"),
    INFO_HANDLER(TEST.getCanonicalLogHierarchyName() + ".infohandler"),
    DEBUG_HANDLER(TEST.getCanonicalLogHierarchyName() + ".debughandler");

    private String canonicalLogCategoryName;

    TestHierarchy(String canonicalLogCategoryName)
    {
      this.canonicalLogCategoryName = canonicalLogCategoryName;
    }

    @Override public String getCanonicalLogHierarchyName()
    {
      return canonicalLogCategoryName;
    }
  }


  /**
   * Just simply run through the method invocations to make sure nothing is badly broken.
   */
  @Test public void testErrorMethod()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    TestLogHandler handler = new TestLogHandler();

    // Add custom log handler -- don't delegate to parent handlers to avoid unneccessary
    // error logging caused by these tests...

    log.logDelegate.addHandler(handler);
    log.logDelegate.setUseParentHandlers(false);


    // Assert the last log message was recorded (at level SEVERE)...

    log.error("Test message");

    handler.assertLastLog(Level.SEVERE, "Test message");


    // Assert the last log message parameters were passed correctly...

    log.error("Test {0} message", "this");

    handler.assertLastLog(Level.SEVERE, "Test this message");


    // Assert last log message cause...

    RuntimeException re = new RuntimeException("log error testing");

    log.error("test message", re);

    handler.assertLastLog(Level.SEVERE, "test message", re);


    // Assert all args...

    re = new RuntimeException("log error testing 2");
    log.error("test {0} message with {1}", re, "error", this.getClass());

    handler.assertLastLog(Level.SEVERE, "test error message with " + this.getClass(), re);
  }


  /**
   * Just simply run through the method invocations to make sure nothing is badly broken.
   */
  @Test public void testWarnMethod()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    TestLogHandler handler = new TestLogHandler();

    log.logDelegate.addHandler(handler);

    log.warn("Test message");

    handler.assertLastLog(Level.WARNING, "Test message");

    log.warn("Test {0} message", "this");

    handler.assertLastLog(Level.WARNING, "Test this message");

    log.warn("test message", new RuntimeException("log warn testing"));

    handler.assertLastLog(Level.WARNING, "test message");

    log.warn("test {0} message with {1}", new RuntimeException("log warn testing"), "warn", this.getClass());

    handler.assertLastLog(Level.WARNING, "test warn message with " + this.getClass());
  }




  /**
   * Just simply run through the method invocations to make sure nothing is badly broken.
   */
  @Test public void testInfoMethod()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    TestLogHandler handler = new TestLogHandler();

    log.logDelegate.addHandler(handler);

    log.info("Test message");

    handler.assertLastLog(Level.INFO, "Test message");

    log.info("Test {0} message", "this");

    handler.assertLastLog(Level.INFO, "Test this message");

    log.info("test message", new RuntimeException("log info testing"));

    handler.assertLastLog(Level.INFO, "test message");

    log.info("test {0} message with {1}", new RuntimeException("log info testing"), "info", this.getClass());

    handler.assertLastLog(Level.INFO, "test info message with " + this.getClass());
  }


  /**
   * Just simply run through the method invocations to make sure nothing is badly broken.
   */
  @Test public void testDebugMethod()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    TestLogHandler handler = new TestLogHandler();

    log.logDelegate.addHandler(handler);
    log.logDelegate.setLevel(Level.FINE);
    
    log.debug("Test message");

    handler.assertLastLog(Level.FINE, "Test message");

    log.debug("Test {0} message", "this");

    handler.assertLastLog(Level.FINE, "Test this message");

    log.debug("test message", new RuntimeException("log debug testing"));

    handler.assertLastLog(Level.FINE, "test message");

    log.debug("test {0} message with {1}", new RuntimeException("log debug testing"), "debug", this.getClass());

    handler.assertLastLog(Level.FINE, "test debug message with " + this.getClass());
  }


  /**
   * TODO : trace not yet implemented
   */
  @Test public void testTraceMethod()
  {
    throw new Error("Not Yet Implemented");

  }
  

  /**
   * Test behavior with null args on logger error facade.
   */
  @Test public void testNullArgsError()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    log.error(null);

    log.error(null, new Object[] {null, null});

    log.error("Test error {0} and {1}", new Object[] {null, null});

    log.error(null, new RuntimeException("test error null args"));

    log.error(null, null, "arg");

    log.error(null, new RuntimeException("test err null args"), (Object[])null);

    log.error("error msg", (Exception)null);

    log.error(null, "error", "arg");
  }

  /**
   * Test behavior with null args on logger warn facade.
   */
  @Test public void testNullArgsWarn()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    log.warn(null);

    log.warn(null, new Object[] {null, null});

    log.warn("Test warn {0} and {1}", new Object[] {null, null});

    log.warn(null, new RuntimeException("test warn null args"));

    log.warn(null, null, "arg");

    log.warn(null, new RuntimeException("test warning null args"), (Object[])null);

    log.warn("warn msg", (Exception)null);

    log.warn(null, "warn", "arg");
  }

  /**
   * Test behavior with null args on logger info facade.
   */
  @Test public void testNullArgsInfo()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    log.info(null);

    log.info(null, new Object[] {null, null});

    log.info("Test info {0} and {1}", new Object[] {null, null});

    log.info(null, new RuntimeException("test info null args"));

    log.info(null, null, "arg");

    log.info(null, new RuntimeException("test information null args"), (Object[])null);

    log.info("info msg", (Exception)null);

    log.info(null, "info", "arg");
  }


  /**
   * Test behavior with null args on logger debug facade.
   */
  @Test public void testNullArgsDebug()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    log.debug(null);

    log.debug(null, new Object[] {null, null});

    log.debug("Test debug {0} and {1}", new Object[] {null, null});

    log.debug(null, new RuntimeException("test debug null args"));

    log.debug(null, null, "arg");

    log.debug(null, new RuntimeException("test debugging null args"), (Object[])null);

    log.debug("debug msg", (Exception)null);

    log.debug(null, "debug", "arg");
  }


  /**
   * Test behavior with null args on logger debug facade.
   */
  @Test public void testFunkyMsgFormatting()
  {
    Logger log = Logger.getInstance(TestHierarchy.TEST);

    log.error("Test error {0, date} and {1, integer, currency}", "foo", "bar");
    log.warn("Test warn {0, foo} and {1, bar}", null, 1);
    log.info("Test debug {0, number} and {1, number, percentage}", "foo", "bar");
    log.debug("Test debug {0}", "foo", "bar");
  }



  /**
   * Tests mapping of error logging to JUL levels.
   */
  @Test public void testErrorMapping()
  {
    Logger log = Logger.getInstance(TestHierarchy.ERROR_HANDLER);
    TestLogHandler handler = new TestLogHandler();

    log.logDelegate.addHandler(handler);

    String msg = "Test error {0}, {1}";

    log.error(msg, "foo", "bar");

    handler.assertLastLog(Level.SEVERE, "Test error foo, bar");
  }

  /**
   * Tests mapping of warn logging to JUL levels.
   */
  @Test public void testWarnMapping()
  {
    Logger log = Logger.getInstance(TestHierarchy.WARN_HANDLER);
    TestLogHandler handler = new TestLogHandler();

    log.logDelegate.addHandler(handler);

    String msg = "Test warn {0}, {1}";

    log.warn(msg, "foo", "bar");

    handler.assertLastLog(Level.WARNING, "Test warn foo, bar");
  }


  /**
   * Tests mapping of info logging to JUL levels.
   */
  @Test public void testInfoMapping()
  {
    Logger log = Logger.getInstance(TestHierarchy.INFO_HANDLER);
    TestLogHandler handler = new TestLogHandler();

    log.logDelegate.addHandler(handler);

    String msg = "Test info {0}, {1}";

    log.info(msg, "foo", "bar");

    handler.assertLastLog(Level.INFO, "Test info foo, bar");
  }


  /**
   * Tests mapping of debug logging to JUL levels.
   */
  @Test public void testDebugMapping()
  {
    Logger log = Logger.getInstance(TestHierarchy.DEBUG_HANDLER);
    TestLogHandler handler = new TestLogHandler();

    log.logDelegate.addHandler(handler);
    log.logDelegate.setLevel(Level.ALL);

    String msg = "Test debug {0}, {1}";

    log.debug(msg, "foo", "bar");

    handler.assertLastLog(Level.FINE, "Test debug foo, bar");
  }



  // Nested Classes -------------------------------------------------------------------------------


  private static class TestLogHandler extends Handler
  {
    private Level lastLevel;
    private String lastMessage;
    private Throwable lastCause;

    @Override public void publish(LogRecord record)
    {
      lastLevel = record.getLevel();
      lastCause = record.getThrown();

      Object[] msgParams = record.getParameters();

      if (msgParams != null && record.getMessage() != null)
      {
        try
        {
          lastMessage = MessageFormat.format(record.getMessage(), msgParams);
        }

        catch (IllegalArgumentException e)
        {
          lastMessage = record.getMessage();
        }
      }

      else
      {
        lastMessage = record.getMessage();
      }
    }

    @Override public void flush()
    {

    }

    @Override public void close()
    {

    }

    void assertLastLog(Level level, String msg)
    {

      Assert.assertTrue(
          msg.equals(lastMessage),
          "Expected log message '" + msg + "', got '" + lastMessage + "'."
      );

      Assert.assertTrue(
          level.equals(lastLevel),
          "Expected level " + level + ", got " + lastLevel
      );
    }

    void assertLastLog(Level level, String msg, Throwable t)
    {
      assertLastLog(level, msg);

      Assert.assertTrue(t.getMessage().equals(lastCause.getMessage()));
    }
  }


  // TODO test a broken handler that throws on publish()

  
}

