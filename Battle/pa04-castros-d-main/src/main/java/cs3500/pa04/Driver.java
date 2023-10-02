package cs3500.pa04;

import cs3500.pa04.Controller.BattleSalvoController;
import cs3500.pa04.Controller.Controller;
import cs3500.pa04.Controller.ProxyController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintStream;


/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) throws IOException {
    // System.out.println("Hello from Battle Salvo - PA03 Template Repo");
    //runs battle salvo user vs AI when no command line arguments are provided

    if (args.length == 2) {
      String host = args[0];int port = Integer.parseInt(args[1]);
      Driver.runClient(host, port);
    }


    if (args.length == 0) {

      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      BattleSalvoController controller = new BattleSalvoController(input, output);
      controller.runGame();
    }
  }



  private static void runClient(String host, int port)
      throws IOException, IllegalStateException {
    //making instances to run the battle salvo controller
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    BattleSalvoController  battleSalvoController = new BattleSalvoController(input, output);

    Socket server = new Socket(host, port);
    Controller proxyController = new ProxyController(server, battleSalvoController) ;
    proxyController.runGame();

  }

}
