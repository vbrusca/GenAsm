package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 6:46 AM EST
 */
public interface JsonObj {
    public String GetLoader();
    public void SetLoader(String s);
    public String GetName();
    public void SetName(String s);    
    public String GetFileName();
    public void SetFileName(String s);
    public void Print();
    public void Print(String prefix);
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink;    
}
