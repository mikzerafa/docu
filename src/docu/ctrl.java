package docu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.Timer;

public class ctrl {
    ArrayList<String[]> ctrls;
    String userString = "";
    file ctrlFile;
    file docFile;
    
    ctrl() throws FileNotFoundException, IOException
    {
        ctrls = new ArrayList();
        location location = new location();
        paths.avail controls = paths.avail.controls;
        paths.avail documentation = paths.avail.documentation;
        
        
        String ctrlFilepth = location.getLocation(controls.name());
        String docFilepth = location.getLocation(documentation.name());
        
        ctrlFile = new file(ctrlFilepth);
        docFile = new file(docFilepth);
        
        loadLiveCtrls();
        loadCtrls();
    }
    
    public void printCtrls()
    {
        for(int index = 0; index < ctrls.size(); index++)
        {
            System.out.println("ctrl#" + index + ":");
            System.out.println("code: " + ctrls.get(index)[0]);
            System.out.println("result: " + ctrls.get(index)[1]);
        }
    }
    
    public String run(String cmd) throws IOException //liveControls
    {
        //System.out.println("running live cmd: "+ cmd);
        String output = ""; // not necessarily used;
        switch(cmd)
        {
            case "-T": //prints and saves in string
                System.out.println("loading time");
                //Format
                DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                     .withLocale( Locale.ITALY )
                     .withZone( ZoneId.systemDefault() );
                
                Instant now = Instant.now();
                String strTime = format.format(now);
                System.out.println(strTime);
                output = strTime;
            break;
            
            default:
                System.out.println("live code: " + cmd + " not found");
                break;
        }
        
        return output;
    }
    
    public void add(String input) throws IOException
    {
        String inp = input.substring(("add").length(), input.length());
        inp = noStartSpace(inp);
        String[] ctr = inp.split(" ");
        
        if(ctr.length > 2)
        {
            for(int index = 2; index< ctr.length; index++)
            {
                ctr[1] = ctr[1] + " " + ctr[index];
            }
        }
        
        this.addctrl(ctr[0], ctr[1]);
        this.saveCtrl(ctr);
        
        System.out.println("control added");
    }
    
    public String noStartSpace(String input)
    {
        String inp = input;
        while(inp.charAt(0) == ' ')
        {
            inp = inp.substring(1, inp.length());
        }
        
        return inp;
    }
    
    public void doc(String input) throws IOException
    {
        String inp = input.substring(("doc").length(), input.length());
        inp = noStartSpace(inp);
        while(inp.contains("-"))
        {
            //resolve codes
            String code = nextCode(inp);
            System.out.println("running code: " + code);
            Boolean live = isLive(code);
            String ans = "";
            
            if(live)
            {
                System.out.println("running live code: " + code);
                ans = run(code);
                System.out.println("live output: " + ans);
            }
            else
            {
                ans = conv("conv " + code);
                if(ans.isEmpty()) // Error
                {
                    System.out.println("Code not found: " + code + " replace? y/n");
                    cmd cmd = new cmd();
                    String reply = cmd.in();
                    
                    if(reply.toLowerCase().equals("y"))
                    {
                        System.out.println("enter new code:");
                        ans = cmd.in();
                    }
                    else
                    {
                        ans = "";
                    }
                }
            }
            
            inp = replace(inp, code, ans);
            
            System.out.println("new input: " + inp);
            
            
        }
        
        docFile.add(inp);
        System.out.println("documented: " + inp);
    }
    
    public String nextCode(String input)
    {
        String output = "";
        int codeIndex = input.indexOf("-");
        String after = input.substring(codeIndex, input.length());
        output = after;
        
        if(after.contains(" "))
        {
            output = after.substring(0, after.indexOf(" "));
        }
        
        return output;
    }
    

    
    public String conv(String input) throws IOException
    {
        
        String inp = input.substring(("conv").length(), input.length());
        inp = noStartSpace(inp);
        //System.out.println("converting ("+ inp + ")...");
        return convCode(inp);
    }
    
    public Boolean isLive(String code)
    {
        Boolean output = false;
        String test = code.toUpperCase();
        
        if(test.equals(code))
        {
            output = true;
        }
        
        return output;
    }
    
    public void loadLiveCtrls() //Live controls Capital 
    {
        String[] timeCtrl = new String[2];
        timeCtrl[0] = "-T";
        timeCtrl[1] = "TIME";
        
        this.ctrls.add(timeCtrl);
    }
    
    public void loadCtrls() throws IOException
    {
        String ctrlFileText = ctrlFile.getText();
        
        if(ctrlFile.getText().contains("\n"))
        {
            String[] knownCtrls = ctrlFile.getText().split("\n");
        
            for(int index = 0 ; index < knownCtrls.length; index++)
            {
                String[] newctrl = new String[2];
                String code = knownCtrls[index].substring(0, knownCtrls[index].indexOf(":"));
                String actual = knownCtrls[index].substring(knownCtrls[index].indexOf(":") +1, knownCtrls[index].length());
                newctrl[0] = code;
                newctrl[1] = actual;

                this.ctrls.add(newctrl);
            }
        }
    }
    
    public void saveCtrl(String[] ctrl) throws IOException
    {
        ctrlFile.add(ctrl[0] +  ":" + ctrl[1]);
    }
    

    

    
    public void addctrl(String code, String actual) throws IOException
    {
        String[] control = new String[2];
        control[0] = code;
        control[1] = actual;
        
        //System.out.println("new Code: " + code);
        //System.out.println("output: " + actual);
        ctrls.add(control);
        
    }   
    
    public String convCode(String code) throws IOException //-u -> unity -n -> netbeans -re -> research
    {
        String error = "code not find " + code + " in database: Use addctrl to add a new control";
        String output = "";
        
        if(code.toUpperCase().equals(code)) // live code
        {
            output = run(code);
        }
        
        else
        {
            int codeIndex = findCode(code);
        
            if(codeIndex >= 0) //-1 = unavailable
            {
                output = getActual(codeIndex);
            }
            else
            {
                System.out.println(error);
            }
        }
        
        
        
        return output;
        
    }
    
    public String getActual(int codeIndex)
    {
        return ctrls.get(codeIndex)[1];
    }
    
    public int findCode(String code)
    {
        int output = -1;
        
        for(int index = 0; index < ctrls.size(); index++)
        {
            if(ctrls.get(index)[0].equals(code))
            {
                output = index;
                index = ctrls.size();
            }
        }
        
        return output;
    }
    
    public String replace(String input, String code, String with)
    {
        String output = "";
        String before = "";
        String after = "";
        
        int codeIndex = input.indexOf(code);
        int codeEndIndex = codeIndex + code.length();
        
        if(codeIndex > 0)
        {
            before = input.substring(0, codeIndex);
        }
        
        output = before + with;
        
        if(codeEndIndex < input.length())
        {
            after = input.substring(codeEndIndex, input.length());
        }
        
        output = output + after;
        
        return output;
        
    }

    public ArrayList<String[]> getCtrls() {
        return ctrls;
    }

    public void setCtrls(ArrayList<String[]> ctrls) {
        this.ctrls = ctrls;
    }
    
    
}
