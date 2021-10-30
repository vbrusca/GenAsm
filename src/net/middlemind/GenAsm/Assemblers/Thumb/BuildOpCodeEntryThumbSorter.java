package net.middlemind.GenAsm.Assemblers.Thumb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A class used to sort a list of BuildOpCodeThumb entries. BuildOpCodeThumb entries are used to finalize the binary code of an assembly source line. 
 * @author Victor G. Brusca, Middlemind Games 08/15/2021 1:42 PM EST
 */
public class BuildOpCodeEntryThumbSorter implements Comparator<BuildOpCodeThumb> {
    /**
     * An enumeration representing the two sort types supported by this class.
     */
    public enum BuildOpCodeEntryThumbSorterType {
        BIT_SERIES_ASC,
        BIT_SERIES_DSC
    }
    
    /**
     * An instance of the sort type enumeration used to drive this class' sorting logic.
     */
    public BuildOpCodeEntryThumbSorterType sortType = BuildOpCodeEntryThumbSorterType.BIT_SERIES_DSC;
    
    /**
     * A generic constructor that takes no arguments.
     */
    public BuildOpCodeEntryThumbSorter() {
    }
    
    /**
     * A constructor that takes a sort type argument.
     * @param sType 
     */
    public BuildOpCodeEntryThumbSorter(BuildOpCodeEntryThumbSorterType sType) {
        sortType = sType;
    }
    
    /**
     * A method that removes any entries that do not need a binary representation, hence no bitSeries data.
     * @param buildEntries      The list of build entries to process.
     * @return                  A Boolean value that indicates if the cleaning process completed successfully.
     */
    public boolean Clean(List<BuildOpCodeThumb> buildEntries) {
        List<BuildOpCodeThumb> clearEntries = new ArrayList<>();
        for(BuildOpCodeThumb entry : buildEntries) {
            if(entry.bitSeries == null) {
                clearEntries.add(entry);
            }
        }
        return buildEntries.removeAll(clearEntries);
    }
    
    /**
     * The comparison method used by the Array sort class when sorting a BuildOpCodeThumb list. 
     * @param a     A BuildOpCodeThumb entry to compare.
     * @param b     A BuildOpCodeThumb entry to compare.
     * @return      An integer value representing the relationship between parameters a and b.
     */
    @Override
    public int compare(BuildOpCodeThumb a, BuildOpCodeThumb b) {
        if(sortType == BuildOpCodeEntryThumbSorterType.BIT_SERIES_DSC) {
            return (b.bitSeries.bit_start - a.bitSeries.bit_start);
        } else {
            return (a.bitSeries.bit_start - b.bitSeries.bit_start);
        }
    }  
}