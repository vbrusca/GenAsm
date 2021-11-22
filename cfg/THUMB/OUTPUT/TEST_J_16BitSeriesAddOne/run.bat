@echo off
@echo %1
if [%~1]==[-vb] (goto startVB) else (goto startMGBA)

:startVB
N:\Emu\VisualBoyAdvance\VisualBoyAdvance "C:\Users\variable\Documents\GitHub\GenAsm\cfg\THUMB\OUTPUT\TEST_J_16BitSeriesAddOne\output_assembly_listing_endian_lil.bin"
goto end

:startMGBA
N:\Emu\mGBA\mGBA "C:\Users\variable\Documents\GitHub\GenAsm\cfg\THUMB\OUTPUT\TEST_J_16BitSeriesAddOne\output_assembly_listing_endian_lil.bin"
goto end

:end
