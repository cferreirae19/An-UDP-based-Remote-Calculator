import java.net.*;
import java.io.*;

public class udpcli {
    public static void main(String args[]) {
        // If the number of parameters is not equal to 3...
        if (args.length != 3) {
            // The correct usage and an example are shown
            System.err.println("Numero incorrecto de argumentos");
            System.err.println("Uso correcto: java udpcli ip_address_servidor port_servidor operacion");
            System.err.println("Ejemplo de uso: java udpcli 127.0.0.1 8000 5+7");
            // Then the program must finish execution
            return;
        }

        // Read address, port and operation from arguments introduced via CLI
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String operation = args[2];

        // A new socket is opened
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress server = InetAddress.getByName(serverAddress);

            // Convert the operation to a byte array and send it
            byte[] sendData = operation.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, server, serverPort);
            socket.send(sendPacket);

            // New byte arrray to receive data (a reply from the server)
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            // Socket timeout = 10s
            socket.setSoTimeout(10000);

            // The result is received and shown
            socket.receive(receivePacket);
            String result = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Resultado recibido del servidor: " + result);

        } catch (SocketTimeoutException e) {
            System.err.println("Ha vencido el temporizador del socket (10 segundos)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
