package MessageSystem;

import Utils.Data;
import java.io.DataOutputStream;

public class Sender {

    public void send(int messageType, String message){
        try {
            DataOutputStream dos = Data.getInstance().getDos();
            dos.writeInt(message.getBytes().length);
            dos.writeInt(messageType);
            dos.write(message.getBytes());
        }catch (Exception ex){
            System.out.println("Exception occurred!\n" + ex);
        }
    }
}
