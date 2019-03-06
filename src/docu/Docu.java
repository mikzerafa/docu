package docu;

import java.io.IOException;

public class Docu {

    public static void main(String[] args) throws IOException 
    {
        cmd cmd = new cmd();
        conv conv = new conv();
        
        ctrl ctrl = new ctrl();
        
        System.out.println("Welcome to DOCU!");
        
        while(true)
        {
            printCtrls();
            String input = cmd.in();
            
            if(input.startsWith("add"))
            {
                ctrl.add(input);
            }
            else if(input.startsWith("doc"))
            {
                ctrl.doc(input);
            }
            else if(input.startsWith("conv"))
            {
                String converted = ctrl.conv(input);
                System.out.println(converted);
            }
            
            else if(input.startsWith("ctrls"))
            {
                ctrl.printCtrls();
            }
            else if(input.startsWith("exit"))
            {
                System.exit(0);
            }
            else
            {
                System.out.println("unknown command entered");
            }
        }
        
        
        
        
        
        
    }
    
    public static void printCtrls()
    {
        System.out.println("\nEnter: add <input> , doc <input> , conv <input> , ctrls or exit");
    }
    
}
