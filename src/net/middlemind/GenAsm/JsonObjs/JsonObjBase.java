package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 8:27 AM EST
 */
public class JsonObjBase implements JsonObj {
    public String name;
    public String fileName;
    public String loader;
    
    @Override
    public String GetLoader() {
        return loader;
    }
    
    @Override
    public void SetLoader(String s) {
        loader = s;
    }    
    
    @Override
    public String GetName() {
        return name;
    }

    @Override
    public void SetName(String s) {
        name = s;
    }    
    
    @Override
    public String GetFileName() {
        return fileName;
    }
    
    @Override
    public void SetFileName(String s) {
        fileName = s;
    }    

    @Override
    public void Print() {
        Print("");
    }
    
    @Override
    public void Print(String prefix) {
        Logger.wrl(prefix + "Name: " + name);
        Logger.wrl(prefix + "FileName: " + fileName);
        Logger.wrl(prefix + "Loader: " + loader);
    }    

    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        Logger.wrl("JsonObjBase: Link: Do nothing...");
    }
}
