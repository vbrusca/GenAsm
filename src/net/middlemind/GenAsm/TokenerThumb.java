package net.middlemind.GenAsm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:39 PM EST
 */
public class TokenerThumb implements Tokener {

    @Override
    public TokenLine LineTokenize(ArtifactLine line, int lineNum, JsonObj entryTypes) {
        JsonObjIsEntryTypes types = (JsonObjIsEntryTypes)entryTypes;
        
        for(Artifact art : line.payload) {
            boolean found = false;
            String payload = art.payload;
            
            //Find NON-group matches
            for(JsonObjIsEntryType typeNgrp : types.is_entry_types) {
                boolean lfound = false;
                int withStartsLen = 0;
                int withEndsLen = 0;
                String payloadContains = "";
                
                //Check for starting string match
                for(String withStarts : typeNgrp.txt_match.starts_with) {
                    withStartsLen = withStarts.length();
                    if(payload.indexOf(withStarts) == 0) {
                        lfound = true;
                        break;
                    }
                }
                
                //Check for ending string match
                if(lfound) {
                    lfound = false;
                    for(String withEnds : typeNgrp.txt_match.ends_with) {
                        withEndsLen = withEnds.length();
                        if(payload.indexOf(withEnds) == (payload.length() - 1 - withEndsLen)) {
                            lfound = true;
                            break;
                        }
                    }
                }
                
                //Check for containing string match
                if(lfound) {
                    lfound = false;
                    payloadContains = payload.substring(withStartsLen, (payload.length() - withEndsLen));
                    for(String withContains : typeNgrp.txt_match.contains) {
                        if(withContains.equals(JsonObjTxtMatch.special_wild_card)) {
                            //Match anything
                            
                        } else if(withContains.equals(JsonObjTxtMatch.special_end_line)) {
                            //Match system end line
                            
                        } else if(withContains.contains(JsonObjTxtMatch.special_range)) {
                            if(Character.isDigit(withContains.charAt(0))) {
                                //Found numeric range
                                
                            } else if(Character.isLowerCase(withContains.charAt(0))) {
                                //Found lower case character range
                                
                            } else if(Character.isUpperCase(withContains.charAt(0))) {                                
                                //Found upper case character range
                                
                            }
                        }
                    }                    
                }
                
            }
            
            //Find group matches
            for(JsonObjIsEntryType typeGrp : types.is_entry_group_types) {
                
            }            
        }
    }

    @Override
    public List<TokenLine> FileTokenize(List<ArtifactLine> file, JsonObj entryTypes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
