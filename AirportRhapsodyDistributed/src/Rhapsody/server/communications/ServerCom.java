package Rhapsody.server.communications;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Server communicaton channel data type
 * 
 * @author José Paiva
 * @author André Mourato
 */

public class ServerCom {

    /**
     * Listening socket
     * 
     * @serialField listener
     */
    private ServerSocket listener = null;

    /**
     * Communication socket
     * 
     * @serialField sock
     */
    private Socket sock = null;

    /**
     * Listening port 
     * 
     * @serialField port
     */
    private final int port;

    /**
     * Socket time out
     * 
     * @serialField timeout
     */
    private final int timeout;

    /**
     * Communication Input Data Stream
     * 
     * @serialField input 
     */
    private ObjectInputStream input = null;

    /**
     * Communication Output Data Stream
     * 
     * @serialField output
     */
    private ObjectOutputStream output = null;

    /**
     * Communication channel creation (1)
     * 
     * @param port
     */
    public ServerCom(final int port) {
     this.port = port;
     this.timeout = 0;
    }

    /**
     * Communication channel creation (2)
     * 
     * @param port
     * @param serverSocket
     */
    public ServerCom(final int port, final ServerSocket serverSocket) {
        this.port = port;
        listener = serverSocket;
        timeout = 0;
    }

    /**
     * Communication channel creation (3)
     * 
     * @param port
     * @param timeout
     */
    public ServerCom(final int port, final int timeout) {
        this.port = port;
        this.timeout = timeout;
    }

    /**
     * Communication channel creation (4)
     * 
     * @param port
     * @param timeout
     * @param serverSocket
     */
    public ServerCom(final int port, final int timeout, final ServerSocket serverSocket) {
        this.port = port;
        this.timeout = timeout;
        this.listener =  serverSocket;
    }

    /**
     * Server startup method
     */
    public void start() {
        try {
            listener = new ServerSocket(port);
            listener.setSoTimeout(timeout);
        } catch (final BindException e) {
            System.err.printf("%s not binding to port %d\n", Thread.currentThread().getName(), port);
            e.printStackTrace();
            System.exit(1);
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        } catch (final Exception e) {
            System.err.printf("%s suffered weird exception\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Server alting method
     */
    public void stop() {
        try {
            listener.close();
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        } catch (final Exception e) {
            System.err.printf("%s suffered weird exception\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Client acception method
     * 
     * @return clientConnectionSocket
     * @throws IOException
     */
    public ServerCom accept() throws IOException {
        
        ServerCom conn = new ServerCom(port, listener);

        // accept socket
        try {
            conn.sock = listener.accept();
            System.out.println("Got connection: " +conn.sock.getInetAddress().toString());
        } catch (final SocketException e) {
            System.err.printf("%s socket is closed\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        } catch (final SocketTimeoutException e) {
            //System.err.printf("%s socket timeout\n", Thread.currentThread().getName());
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        }

        // listen to client input
        try {
            conn.input = new ObjectInputStream(conn.sock.getInputStream());
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        } catch (NullPointerException e) {
            //System.err.print("Read on client - Nothing connected\n");
        }

        // write client output
        try {
            conn.output = new ObjectOutputStream(conn.sock.getOutputStream());
        } catch (final IOException e) {
            System.err.printf("%s suffered unknown IO error\n", Thread.currentThread().getName());
            e.printStackTrace();
            System.exit(1);
        } catch (NullPointerException e) {
            //System.err.print("Write on client - Nothing connected\n");
        }

        return conn;
    }

    /**
     * Closing of the server socket
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
     * Server data stream read method
     * 
     * @return clientObject
     */
    public Object readObject () {
        Object clientObject = null;

        try {
            clientObject = input.readObject();
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

        return clientObject;
    }

    /**
     * Server data stream write method
     * 
     * @param serverObject
     */
    public void writeObject(final Object serverObject) {
        try {
            output.writeObject(serverObject);
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