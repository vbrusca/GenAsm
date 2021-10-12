
NewLine:
	STMFD sp!,{r0-r12, lr}
		mov r3,#CursorX
		mov r0,#0
		strB r0,[r3]	;X

		mov r3,#CursorY
		ldrB r0,[r3]	;Y
		add r0,r0,#1
		strB r0,[r3]	;Y
	LDMFD sp!,{r0-r12, pc}
PrintChar:
	STMFD sp!,{r0-r12, lr}
		sub r0,r0,#32
		mov r4,#0
		mov r5,#0

		mov r3,#CursorX
		ldrB r4,[r3]	;X
		mov r3,#CursorY
		ldrB r5,[r3]	;Y

		mov r3,#0x06000000 ; VRAM

		mov r6,#8			;Xpos
		mul r2,r4,r6
		add r3,r3,r2

		mov r4,#240*8		;Ypos
		mul r2,r5,r4
		add r3,r3,r2

		adr r4,BitmapFont ; use address 4 bytes before to use auto update later
		add r4,r4,r0,asl #3

		mov r1,#8 ; 48 bytes per row = 12 words
	.pixel_loop:

		mov r9,#0b00000001
		ldrb r8,[r4]	;Load Letter
		add r4,r4,#1


		and r11,r9,r8,ror #7
		and r10,r9,r8,ror #6
		mov r10,r10,lsl #8
		orr r2,r10,r11
		orr r2,r2,r2,asl #1
		orr r2,r2,r2,asl #2
		strh r2,[r3]
		add r3,r3,#2

		and r11,r9,r8,ror #5
		and r10,r9,r8,ror #4
		mov r10,r10,lsl #8
		orr r2,r10,r11
		orr r2,r2,r2,asl #1
		orr r2,r2,r2,asl #2
		strh r2,[r3]
		add r3,r3,#2

		and r11,r9,r8,ror #3
		and r10,r9,r8,ror #2
		mov r10,r10,lsl #8
		orr r2,r10,r11
		orr r2,r2,r2,asl #1
		orr r2,r2,r2,asl #2
		strh r2,[r3]
		add r3,r3,#2

		and r11,r9,r8,ror #1
		and r10,r9,r8;,ror #0
		mov r10,r10,lsl #8
		orr r2,r10,r11
		orr r2,r2,r2,asl #1
		orr r2,r2,r2,asl #2
		strh r2,[r3]
		add r3,r3,#2

		add r3,r3,#240-8

		subs r1,r1,#1
		bne .pixel_loop

		mov r3,#CursorX

		ldrB r0,[r3]	;X
		add r0,r0,#1
		strB r0,[r3]	;X
	LDMFD sp!,{r0-r12, pc}
;    MOV pc,lr		;ret


;Address = 0x06000000 + Ypos * (240 * 2)+ Xpos * 2

GetScreenPos: ;R1,R2 = X,Y  - result in r10
	STMFD sp!,{r2}
		STMFD sp!,{r1}
			mov r10,#0x06000000 ; VRAM base

			mov r1,#240		;Ypos *240
			mul r2,r1,r2
		LDMFD sp!,{r1}
		add r10,r10,r2
		add r10,r10,r1		;Xpos
	LDMFD sp!,{r2}
	MOV pc,lr

GetNextLine:
	add r10,r10,#240		;Move down a line
	MOV pc,lr

ScreenInit:
;Turn on the screen - ScreenMode 4 - 240x160 256 color
	STMFD sp!,{r0-r12, lr}
		mov r4,#0x04000000  	;DISPCNT -LCD Control
		mov r2,#0x404    		;4= Layer 2 on / 4= Screen Mode 4
		str	r2,[r4]

;Fill the screen
		mov r0, #0x06000000		;Screen RAM
				;  CCcc
		mov r1, #0x0000
		mov r2, #256*192/2		;Number of Words (2 pixels per word)

FillScreenLoop:
		strh r1, [r0],#2		;Store+inc 2 bytes
		subs r2, r2, #1
		bne FillScreenLoop

		adr r0,Defaultpalette
		sub r0,r0,#2
		mov r1,#0x05000000  	;Palette
		sub r1,r1,#2
		mov r2,#16				;Entry count
ScreenInit_MorePalette:
		ldrh  r3,[r0,#2]!      	;-BBBBBGGGGGRRRRR
		strh  r3,[r1,#2]!      	;store in palette register
		subs r2,r2,#1
		bne ScreenInit_MorePalette
	LDMFD sp!,{r0-r12, pc}

Defaultpalette:
		   ;-BBBBBGGGGGRRRRR
    .half 0b0011110000000000	;0
	.half 0b0000001111111111	;1
	.half 0b0111111111100000	;2
	.half 0b0000000000011111	;3
	.half 0b0000001111111111	;4
	.half 0b0000001111111111	;5
	.half 0b0000001111111111	;6
	.half 0b0000001111111111	;7
	.half 0b0000001111111111	;8
	.half 0b0000001111111111	;9
	.half 0b0000001111111111	;A
	.half 0b0000001111111111	;B
	.half 0b0000001111111111	;C
	.half 0b0000001111111111	;D
	.half 0b0000001111111111	;E
	.half 0b0000001111111111	;F

SetPalette:	;R0= Palette entry ; R1= 0x-GRB color def
	STMFD sp!,{r0-r12, lr}
		mov r11,#0x05000000  	;palette register address
		add r11,r11,r0,asl #1	;2 bytes per color

		mov r2,r1,asl #11				;B
				;    -BBBBBGGGGGRRRRR
		and r2,r2,#0b0111100000000000
		mov r5,r2

		mov r2,r1,lsr #3			;R
				;    -BBBBBGGGGGRRRRR
		and r2,r2,#0b0000000000011110
		orr r5,r5,r2

		mov r2,r1,lsr #2			;G
				;    -BBBBBGGGGGRRRRR
		and r2,r2,#0b0000001111000000
		orr r5,r5,r2
		;Store Result
		strH r5,[r11]	;-BBBBBGGGGGRRRRR
	LDMFD sp!,{r0-r12, pc}
