package MessageSystem.Processors;

import MessageSystem.MessageReader;

import java.io.FileWriter;

public class Chat implements MessageProcessor{

    private final MessageReader mr;

    public Chat(MessageReader mr){
        this.mr = mr;
    }

    @Override
    public void start() {
        System.out.println(mr.getMessage());
        try(FileWriter fw = new FileWriter("Chats.log",true)) {
            fw.write(mr.getMessage());
            fw.write("\n");
            fw.flush();
        }catch (Exception ex){
            System.out.println("Exception in Chat message processor : " + ex);
        }
    }
}
