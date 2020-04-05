
import java.io.*;

public class MainAssemble {
    public static void main(String[] args)
    {
        // must specify file to assemble
        if (args.length != 1)
            Error.error("please specify a file to assemble");

        String inputFile = args[0];

        try {
            Assembler assemble = new Assembler(inputFile);
            assemble.assemblePass1();
            assemble.assemblePass2();
            assemble.close();
        } catch (FileNotFoundException ex)
        {
            Error.error("file \'" + inputFile + "\' not found");
        } catch (IOException ex)
        {
            Error.error("a i/o exception occured");
        }
    }
}
