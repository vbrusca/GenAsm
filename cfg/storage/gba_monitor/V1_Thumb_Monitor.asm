
Monitor:
	push {r0-r7, lr}
	push {r7}
	push {r6}
		mov r6,#0
		mov r7,r0
		bl ShowReg ;ShowReg R6=R7
		mov r7,r8
		bl ShowReg ;ShowReg R6=R7
		bl newline

		mov r6,#1
		mov r7,r1
		bl ShowReg ;ShowReg R6=R7
		mov r7,r9
		bl ShowReg ;ShowReg R6=R7
		bl newline

		mov r6,#2
		mov r7,r2
		bl ShowReg ;ShowReg R6=R7
		mov r7,r10
		bl ShowReg ;ShowReg R6=R7
		bl newline

		mov r6,#3
		mov r7,r3
		bl ShowReg ;ShowReg R6=R7
		mov r7,r11
		bl ShowReg ;ShowReg R6=R7
		bl newline

		mov r6,#4
		mov r7,r4
		bl ShowReg ;ShowReg R6=R7
		mov r7,r12
		bl ShowReg ;ShowReg R6=R7
		bl newline

		mov r6,#5
		mov r7,r5
		bl ShowReg ;ShowReg R6=R7
		mov r7,r13
		add r7,#44
		bl ShowReg ;ShowReg R6=R7
		bl newline

		mov r6,#6
	pop {r7}
		bl ShowReg ;ShowReg R6=R7
		mov r7,r14
		bl ShowReg ;ShowReg R6=R7
		bl newline

		mov r6,#7
	pop {r7}
		bl ShowReg ;ShowReg R6=R7
		mov r7,r15
		bl ShowReg ;ShowReg R6=R7
		bl newline

	pop {r0-r7, pc}



ShowRegLR:
		mov r0,#76 ; L
		bl PrintChar

		mov r0,#82 ; R
		bl PrintChar
	b ShowRegB


ShowRegSP:
		mov r0,#83 ; S
		bl PrintChar

		mov r0,#80 ; P
		bl PrintChar
	b ShowRegB

ShowRegPC:
		mov r0,#80 ; P
		bl PrintChar

		mov r0,#67 ; C
		bl PrintChar
	b ShowRegB

ShowReg:					;ShowReg R6=R7
	push {r0-r5,r7, lr}
		cmp r6,#15
		beq ShowRegPC

		cmp r6,#14
		beq ShowRegLR
		cmp r6,#13
		beq ShowRegSP


		mov r0,#82 ; D
		bl PrintChar

		mov r0,r6 ; 0
		bl ShowHexChar
ShowRegB:
		mov r0,#58 ; :
		bl PrintChar

		bl ShowHex32
		add r6,#8

		mov r0,#32 ; space
		bl PrintChar
	pop {r0-r5,r7, pc}
ShowHex:
	push {r0-r7, lr}
		LSR r0,r7,#4
		bl ShowHexChar 			;Print Char
		mov r0,r7
		bl ShowHexChar 			;Print Char
	pop {r0-r7, pc}


ShowHex32:			;Show 32 bit val R7
	push {r0-r7, lr}
		LSR r0,r7,#28
		bl ShowHexChar 			;Print Char
		LSR r0,r7,#24
		bl ShowHexChar 			;Print Char
		LSR r0,r7,#20
		bl ShowHexChar 			;Print Char
		LSR r0,r7,#16
		bl ShowHexChar 			;Print Char
		LSR r0,r7,#12
		bl ShowHexChar 			;Print Char
		LSR r0,r7,#8
		bl ShowHexChar 			;Print Char
		LSR r0,r7,#4
		bl ShowHexChar 			;Print Char
		mov r0,r7
		bl ShowHexChar 			;Print Char
	pop {r0-r7, pc}




ShowHexChar:
	push {r0-r7, lr}
		mov r2,#0xF
		and r0,r2
		cmp r0,#10
		blt ShowHexN
		add r0,#7
ShowHexN:
		add r0,#48
		bl PrintChar
	pop {r0-r7, pc}
	;STMFD sp!,{r0-r12, lr}
		;mov r3,
		;and r0,r2,#0x0F ; r3
		;cmp r0,#10
		;addge r0,r0,#7
		;add r0,r0,#48
		;bl PrintChar
	;LDMFD sp!,{r0-r12, pc}

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




	;mov	r0,#userram		;Address
	;mov r1,#2				;Lines
	;bl MemDump
MemDump:
	push {r0-r7, lr}
		mov r4,r0
		mov r7,r0
		bl ShowHex32
		mov r0,#58 ; :
		bl PrintChar
		bl NewLine

MemDumpNextLine:
		mov r3,#0
MemDumpAgain:
		ldrb r7,[r4,r3]
		bl ShowHex

		mov r0,#32 ; space
		bl PrintChar

		add r3,r3,#1
		cmp r3,#MonitorWidth
		bne MemDumpAgain

		mov r3,#0
MemDumpAgainB:
		mov r0,#0
		ldrb r0,[r4,r3]
		bl PrintCharSafe
		add r3,r3,#1
		cmp r3,#MonitorWidth
		bne MemDumpAgainB
		add r4,r4,r3
		bl NewLine

		sub r1,r1,#1
		bne MemDumpNextLine
	pop {r0-r7, pc}

PrintCharSafe:
	push {r0-r7, lr}
		cmp r0,#32
		blt PrintCharNG

		cmp r0,#128
		bgt PrintCharNG

		bl printchar
	pop {r0-r7, pc}
PrintCharNG:
		mov r0,#46 ;'.'
		bl printchar
	pop {r0-r7, pc}
