package Utils;

public class ServerAddress {

    private final String host;
    private final int port;

    /**
     * This class contains the server information.
     * @param host : The server host address.
     * @param port : The server port.
     */
    public ServerAddress(String host, int port){
        this.host = host;
        this.port = port;
    }

    /**
     * @return The server's host address.
     */
    public String getHost(){
        return this.host;
    }

    /**
     * @return The server's port.
     */
    public int getPort(){
        return this.port;
    }
}
