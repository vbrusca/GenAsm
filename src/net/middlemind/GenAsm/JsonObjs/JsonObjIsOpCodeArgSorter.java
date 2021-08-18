package net.middlemind.GenAsm.JsonObjs;

import java.util.Comparator;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/15/2021 1:42 PM EST
 */
public class JsonObjIsOpCodeArgSorter implements Comparator<JsonObjIsOpCodeArg> {
    public enum JsonObjIsOpCodeArgSorterType {
        BIT_SERIES_ASC,
        BIT_SERIES_DSC,
        BIT_INDEX_ASC,
        BIT_INDEX_DSC,
        ARG_INDEX_ASC,
        ARG_INDEX_DSC,
    }
    
    public JsonObjIsOpCodeArgSorterType sortType = JsonObjIsOpCodeArgSorterType.ARG_INDEX_ASC;
    
    public JsonObjIsOpCodeArgSorter() {
    }
    
    public JsonObjIsOpCodeArgSorter(JsonObjIsOpCodeArgSorterType sType) {
        sortType = sType;
    }
    
    @Override
    public int compare(JsonObjIsOpCodeArg a, JsonObjIsOpCodeArg b) {
        if(sortType == JsonObjIsOpCodeArgSorterType.BIT_SERIES_ASC) {
            return (a.bit_series.bit_start - b.bit_series.bit_start);
        } else if(sortType == JsonObjIsOpCodeArgSorterType.BIT_SERIES_DSC) {
            return (b.bit_series.bit_start - a.bit_series.bit_start);
        } else if(sortType == JsonObjIsOpCodeArgSorterType.BIT_INDEX_ASC) {
            return (a.bit_index - b.bit_index);
        } else if(sortType == JsonObjIsOpCodeArgSorterType.BIT_INDEX_DSC) {
            return (b.bit_index - a.bit_index);
        } else if(sortType == JsonObjIsOpCodeArgSorterType.ARG_INDEX_DSC) {
            return (b.arg_index - a.arg_index);
        } else {
            return (a.arg_index - b.arg_index);
        }
    }  
}