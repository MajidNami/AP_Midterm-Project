package Utils;

import java.io.FileWriter;

public class Logger {

    /**
     * This static method is for logging an exception in the 'Errors.log' file.
     * @param ex : The risen exception.
     */
    public static void log(Exception ex){
        StringBuilder sb = new StringBuilder('\n' + ex.toString() + '\n');
        for (StackTraceElement st : ex.getStackTrace())
            sb.append(st.toString()).append('\n');
        sb.append("------------------------------------");
        try(FileWriter fw = new FileWriter("Errors.log",true)) {
            fw.write(sb.toString());
        }catch (Exception exp){
            Logger.log(exp);
        }
    }
}
