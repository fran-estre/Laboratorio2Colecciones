package Client;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientApp {

    private static CommunicationClient communication;
    public static CommunicationClient getCommunication() {
        return communication;
    }
    private static void setCommunication(CommunicationClient communication) {
        ClientApp.communication = communication;
    }

    public static void main(String[] args) {
        String serverAddress;
        int port;
        if (args.length != 2) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the server address: ");
            serverAddress = scanner.nextLine();
            System.out.print("Enter the port: ");
            port = Integer.parseInt(scanner.nextLine());
        } else {
            serverAddress = args[0];
            port = Integer.parseInt(args[1]);
        }

        if (initializeCommunication(serverAddress, port))
            new CommandReader().readConsoleCommand();
    }

    private static Boolean initializeCommunication(String serverAddress, Integer port) {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                setCommunication(new CommunicationClient(port, serverAddress));
                return true;
            } catch (SocketException e) {
                e.printStackTrace();
                System.out.println("There was an exception while connecting to the server." + e.getMessage());
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.out.println("There was an unknown exception. " + e.getMessage());
            } catch (Exception e) {
                System.out.println("There was an generic exception. " + e.getMessage());
            }
            System.out.println("Would you like to try again (yes/no)?");
        } while (scanner.nextLine().equalsIgnoreCase("YES"));
        return false;
    }
}
