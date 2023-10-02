package cs3500.pa04.Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;

/**
 * Class used for testing of proxy controller
 */
public class Mocket extends Socket {
  //look into the internal state of the socket to see what we need to inject
  private final InputStream testInputs;
  private final ByteArrayOutputStream testLog;

  /**
   * Constructor used for initializing
   *
   * @param testLog the test log
   * @param toSend string to send
   */
  public Mocket(ByteArrayOutputStream testLog, List<String> toSend) {
    this.testLog = testLog;

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    for (String message : toSend) {
      printWriter.println(message);
    }
    this.testInputs = new ByteArrayInputStream(stringWriter.toString().getBytes());


  }

  //now this is the overriding of the methods to return what we want
  @Override
  public InputStream getInputStream() {
    return this.testInputs;
  }

  //what we pass in
  @Override
  public OutputStream getOutputStream() {
    return this.testLog;
  }
  //what our local server responds is appropiate
}


