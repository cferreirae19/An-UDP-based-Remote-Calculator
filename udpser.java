import java.net.*;
import java.io.*;

public class udpser {
    public static void main(String args[]) {
        // If the number of parameters is not equal to 2...
        if (args.length != 2) {
            // The correct usage and an example are shown
            System.err.println("Numero incorrecto de argumentos");
            System.err.println("Uso correcto: java udpser port_numero secreto");
            // Then the program must finish execution
            return;
        }
        
        // Read port and secret from arguments introduced via CLI
        int serverPort = Integer.parseInt(args[0]);
        int secret = Integer.parseInt(args[1]);

        // A new socket is opened
        try (DatagramSocket socket = new DatagramSocket(serverPort)) {

            while (true) {
                // Data sent by the client is received
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                // The operation is extracted and shown
                String operation = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Operacion recibida del cliente: " + operation);

                // We evaluate the operation (external method)
                int result = evaluateOperation(operation) + secret;

                // A reply datagram is sent to the client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                byte[] sendData = String.valueOf(result).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(sendPacket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int evaluateOperation(String operation) {
        // We divide the operation into its different parts according to the operand (sign)
        String[] parts = operation.split("[\\+\\-\\*\\/]");
        int operand1 = Integer.parseInt(parts[0]);
        char operator = operation.charAt(parts[0].length());
        int operand2 = Integer.parseInt(parts[1]);

        // Based on the operator, the corresponding operation is computed
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Operador no válido: " + operator);
        }
    }
}
