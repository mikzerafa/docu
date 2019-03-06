package docu;

public class location {
    
    location()
    {
        
    }
    
    public String getLocation(String pathName)
    {
        String output = "";
        //System.out.println("looking for pathName: " + pathName);
        
        switch(pathName)
        {
            case "controls":
                output = "src\\docuFile\\ctrls.txt";//docuFile\\ctrls.txt";
                break;
            case "documentation":
                output = "src\\docuFile\\documentation.txt";
                break;
            default:
                System.out.println("--path unknown--");
                break;
        }
        
        return output;
    }
}
