package net.middlemind.GenAsm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:39 PM EST
 */
public class TokenerThumb implements Tokener {

    @Override
    public TokenLine LineTokenize(ArtifactLine line, int lineNum, JsonObj entryTypes) throws ExceptionTokenerNotFound {
        JsonObjIsEntryTypes types = (JsonObjIsEntryTypes)entryTypes;
        boolean found;
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
                    for(JsonObjIsEntryType type : types.is_entry_types) {
                        boolean lfound = false;
                        int withStartsLen = 0;
                        int withEndsLen = 0;
                        String payloadContains = "";
                        compareType = type;

                        //Check for starting string match
                        for(String withStarts : type.txt_match.starts_with) {
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
                                    int j = 0;
                                    
                                    try {
                                        j = Utils.GetIntFromChar(payload.charAt(0));
                                        if(j >= range[0] && j <= range[1]) {
                                            lfound = true;
                                            break;
                                        }
                                    } catch (ExceptionMalformedRange e) {
                                        //do nothing
                                    }

                                } else if(withStarts.equals(JsonObjTxtMatch.special_lowercase_range)) {
                                    //Found lower case character range
                                    //First entry must match
                                    char lc = payload.charAt(0);
                                    if(lc == '_' || Character.isLowerCase(lc)) {
                                        lfound = true;
                                        break;
                                    }
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                    //Found lower case character range
                                    //First entry must match
                                    char lc = payload.charAt(0);                                    
                                    if(lc == '_' || Character.isLowerCase(lc) || Character.isDigit(lc)) {
                                        lfound = true;
                                        break;
                                    }                                    
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                    //Found upper case character range
                                    //First entry must match
                                    char lc = payload.charAt(0);
                                    if(lc == '_' || Character.isUpperCase(lc)) {
                                        lfound = true;
                                        break;
                                    }
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                    //Found lower case character range
                                    //First entry must match
                                    char lc = payload.charAt(0);                                    
                                    if(lc == '_' || Character.isUpperCase(lc) || Character.isDigit(lc)) {
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
                            for(String withEnds : type.txt_match.ends_with) {
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
                                    //Logger.wrl("Payload: " + payload + ", " + payload.indexOf(withEnds) + ", " + payload.indexOf('\n'));
                                    if(payload.indexOf(withEnds) == (payload.length() - withEndsLen)) {
                                        lfound = true;
                                        break;
                                    }
                                } else if(withEnds.length() > 1 && withEnds.contains(JsonObjTxtMatch.special_range)) {
                                    //Logger.wrl("Special range found: " + withEnds);
                                    if(Character.isDigit(withEnds.charAt(0))) {
                                        //Found numeric range
                                        //Last entry must match
                                        int[] range = Utils.GetIntsFromRange(withEnds);
                                        int j = 0;
                                        
                                        try {
                                            j = Utils.GetIntFromChar(payload.charAt(payload.length() - 1));
                                            if(j >= range[0] && j <= range[1]) {
                                                lfound = true;
                                                break;
                                            }
                                        } catch (ExceptionMalformedRange e) {
                                            //do nothing
                                        }
                                       
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_lowercase_range)) {
                                        //Found lower case character range
                                        //Last entry must match
                                        //Logger.wrl("SpecialLowercaseRange: " + payload.charAt(payload.length() - 1));
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(lc == '_' || Character.isLowerCase(lc)) {
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                        //Found lower case character range
                                        //First entry must match
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(lc == '_' || Character.isLowerCase(lc) || Character.isDigit(lc)) {
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                        //Found upper case character range
                                        //Last entry must match
                                        //Logger.wrl("SpecialUppercaseRange: " + payload.charAt(payload.length() - 1));                                        
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(lc == '_' || Character.isUpperCase(lc)) {
                                            //Logger.wrl("SpecialUppercaseRange: true");
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                        //Found lower case character range
                                        //First entry must match
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(lc == '_' || Character.isUpperCase(lc) || Character.isDigit(lc)) {
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

                            for(String withContains : type.txt_match.contains) {
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
                                            int j = 0;
                                            
                                            try {
                                                j = Utils.GetIntFromChar(c);
                                            } catch (ExceptionMalformedRange e) {
                                                llfound = false;
                                                break;
                                            }
                                            
                                            if(!(j >= range[0] && j <= range[1])) {
                                                llfound = false;
                                                break;
                                            }
                                        }

                                        if(llfound) {
                                            lfound = true;
                                            break;
                                        } 
                                        
                                    } else if(withContains.equals(JsonObjTxtMatch.special_lowercase_range)) {
                                        //Found lower case character range
                                        //All entries must match
                                        char[] chars = payloadContains.toCharArray();
                                        boolean llfound = true;
                                        for(char c : chars) {
                                            if(c != '_' && !Character.isLowerCase(c)) {
                                                llfound = false;
                                                break;
                                            }
                                        }

                                        if(llfound) {
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withContains.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                        //Found lower case character range
                                        //All entries must match
                                        char[] chars = payloadContains.toCharArray();
                                        boolean llfound = true;
                                        for(char c : chars) {
                                            if(c != '_' && !Character.isLowerCase(c) && !Character.isDigit(c)) {
                                                llfound = false;
                                                break;
                                            }
                                        }

                                        if(llfound) {
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withContains.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                        //Found upper case character range
                                        //All entries must match
                                        //Logger.wrl("SpecialUppercaseRange contains");
                                        char[] chars = payloadContains.toCharArray();
                                        boolean llfound = true;
                                        for(char c : chars) {
                                            //Logger.wrl("SpecialUppercaseRange contains: " + c);                                            
                                            if(c != '_' && !Character.isUpperCase(c)) {
                                                llfound = false;
                                                break;
                                            }
                                        }

                                        if(llfound) {
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withContains.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                        //Found lower case character range
                                        //All entries must match
                                        char[] chars = payloadContains.toCharArray();
                                        boolean llfound = true;
                                        for(char c : chars) {
                                            if(c != '_' && !Character.isUpperCase(c) && !Character.isDigit(c)) {
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
                                        if(!(c + "").equals(withContains)) {
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

                        //Logger.wrl("CompareType " + type.type_name + " lfound: " + lfound + " found: " + found + " payload: " + payload + " comparetype: " + compareType.name);                        
                        if(lfound) {
                            found = true;
                            break;
                        }
                    }
                }

                Token tmb = new Token();                
                tmb.payloadArtifact = art;
                tmb.index = count;
                tmb.lineNum = lineNum;
                tmb.payloadSource = payload;
                if(!found || compareType == null) {
                    //Logger.wrl("SpecialUppercaseRange set name: " + found + ", " + compareType);
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
        } catch (ExceptionMalformedRange e) {
            e.printStackTrace();
            throw new ExceptionTokenerNotFound("Could not find token match for artifact, " + payload + ", at line number " + lineNum + " with artifact " + current.payload + " compared to " + compare);
        } catch(Exception e) {
            e.printStackTrace();
            throw new ExceptionTokenerNotFound("Could not find token match for artifact, " + payload + ", at line number " + lineNum + " with artifact " + current.payload + " compared to " + compare);                
        }
    }

    @Override
    public List<TokenLine> FileTokenize(List<ArtifactLine> file, JsonObj entryTypes) throws ExceptionTokenerNotFound {
        int lineNum = 0;
        ArrayList<TokenLine> ret = new ArrayList<>();
        for(ArtifactLine art : file) {
            ret.add(LineTokenize(art, lineNum, entryTypes));
            lineNum++;
        }
        return ret;
    }
}
