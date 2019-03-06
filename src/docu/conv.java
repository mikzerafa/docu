package docu;

public class conv {
    conv()
    {
        
    }
    
    public String toStr(int num)
    {
        return num + "";
    }
    
    public int toInt(String str)
    {
        int output = 0;
        
        try
        {
            output = Integer.parseInt(str);
        }
        catch(Exception ex)
        {
            System.out.println("toInt-ing a non-int");
            return -1;
        }
        return output;
    }
}
