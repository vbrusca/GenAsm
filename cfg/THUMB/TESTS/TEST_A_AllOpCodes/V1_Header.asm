	;VGB: Header with dummy data
	;.arm                 ; Use arm instruction set.
    ; header...
    .org  0x08000000     ; GBA ROM Address starts at 0x08000000       
	.equ ProgBase,0;0x00000000
    ;.section text			

	.equ ramarea, 0x02000000
	
	.equ userram, 0x02000000
	
	.equ CursorX,ramarea+32

	.equ MonitorWidth,6
 
;000h    4     ROM Entry Point  (32bit ARM branch opcode, eg. "B rom_start") 
    b	GbaStart

	.equ CursorX,ramarea+32
	.equ CursorY,ramarea+33

	;.space 178					;Logo (omitted) + Program name
	;.byte 0x96					;Fixed value
	;.space 49					;Dummy Header
;Standard, empty GBA header
.incbin "\gba_example_bins\gba_header.bin"

GbaStart:

