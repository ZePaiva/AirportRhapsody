package Rhapsody.client.communications;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Client communication channel data type Uses TCP sockets and object-based data
 * transfer
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */

public class ClientCom {

    /**
     * Communication socket
     * 
     * @serialField sock
     */
    private Socket sock = null;

    /**
     * Socket hostname
     * 
     * @serialField host
     */
    private String host = null;

    /**
     * Socket port
     * 
     * @serialField port
     */
    private int port;

    /**
     * Communication input
     * 
     * @serialField input
     */
    private ObjectInputStream input = null;

    /**
     * Communication output
     * 
     * @serialField output
     */
    private ObjectOutputStream output = null;

    /**
     * ClientCom constructor method
     * 
     * @param host system hostname
     * @param port socket target port number
     */
    public ClientCom(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Client communication channel opening
     * 
     * @return true if it is successfull, otherwise false
     */
    public boolean open() {

        boolean success = true;
        SocketAddress socketAddress = new InetSocketAddress(host, port);

        try {
            sock = new Socket();
            sock.connect(socketAddress);
        } catch (UnknownHostException e) {
            System.err.printf("%s hostname %s unknown ", Thread.currentThread().getName(), host);
            e.printStackTrace();
            System.exit(1);
        } catch (NoRouteToHostException e) {
            System.err.printf("%s hostname %s unreachable ", Thread.currentThread().getName(), host);
            e.printStackTrace();
            System.exit(1);
        } catch (ConnectException e) {
            System.err.printf("%s hostname %s not respondindg ", Thread.currentThread().getName(), host);
            if (e.getMessage().equals("Connection refused (Connection refused)")) {
                success = false;
            } else {
                e.printStackTrace();
                System.exit(1);    
            }
        } catch (SocketTimeoutException e) {
            System.err.printf("%s socket timout ", Thread.currentThread().getName());
            success=false;
        } catch (IOException e) {
            System.err.printf("%s unknown IO error ", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }

        if (!success) return success;

        try { 
            output = new ObjectOutputStream (sock.getOutputStream());
        } catch (IOException e) { 
            System.err.printf ("%s socket output channel unable to open\n", Thread.currentThread().getName());
            e.printStackTrace ();
            System.exit (1);
        }

        try { 
            input = new ObjectInputStream (sock.getInputStream());
        } catch (IOException e) { 
            System.err.printf ("%s socket input channel unable to open\n", Thread.currentThread().getName());
            e.printStackTrace ();
            System.exit (1);
        }

        return success;
    }
    
    /**
     * Client closing method
     */
    public void close() {

        // close input data stream
        try {
            input.close();
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }
        
        // close output data stream
        try {
            output.close();
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }

        // close socket 
        try {
            sock.close();
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Client data stream read method
     * 
     * @return serverObject
     */
    public Object readObject () {
        Object serverObject = null;

        try {
            serverObject = input.readObject();
        } catch (final InvalidClassException e) {
            System.err.printf("%s can't deserialize data\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        } catch (final ClassNotFoundException e) {
            System.err.printf("%s wrong data type\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }

        return serverObject;
    }

    /**
     * Client data stream write method
     * 
     * @param clientObject
     */
    public void writeObject(final Object clientObject) {
        try {
            output.writeObject(clientObject);
        } catch (final InvalidClassException e) {
            System.err.printf("%s can't serialize class\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }  catch (final NotSerializableException e) {
            System.err.printf("%s non-serializable data type\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }
    }
}