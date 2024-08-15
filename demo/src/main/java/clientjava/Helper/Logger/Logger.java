package clientjava.Helper.Logger;

public class Logger {
    public static void Log(LoggerLevel level, String caller, String message)
        {
            String format = String.format("[%s]::[%s]::[%s]", level,caller,message);
            System.out.println(format);
        }
}
