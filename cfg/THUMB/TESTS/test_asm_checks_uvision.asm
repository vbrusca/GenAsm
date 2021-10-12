AREA ToThumb, CODE, READONLY        ; Name this block of code
    ENTRY                           ; Mark first instruction to execute
    ARM                             ; Subsequent instructions are ARM 
start
    ADR     r0, into_thumb + 1      ; Processor starts in ARM state 
    BX      r0                      ; Inline switch to Thumb state
    THUMB                           ; Subsequent instructions are Thumb
into_thumb
    MOVS    r0, #10 
