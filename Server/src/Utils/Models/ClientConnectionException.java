package Utils.Models;

public class ClientConnectionException extends Exception{

    private final String message;

    public ClientConnectionException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String toString(){
        return "ClientConnectionException: " + this.message;
    }
}
