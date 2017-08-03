package de.dk.bininja.admin.ui.cli;

import java.io.IOException;
import java.util.Collection;

import de.dk.bininja.admin.ui.UIController;
import de.dk.bininja.net.ConnectionDetails;
import de.dk.bininja.net.ConnectionType;

public class ReadConnectionsCommand extends CliCommand {
   private static final String NAME = "read-connection-details";
   private static final String REGEX = "^" + NAME + "( (" + CountConnectionsCommand.connectionTypesWithPipes() + "))?$";

   public ReadConnectionsCommand() {
      super(NAME, REGEX);
   }

   @Override
   protected CliCommandResult checkedExecute(String input, UIController controller) throws IOException,
                                                                                           InterruptedException {
      ConnectionType type;
      try {
         type = CountConnectionsCommand.extractType(input, name);
      } catch (IllegalArgumentException e) {
         return new CliCommandResult(false, e.getMessage());
      }

      Collection<ConnectionDetails> result = controller.readConnectionDetailsOf(type);
      String typeString = type == ConnectionType.ALL ? "" : (type.getString() + " ");
      String output = "Total " + typeString + "connections: " + result.size() + "\n";
      for (ConnectionDetails connectionDetails : result)
         output += connectionDetails.toString() + "\n";

      return new CliCommandResult(true, output);
   }

   @Override
   public void printUsage() {
      System.out.println(NAME + " [" + CountConnectionsCommand.connectionTypesWithPipes() + "]");
      System.out.println("Read the connection details of every connection of a specified type (or all) from the server.");
      System.out.println("If no connection type is specified all connections will be read.");
   }

}
