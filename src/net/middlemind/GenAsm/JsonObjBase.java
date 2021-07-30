package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 8:27 AM EST
 */
public class JsonObjBase implements JsonObj {
    public String name;
    public String fileName;
    
    @Override
    public String GetName() {
        return name;
    }

    @Override
    public String GetFileName() {
        return fileName;
    }        

    @Override
    public void Print() {
        Logger.wrl("Name: " + name + " FileName: " + fileName);
    }
    
    @Override
    public void Print(String prefix) {
        Logger.wrl(prefix + "Name: " + name + " FileName: " + fileName);
    }    
}
