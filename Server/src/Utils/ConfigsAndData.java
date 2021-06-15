package Utils;

public class ConfigsAndData {

    private int port = 27960;
    private int playersNumber = 10;
    private static final ConfigsAndData instance = new ConfigsAndData();

    private ConfigsAndData(){}

    /**
     * @return The only instance of this class.
     */
    public static ConfigsAndData getInstance(){
        return instance;
    }

    /**
     * @return The port that server runs on.
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the server's port.
     * @param port : The servers port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return The maximum capacity of the server.
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * Sets the maximum capacity of the server.
     * @param playersNumber : Number os players.
     */
    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }
}
