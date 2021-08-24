package net.middlemind.GenAsm.Assemblers.Thumb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/15/2021 1:42 PM EST
 */
public class BuildOpCodeEntryThumbSorter implements Comparator<BuildOpCodeThumb> {
    public enum BuildOpCodeEntryThumbSorterType {
        BIT_SERIES_ASC,
        BIT_SERIES_DSC
    }
    
    public BuildOpCodeEntryThumbSorterType sortType = BuildOpCodeEntryThumbSorterType.BIT_SERIES_DSC;
    
    public BuildOpCodeEntryThumbSorter() {
    }
    
    public BuildOpCodeEntryThumbSorter(BuildOpCodeEntryThumbSorterType sType) {
        sortType = sType;
    }
    
    public boolean Clean(List<BuildOpCodeThumb> buildEntries) {
        List<BuildOpCodeThumb> clearEntries = new ArrayList<>();
        for(BuildOpCodeThumb entry : buildEntries) {
            if(entry.bitSeries == null) {
                clearEntries.add(entry);
            }
        }
        return buildEntries.removeAll(clearEntries);
    }
    
    @Override
    public int compare(BuildOpCodeThumb a, BuildOpCodeThumb b) {
        if(sortType == BuildOpCodeEntryThumbSorterType.BIT_SERIES_DSC) {
            return (b.bitSeries.bit_start - a.bitSeries.bit_start);
        } else {
            return (a.bitSeries.bit_start - b.bitSeries.bit_start);
        }
    }  
}