package docu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class file {

    private File file;
    
    file(String path) throws FileNotFoundException, IOException
    {
        this.file = new File(path);
        
        
    }
    
    public String getText() throws IOException
    {
        //System.out.println("getting text");
        String output = "";
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(this.file));
        while((line = br.readLine()) != null)
        {
            
           //System.out.println("found text: "+ line);
            output = output + line + "\r\n";
            
        }
        
        return output;
    }
    
    public void add(String text) throws IOException
    {
        String current = getText();
        PrintWriter pr = new PrintWriter(new FileWriter(this.file));
        pr.flush();
        pr.write(current + text + "\r\n");
        pr.close();
        //System.out.println("Saving text: " + current + text);
        
        //System.out.println("new file text: " + this.getText());
    }
    
    
}
