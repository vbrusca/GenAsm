package net.middlemind.GenAsm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:39 PM EST
 */
public class TokenerThumb implements Tokener {

    @Override
    public TokenLine LineTokenize(ArtifactLine line, int lineNum, JsonObj entryTypes) throws TokenerNotFoundException {
        JsonObjIsEntryTypes types = (JsonObjIsEntryTypes)entryTypes;
        boolean found = false;
        String payload = "";
        Artifact current = null;
        String compare = null;
        JsonObjIsEntryType compareType = null;
        int count = 0;
        boolean inComment = false;
        JsonObjIsEntryType commentType = null;
        
        TokenLine ret = new TokenLine();
        ret.lineNum = lineNum;
        ret.source = line;
        ret.sourceLen = line.payload.size();
        ret.payload = new ArrayList<>();
        
        try {
            for(Artifact art : line.payload) {
                current = art;
                found = false;
                payload = art.payload;

                if(inComment) {
                    compareType = commentType;
                    found = true;
                } else {
                    //Find NON-group matches
                    for(JsonObjIsEntryType typeNgrp : types.is_entry_types) {
                        boolean lfound = false;
                        int withStartsLen = 0;
                        int withEndsLen = 0;
                        String payloadContains = "";
                        compareType = typeNgrp;

                        //Check for starting string match
                        for(String withStarts : typeNgrp.txt_match.starts_with) {
                            compare = withStarts;
                            withStartsLen = withStarts.length();
                            if(withStarts.equals(JsonObjTxtMatch.special_wild_card)) {
                                //Match anything
                                lfound = true;
                                break;
                            } else if(withStarts.equals(JsonObjTxtMatch.special_end_line)) {
                                //Adjust for system line separator
                                withStarts = System.lineSeparator();
                                withStartsLen = withStarts.length();
                                if(payload.indexOf(withStarts) == 0) {
                                    lfound = true;
                                    break;
                                }
                            } else if(withStarts.length() > 1 && withStarts.contains(JsonObjTxtMatch.special_range)) {                    
                                if(Character.isDigit(withStarts.charAt(0))) {
                                    //Found numeric range
                                    //First entry must match
                                    int[] range = Utils.GetIntsFromRange(withStarts);
                                    int j = Utils.GetIntFromChar(payload.charAt(0));
                                    if(j >= range[0] && j <= range[1]) {
                                        lfound = true;
                                        break;
                                    }

                                } else if(withStarts.equals("a-z")) {
                                    //Found lower case character range
                                    //First entry must match
                                    if(Character.isLowerCase(payload.charAt(0))) {
                                        lfound = true;
                                        break;
                                    }
                                } else if(withStarts.equals("A-Z")) {                                
                                    //Found upper case character range
                                    //First entry must match
                                    if(Character.isUpperCase(payload.charAt(0))) {
                                        lfound = true;
                                        break;
                                    }                                
                                }
                            } else {
                                if(payload.indexOf(withStarts) == 0) {
                                    lfound = true;
                                    break;
                                }
                            }
                        }

                        //Check for ending string match
                        if(lfound) {
                            lfound = false;
                            for(String withEnds : typeNgrp.txt_match.ends_with) {
                                compare = withEnds;
                                withEndsLen = withEnds.length();
                                if(withEnds.equals(JsonObjTxtMatch.special_wild_card)) {
                                    //Match anything
                                    lfound = true;
                                    break;
                                } else if(withEnds.equals(JsonObjTxtMatch.special_end_line)) {
                                    //Adjust for system line separator
                                    withEnds = System.lineSeparator();
                                    withEndsLen = withEnds.length();
                                    if(payload.indexOf(withEnds) == (payload.length() - 1 - withEndsLen)) {
                                        lfound = true;
                                        break;
                                    }
                                } else if(withEnds.length() > 1 && withEnds.contains(JsonObjTxtMatch.special_range)) {
                                    if(Character.isDigit(withEnds.charAt(0))) {
                                        //Found numeric range
                                        //Last entry must match
                                        int[] range = Utils.GetIntsFromRange(withEnds);
                                        int j = Utils.GetIntFromChar(payload.charAt(payload.length() - 1));                                    
                                        if(j >= range[0] && j <= range[1]) {
                                            lfound = true;
                                            break;
                                        }

                                    } else if(withEnds.equals("a-z")) {
                                        //Found lower case character range
                                        //Last entry must match
                                        if(Character.isLowerCase(payload.charAt(payload.length() - 1))) {
                                            lfound = true;
                                            break;
                                        }
                                    } else if(withEnds.equals("A-Z")) {                                
                                        //Found upper case character range
                                        //Last entry must match
                                        if(Character.isUpperCase(payload.charAt(payload.length() - 1))) {
                                            lfound = true;
                                            break;
                                        }
                                    }
                                } else {
                                    if(payload.indexOf(withEnds) == (payload.length() - withEndsLen)) {
                                        lfound = true;
                                        break;
                                    }
                                }
                            }
                        }

                        //Check for containing string match
                        if(lfound) {
                            lfound = false;
                            if(payload.length() >= withStartsLen + withEndsLen) {
                                payloadContains = payload.substring(withStartsLen, (payload.length() - withEndsLen));
                            }

                            for(String withContains : typeNgrp.txt_match.contains) {
                                compare = withContains;
                                if(withContains.equals(JsonObjTxtMatch.special_wild_card)) {
                                    //Match anything
                                    lfound = true;
                                    break;
                                } else if(withContains.equals(JsonObjTxtMatch.special_end_line)) {
                                    //Match system end line
                                    //All entries must match
                                    char[] chars = payloadContains.toCharArray();
                                    boolean llfound = true;
                                    for(char c : chars) {
                                        if((c + "").equals(System.lineSeparator())) {
                                            llfound = false;
                                            break;
                                        }
                                    }

                                    if(llfound) {
                                        lfound = true;
                                        break;
                                    }
                                } else if(withContains.length() > 1 && withContains.contains(JsonObjTxtMatch.special_range)) {
                                    if(Character.isDigit(withContains.charAt(0))) {
                                        //Found numeric range
                                        //All entries must match
                                        int[] range = Utils.GetIntsFromRange(withContains);
                                        char[] chars = payloadContains.toCharArray();
                                        boolean llfound = true;

                                        for(char c : chars) {
                                            int j = Utils.GetIntFromChar(c);
                                            if(!(j >= range[0] && j <= range[1])) {
                                                llfound = false;
                                                break;
                                            }
                                        }

                                        if(llfound) {
                                            lfound = true;
                                            break;
                                        }                                    
                                    } else if(withContains.equals("a-z")) {
                                        //Found lower case character range
                                        //All entries must match
                                        char[] chars = payloadContains.toCharArray();
                                        boolean llfound = true;
                                        for(char c : chars) {
                                            if(Character.isLowerCase(c)) {
                                                llfound = false;
                                                break;
                                            }
                                        }

                                        if(llfound) {
                                            lfound = true;
                                            break;
                                        }
                                    } else if(withContains.equals("A-Z")) {                                
                                        //Found upper case character range
                                        //All entries must match
                                        char[] chars = payloadContains.toCharArray();
                                        boolean llfound = true;
                                        for(char c : chars) {
                                            if(Character.isUpperCase(c)) {
                                                llfound = false;
                                                break;
                                            }
                                        }

                                        if(llfound) {
                                            lfound = true;
                                            break;
                                        }                                        
                                    }
                                } else {
                                    //All entries must match
                                    char[] chars = payloadContains.toCharArray();
                                    boolean llfound = true;
                                    for(char c : chars) {
                                        if((c + "").equals(withContains)) {
                                            llfound = false;
                                            break;
                                        }
                                    }

                                    if(llfound) {
                                        lfound = true;
                                        break;
                                    }                                    
                                }
                            }                    
                        }

                        if(lfound) {
                            found = true;
                            break;
                        }
                    }

                    if(!found) {
                        //Find group matches
                        for(JsonObjIsEntryType typeGrp : types.is_entry_group_types) {
                            compareType = typeGrp;
                        }
                    }
                }

                Token tmb = new Token();                
                tmb.payloadArtifact = art;
                tmb.index = count;
                tmb.lineNum = lineNum;
                tmb.payloadSource = payload;
                if(!found || compareType == null) {
                    tmb.name = "Unknown";
                    tmb.payloadType = null;
                } else {
                    tmb.name = compareType.type_name;
                    tmb.payloadType = compareType;
                }
                tmb.argTokens = new ArrayList<>();
                
                ret.payload.add(tmb);                
                count++;
                
                if(tmb.name.equals("Comment")) {
                    commentType = compareType;
                    inComment = true;
                }
            }

            return ret;
        } catch (MalformedRangeException e) {
            e.printStackTrace();
            throw new TokenerNotFoundException("Could not find token match for artifact, " + payload + ", at line number " + lineNum + " with artifact " + current.payload + " compared to " + compare);
        } catch(Exception e) {
            e.printStackTrace();
            throw new TokenerNotFoundException("Could not find token match for artifact, " + payload + ", at line number " + lineNum + " with artifact " + current.payload + " compared to " + compare);                
        }
    }

    @Override
    public List<TokenLine> FileTokenize(List<ArtifactLine> file, JsonObj entryTypes) throws TokenerNotFoundException {
        int lineNum = 0;
        ArrayList<TokenLine> ret = new ArrayList<>();
        for(ArtifactLine art : file) {
            ret.add(LineTokenize(art, lineNum, entryTypes));
            lineNum++;
        }
        return ret;
    }
}
