package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.Comparator;

/**
 * A class used to sort instruction set op-code arguments.
 * @author Victor G. Brusca, Middlemind Games 08/15/2021 1:42 PM EST
 */
public class JsonObjIsOpCodeArgSorter implements Comparator<JsonObjIsOpCodeArg> {
    /**
     * An enumeration the represents a number of different sorting types supported by this comparator implementation.
     */
    public enum JsonObjIsOpCodeArgSorterType {
        BIT_SERIES_ASC,
        BIT_SERIES_DSC,
        BIT_INDEX_ASC,
        BIT_INDEX_DSC,
        ARG_INDEX_ASC,
        ARG_INDEX_DSC,
    }
    
    /**
     * An instance of the argument sorter type that dictates what type of sort this comparator implementation.
     */    
    public JsonObjIsOpCodeArgSorterType sortType = JsonObjIsOpCodeArgSorterType.ARG_INDEX_ASC;

    /**
     * A simple class constructor.
     * This constructor uses the default sort type.
     */    
    public JsonObjIsOpCodeArgSorter() {
    }
    
    /**
     * A class constructor that takes an op-code argument sorter as an argument.
     */    
    public JsonObjIsOpCodeArgSorter(JsonObjIsOpCodeArgSorterType sType) {
        sortType = sType;
    }
    
    /**
     * A comparison method used to sort two instruction set op-code arguments by the class specified sort type.
     */    
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