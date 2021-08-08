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
        String withResStarts = null;
        String withResEnds = null;
        JsonObjIsEntryType compareType = null;
        int count = 0;
        boolean inComment = false;
        JsonObjIsEntryType commentType = null;
        
        TokenLine ret = new TokenLine();
        ret.lineNum = lineNum;
        ret.source = line;
        ret.payloadLen = line.payload.size();
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
                            withResStarts = withStarts;
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
                                    int[] range = Utils.GetIntsFromRange(withStarts);
                                    int j = 0;
                                    char lc = payload.charAt(0);
                                    try {
                                        j = Utils.GetIntFromChar(lc);
                                        if(Character.isDigit(lc) && j >= range[0] && j <= range[1]) {
                                            withStarts = (j + "");
                                            withStartsLen = 1;
                                            lfound = true;
                                            break;
                                        }
                                    } catch (ExceptionMalformedRange e) {
                                        //do nothing
                                    }

                                } else if(withStarts.equals(JsonObjTxtMatch.special_lowercase_range)) {
                                    //Found lower case character range
                                    char lc = payload.charAt(0);
                                    if(lc == '_' || Character.isLowerCase(lc)) {
                                        withStarts = (lc + "");
                                        withStartsLen = 1;                                        
                                        lfound = true;
                                        break;
                                    }
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                    //Found lower case character range
                                    char lc = payload.charAt(0);                                    
                                    if(lc == '_' || Character.isLowerCase(lc) || Character.isDigit(lc)) {
                                        withStarts = (lc + "");
                                        withStartsLen = 1;                                        
                                        lfound = true;
                                        break;
                                    }                                    
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                    //Found upper case character range
                                    char lc = payload.charAt(0);
                                    if(lc == '_' || Character.isUpperCase(lc)) {
                                        withStarts = (lc + "");
                                        withStartsLen = 1;                                        
                                        lfound = true;
                                        break;
                                    }
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                    //Found lower case character range
                                    char lc = payload.charAt(0);                                    
                                    if(lc == '_' || Character.isUpperCase(lc) || Character.isDigit(lc)) {
                                        withStarts = (lc + "");
                                        withStartsLen = 1;
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
                        //Logger.wrl(current.payload + " Compare: '" + compare + "' Starts result: " + lfound + " CompareType: " + compareType.type_name);

                        //Check for ending string match
                        if(lfound) {
                            lfound = false;
                            for(String withEnds : type.txt_match.ends_with) {
                                compare = withEnds;
                                withResEnds = withEnds;
                                withEndsLen = withEnds.length();
                                if(withEnds.equals(JsonObjTxtMatch.special_wild_card)) {
                                    //Match anything
                                    lfound = true;
                                    break;
                                } else if(withEnds.equals(JsonObjTxtMatch.special_end_line)) {
                                    //Adjust for system line separator
                                    withEnds = System.lineSeparator();
                                    withEndsLen = withEnds.length();
                                    if(payload.indexOf(withEnds) == (payload.length() - withEndsLen)) {
                                        lfound = true;
                                        break;
                                    }
                                } else if(withEnds.length() > 1 && withEnds.contains(JsonObjTxtMatch.special_range)) {
                                    if(Character.isDigit(withEnds.charAt(0))) {
                                        //Found numeric range
                                        int[] range = Utils.GetIntsFromRange(withEnds);
                                        int j = 0;
                                        char lc = payload.charAt(payload.length() - 1);
                                        try {
                                            j = Utils.GetIntFromChar(lc);
                                            if(Character.isDigit(lc) && j >= range[0] && j <= range[1]) {
                                                withEnds = (j + "");
                                                withEndsLen = 1;                                                
                                                lfound = true;
                                                break;
                                            }
                                        } catch (ExceptionMalformedRange e) {
                                            //do nothing
                                        }
                                       
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_lowercase_range)) {
                                        //Found lower case character range
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(lc == '_' || Character.isLowerCase(lc)) {
                                            withEnds = (lc + "");
                                            withEndsLen = 1;                                            
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                        //Found lower case character range
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(lc == '_' || Character.isLowerCase(lc) || Character.isDigit(lc)) {
                                            withEnds = (lc + "");
                                            withEndsLen = 1;                                            
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                        //Found upper case character range
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(lc == '_' || Character.isUpperCase(lc)) {
                                            withEnds = (lc + "");
                                            withEndsLen = 1;                                            
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                        //Found lower case character range
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(lc == '_' || Character.isUpperCase(lc) || Character.isDigit(lc)) {
                                            withEnds = (lc + "");
                                            withEndsLen = 1;
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
                        //Logger.wrl(current.payload + " Compare: '" + compare + "' Ends result: " + lfound + " CompareType: " + compareType.type_name);                        

                        //Check for containing string match
                        if(lfound) {
                            lfound = false;
                            if(payload.length() >= withStartsLen + withEndsLen) {
                                payloadContains = payload.substring(withStartsLen, (payload.length() - withEndsLen));
                            } else {
                                payloadContains = payload;
                            }
                            //Logger.wrl("Contains compare: " + payloadContains);                            
                                                        
                            if(type.txt_match.contains == null || type.txt_match.contains.isEmpty() || Utils.IsStringEmpty(payloadContains)) {
                                lfound = true;
                            } else {
                                char[] chars = payloadContains.toCharArray();
                                boolean llfound = false;
                                
                                for(char c : chars) {
                                    llfound = false;
                                    //Logger.wrl("Contains compare: Char: " + c + ", " + withResEnds + ", " + withResStarts);   
                                    for(String withContains : type.txt_match.contains) {
                                        compare = withContains;
                                        if(withContains.equals(JsonObjTxtMatch.special_wild_card)) {
                                            //Match anything
                                            //Logger.wrl("Wild card");
                                            llfound = true;
                                            break;

                                        } else if(withContains.equals(JsonObjTxtMatch.special_end_line)) {
                                            //Match system end line
                                            //Logger.wrl("End line");                                            
                                            if((c + "").equals(System.lineSeparator())) {                                                
                                                llfound = true;
                                                break;
                                            }

                                        } else if(withContains.length() > 1 && withContains.contains(JsonObjTxtMatch.special_range)) {
                                            if(Character.isDigit(withContains.charAt(0))) {
                                                //Found numeric range
                                                //Logger.wrl("Numeric range");
                                                int[] range = Utils.GetIntsFromRange(withContains);
                                                int j = 0;
                                                try {
                                                    j = Utils.GetIntFromChar(c);
                                                } catch (ExceptionMalformedRange e) {

                                                }

                                                if(Character.isDigit(c) && j >= range[0] && j <= range[1]) {
                                                    llfound = true;
                                                    break;
                                                }

                                            } else if(withContains.equals(JsonObjTxtMatch.special_lowercase_range)) {
                                                //Found lower case character range
                                                //Logger.wrl("Lowercase range");
                                                if(c == '_' || Character.isLowerCase(c)) {
                                                    llfound = true;
                                                    break;
                                                }

                                            } else if(withContains.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                                //Found lower case character range
                                                //Logger.wrl("Lowercase num range");
                                                if(c == '_' || Character.isLowerCase(c) || Character.isDigit(c)) {
                                                    llfound = true;
                                                    break;
                                                }

                                            } else if(withContains.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                                //Found upper case character range
                                                //Logger.wrl("Uppercase range");
                                                if(c == '_' || Character.isUpperCase(c)) {
                                                    llfound = true;
                                                    break;
                                                }

                                            } else if(withContains.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                                //Found lower case character range
                                                //Logger.wrl("Uppercase num range");
                                                if(c == '_' || Character.isUpperCase(c) || Character.isDigit(c)) {
                                                    llfound = true;
                                                    break;
                                                }

                                            }

                                        } else {
                                            //All entries must match
                                            if((c + "").equals(withContains)) {
                                                llfound = true;
                                                break;
                                            }

                                        }
                                    }

                                    if(llfound == false) {
                                        lfound = false;
                                        break;
                                    }                                
                                }
                                
                                if(llfound) {
                                    lfound = true;
                                }
                            }
                        }
                        //Logger.wrl(current.payload + " Compare: '" + compare + "' Contains result: " + lfound + " CompareType: " + compareType.type_name);                        

                        if(lfound) {
                            found = true;
                            break;
                        }
                    }
                }

                Token tmb = new Token();                
                tmb.artifact = art;
                tmb.index = count;
                tmb.lineNum = lineNum;
                tmb.source = payload;
                if(!found || compareType == null) {
                    tmb.type_name = "Unknown";
                    tmb.type = null;
                } else {
                    tmb.type_name = compareType.type_name;
                    tmb.type = compareType;
                }
                tmb.payload = new ArrayList<>();                
                ret.payload.add(tmb);                
                count++;
                
                if(tmb.type_name.equals("Comment")) {
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
