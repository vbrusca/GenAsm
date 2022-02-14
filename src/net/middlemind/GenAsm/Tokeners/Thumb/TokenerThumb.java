package net.middlemind.GenAsm.Tokeners.Thumb;

import net.middlemind.GenAsm.Tokeners.Tokener;
import net.middlemind.GenAsm.Tokeners.TokenLine;
import net.middlemind.GenAsm.Tokeners.Token;
import net.middlemind.GenAsm.Lexers.ArtifactLine;
import net.middlemind.GenAsm.Lexers.Artifact;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMalformedRange;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoTokenerFound;
import net.middlemind.GenAsm.JsonObjs.JsonObjTxtMatch;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsEntryType;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsEntryTypes;
import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 * An implementation of the tokener interface this class is used to tokenize lexerized assembly source code.
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:39 PM EST
 */
public class TokenerThumb implements Tokener {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "TokenerThumb";
    
    /**
     * A method used to tokenize an artifact line.
     * @param line                      The artifact line to tokenize.
     * @param lineNum                   The line number of the artifact line being tokenized.
     * @param entryTypes                An object representing the instruction sets entry types.
     * @return                          A token line derived from the artifact lines.
     * @throws ExceptionNoTokenerFound  An exception is thrown if there is an error during the tokenizing process.
     */
    @Override
    @SuppressWarnings({"CallToPrintStackTrace", "null"})
    public TokenLine LineTokenize(ArtifactLine line, int lineNum, JsonObj entryTypes) throws ExceptionNoTokenerFound {
        JsonObjIsEntryTypes types = (JsonObjIsEntryTypes)entryTypes;
        boolean found;
        String payload = "";
        Artifact current = null;
        String compare = null;
        String withResStarts = null;
        String withResEnds = null;
        String withResContains = null;
        JsonObjIsEntryType compareType = null;
        int count = 0;
        boolean inComment = false;
        JsonObjIsEntryType commentType = null;
        boolean verbose = false;
        
        TokenLine ret = new TokenLine();
        ret.lineNumAbs = lineNum;
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
                        int withContainsLen = 0;
                        String payloadContains = "";
                        compareType = type;

                        //Check for starting string match
                        for(String withStarts : type.txt_match.starts_with) {
                           // <editor-fold defaultstate="collapsed" desc="starts_with">
                            compare = withStarts;
                            withResStarts = withStarts;
                            withStartsLen = withStarts.length();
                            if(withStarts.equals(JsonObjTxtMatch.special_wild_card)) {
                                //Match anything
                                lfound = true;
                                break;

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
                                    if(Character.isLowerCase(lc)) {
                                        withStarts = (lc + "");
                                        withStartsLen = 1;                                        
                                        lfound = true;
                                        break;
                                    }
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                    //Found lower case character range numeric
                                    char lc = payload.charAt(0);
                                    if(Character.isLowerCase(lc) || Character.isDigit(lc)) {
                                        withStarts = (lc + "");
                                        withStartsLen = 1;                                        
                                        lfound = true;
                                        break;
                                    }                                    
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                    //Found upper case character range
                                    char lc = payload.charAt(0);
                                    if(Character.isUpperCase(lc)) {
                                        withStarts = (lc + "");
                                        withStartsLen = 1;                                        
                                        lfound = true;
                                        break;
                                    }
                                    
                                } else if(withStarts.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                    //Found upper case character range numeric
                                    char lc = payload.charAt(0);
                                    if(Character.isUpperCase(lc) || Character.isDigit(lc)) {
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
                            // </editor-fold>
                        }
                        
                        if(verbose) {
                            Logger.wrl(current.payload + " Compare: '" + compare + "' Starts result: " + lfound + " CompareType: " + compareType.type_name);
                        }
                        
                        //Check for ending string match
                        if(lfound) {
                            lfound = false;
                            for(String withEnds : type.txt_match.ends_with) {
                                 // <editor-fold defaultstate="collapsed" desc="ends_with">
                                compare = withEnds;
                                withResEnds = withEnds;
                                withEndsLen = withEnds.length();
                                if(withEnds.equals(JsonObjTxtMatch.special_wild_card)) {
                                    //Match anything
                                    lfound = true;
                                    break;

                                } else if(withEnds.length() > 1 && withEnds.contains(JsonObjTxtMatch.special_range)) {
                                    if(Character.isDigit(withEnds.charAt(0))) {
                                        //Found numeric range
                                        int[] range = Utils.GetIntsFromRange(withEnds);
                                        for(int z = range[0]; z <= range[1]; z++) {
                                            String zI = (z + "");
                                            int j = 0;
                                            String lc = payload.substring(payload.length() - zI.length());
                                            try {
                                                j = Utils.GetIntFromString(lc);
                                                if(j >= range[0] && j <= range[1]) {
                                                    withEnds = (j + "");
                                                    withEndsLen = withEnds.length();
                                                    lfound = true;
                                                    break;
                                                }
                                            } catch (ExceptionMalformedRange e) {
                                                //do nothing
                                            }
                                        }
                                       
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_lowercase_range)) {
                                        //Found lower case character range
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(Character.isLowerCase(lc)) {
                                            withEnds = (lc + "");
                                            withEndsLen = 1;                                            
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                        //Found lower case character range numeric
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(Character.isLowerCase(lc) || Character.isDigit(lc)) {
                                            withEnds = (lc + "");
                                            withEndsLen = 1;                                            
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                        //Found upper case character range
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(Character.isUpperCase(lc)) {
                                            withEnds = (lc + "");
                                            withEndsLen = 1;                                            
                                            lfound = true;
                                            break;
                                        }
                                        
                                    } else if(withEnds.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                        //Found upper case character range numeric
                                        char lc = payload.charAt(payload.length() - 1);
                                        if(Character.isUpperCase(lc) || Character.isDigit(lc)) {
                                            withEnds = (lc + "");
                                            withEndsLen = 1;
                                            lfound = true;
                                            break;
                                        }                                        
                                        
                                    }
                                } else {
                                    int compStart = 0;
                                    if(payload.length() > 1) {
                                        compStart = 1;
                                    }
                                    
                                    if(verbose) {
                                        Logger.wrl("IndexOf withEnds: " + withEnds + " in string payload: " + payload + " with compStart: " + compStart + " is " + (payload.indexOf(withEnds, compStart)) + " - or - " + (payload.lastIndexOf(withEnds)) + ", " + withStartsLen + ", compared to " + (payload.length() - withEndsLen) + " withStartsLen: " + withStartsLen);
                                    }
                                    
                                    if(payload.indexOf(withEnds, compStart) == (payload.length() - withEndsLen)) {
                                        lfound = true;
                                        break;
                                    } else if(payload.lastIndexOf(withEnds) == (payload.length() - withEndsLen)) {
                                        lfound = true;
                                        break;                                        
                                    }
                                }
                                 // </editor-fold>
                            }
                        }
                        
                        if(verbose) {
                            Logger.wrl(current.payload + " Compare: '" + compare + "' Ends result: " + lfound + " CompareType: " + compareType.type_name);                        
                        }
                        
                        //Check for containing string match
                        if(lfound) {
                            lfound = false;
                            if(payload.length() >= withStartsLen + withEndsLen) {
                                payloadContains = payload.substring(withStartsLen, (payload.length() - withEndsLen));
                            } else {
                                payloadContains = payload;
                            }
                                                        
                            if(type.txt_match.contains == null || type.txt_match.contains.isEmpty() || Utils.IsStringEmpty(payloadContains)) {
                                lfound = true;
                            } else {
                                char[] chars = payloadContains.toCharArray();
                                boolean llfound = false;
                                
                                for(char c : chars) {
                                    // <editor-fold defaultstate="collapsed" desc="contains_with">
                                    llfound = false;
                                    for(String withContains : type.txt_match.contains) {
                                        compare = withContains;
                                        withResContains = withContains;
                                        withContainsLen = withContains.length();
                                        
                                        if(withContains.equals(JsonObjTxtMatch.special_wild_card)) {
                                            //Match anything
                                            llfound = true;
                                            break;

                                        } else if(withContains.length() > 1 && withContains.contains(JsonObjTxtMatch.special_range)) {
                                            if(Character.isDigit(withContains.charAt(0))) {
                                                //Found numeric range
                                                int[] range = Utils.GetIntsFromRange(withContains);
                                                for(int z = range[0]; z <= range[1]; z++) {
                                                    String zI = (z + "");
                                                    int j = 0;
                                                    String lc = payloadContains.substring(payloadContains.length() - zI.length());
                                                    try {
                                                        j = Utils.GetIntFromString(lc);
                                                        if(j >= range[0] && j <= range[1]) {
                                                            withContains = (j + "");
                                                            withContainsLen = withContains.length();
                                                            llfound = true;
                                                            break;
                                                        }
                                                    } catch (ExceptionMalformedRange e) {
                                                        //do nothing
                                                    }
                                                }                                                

                                            } else if(withContains.equals(JsonObjTxtMatch.special_lowercase_range)) {
                                                //Found lower case character range
                                                if(Character.isLowerCase(c)) {
                                                    withContains = (c + "");
                                                    withContainsLen = 1;
                                                    llfound = true;
                                                    break;
                                                }

                                            } else if(withContains.equals(JsonObjTxtMatch.special_lowercase_num_range)) {
                                                //Found lower case character range
                                                if(Character.isLowerCase(c) || Character.isDigit(c)) {
                                                    withContains = (c + "");
                                                    withContainsLen = 1;
                                                    llfound = true;
                                                    break;
                                                }

                                            } else if(withContains.equals(JsonObjTxtMatch.special_uppercase_range)) {                                
                                                //Found upper case character range
                                                if(Character.isUpperCase(c)) {
                                                    withContains = (c + "");
                                                    withContainsLen = 1;                                                    
                                                    llfound = true;
                                                    break;
                                                }

                                            } else if(withContains.equals(JsonObjTxtMatch.special_uppercase_num_range)) {
                                                //Found lower case character range
                                                if(Character.isUpperCase(c) || Character.isDigit(c)) {
                                                    withContains = (c + "");
                                                    withContainsLen = 1;                                                    
                                                    llfound = true;
                                                    break;
                                                }

                                            }

                                        } else {
                                            if((c + "").equals(withContains)) {
                                                llfound = true;
                                                break;
                                            }
                                        }
                                    }
                                    // </editor-fold>
                                }

                                if(llfound) {
                                    lfound = true;
                                }
                            }
                        }
                        
                        if(verbose) {                        
                            Logger.wrl(current.payload + " Compare: '" + compare + "' Contains result: " + lfound + " CompareType: " + compareType.type_name + " PayloadContains: " + payloadContains);                        
                        }
                        
                        //Check for must contain
                        if(lfound && !Utils.IsListEmpty(type.txt_match.must_contain)) {           
                            lfound = true;
                            for(String s : type.txt_match.must_contain) {
                                 // <editor-fold defaultstate="collapsed" desc="must_contain">
                                if(!payload.contains(s)) {
                                    lfound = false;
                                    break;
                                }
                                 // </editor-fold>
                            }
                        }
                        
                        if(verbose) {                        
                            Logger.wrl(current.payload + " Compare: '" + compare + "' MustNotContain result: " + lfound + " CompareType: " + compareType.type_name);
                        }
                        
                        //Check for must not contain
                        if(lfound && !Utils.IsListEmpty(type.txt_match.must_not_contain)) {           
                            lfound = true;
                            for(String s : type.txt_match.must_not_contain) {
                              // <editor-fold defaultstate="collapsed" desc="must_not_contain">
                                if(payload.contains(s)) {
                                    lfound = false;
                                    break;
                                }
                              // </editor-fold>  
                            }
                        }
                        
                        if(verbose) {                        
                            Logger.wrl(current.payload + " Compare: '" + compare + "' MustNotContain result: " + lfound + " CompareType: " + compareType.type_name);
                        }
                        
                        if(lfound) {
                            found = true;
                            break;
                        }
                    }
                }

                Token tmb = new Token();                
                tmb.artifact = art;
                tmb.index = count;
                tmb.lineNumAbs = lineNum;
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
                
                if(tmb.type_name.equals(JsonObjIsEntryTypes.NAME_OPCODE)) {
                    tmb.isOpCode = true;
                    
                } else if(tmb.type_name.equals(JsonObjIsEntryTypes.NAME_DIRECTIVE)) {
                    tmb.isDirective = true;
                
                } else if(tmb.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL)) {
                    tmb.isLabel = true;
                
                } else if(tmb.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {
                    tmb.isLabelRef = true;
                
                } else if(tmb.type_name.equals(JsonObjIsEntryTypes.NAME_COMMENT)) {
                    tmb.isComment = true;
                    commentType = compareType;
                    inComment = true;
                
                } else if(tmb.type_name.equals(JsonObjIsEntryTypes.NAME_DIRECTIVE_STRING)) {
                    tmb.source = tmb.source.replace("|", "");
                
                }
            }

            return ret;
        } catch (ExceptionMalformedRange e) {
            e.printStackTrace();
            throw new ExceptionNoTokenerFound("Could not find token match for artifact, " + payload + ", at line number " + lineNum + " with artifact " + current.payload + " compared to " + compare);
        } catch(Exception e) {
            e.printStackTrace();
            throw new ExceptionNoTokenerFound("Could not find token match for artifact, " + payload + ", at line number " + lineNum + " with artifact " + current.payload + " compared to " + compare);                
        }
    }

    /**
     * A method used to tokenize a lexerized assembly source file.
     * @param file                      A list of artifact lines that represent the assembly source file to process.
     * @param entryTypes                An object representing the instruction sets entry types.
     * @return                          A list of token line instances that represent the processed list of artifact lines.
     * @throws ExceptionNoTokenerFound  An exception is thrown if there is an error during the file tokening process.
     */    
    @Override
    public List<TokenLine> FileTokenize(List<ArtifactLine> file, JsonObj entryTypes) throws ExceptionNoTokenerFound {
        int lineNum = 0;
        ArrayList<TokenLine> ret = new ArrayList<>();
        for(ArtifactLine art : file) {
            ret.add(LineTokenize(art, lineNum, entryTypes));
            lineNum++;
        }
        return ret;
    }
}
