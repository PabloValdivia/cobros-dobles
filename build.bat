@echo off

set DEBUG_MODE=

if "%1" == "debug" (
  set DEBUG_MODE=debug
)

cd ve.net.alge.cxcdobles.targetplatform
call .\plugin-builder.bat %DEBUG_MODE% ..\ve.net.alge.cxcdobles ..\ve.net.alge.cxcdobles.test
cd ..
