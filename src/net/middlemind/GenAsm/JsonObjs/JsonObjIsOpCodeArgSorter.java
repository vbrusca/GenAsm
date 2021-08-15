package net.middlemind.GenAsm.JsonObjs;

import java.util.Comparator;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/15/2021 1:42 PM EST
 */
public class JsonObjIsOpCodeArgSorter implements Comparator<JsonObjIsOpCodeArg> {
    @Override
    public int compare(JsonObjIsOpCodeArg a, JsonObjIsOpCodeArg b)
    {
        return (a.bit_series.bit_start - b.bit_series.bit_start);
    }  
}