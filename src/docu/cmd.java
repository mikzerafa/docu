package docu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class cmd {
    BufferedReader br;
    
    cmd()
    {
        br = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public String in() throws IOException
    {
        return br.readLine();
    }
    
    public void out(String text)
    {
        System.out.println(text);
    }
}
