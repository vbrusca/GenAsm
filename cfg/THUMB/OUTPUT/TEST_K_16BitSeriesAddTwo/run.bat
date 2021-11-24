@echo off
@echo %1
if [%~1]==[-vb] (goto startVB) else (goto startMGBA)

:startVB
N:\Emu\VisualBoyAdvance\VisualBoyAdvance ".\output_assembly_listing_endian_lil.bin"
goto end

:startMGBA
N:\Emu\mGBA\mGBA ".\output_assembly_listing_endian_lil.bin"
goto end

:end
