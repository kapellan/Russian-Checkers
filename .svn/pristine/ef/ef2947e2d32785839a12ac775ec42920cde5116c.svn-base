;!include MUI2.nsh

Name "RussianCheckers-beta-2 installer"

; The file to write
OutFile "RussianCheckers.exe"


; The default installation directory
InstallDir "$PROGRAMFILES\RussianCheckers"

; Request application privileges for Windows Vista
RequestExecutionLevel "user"

;--------------------------------

; Pages

Page directory
Page instfiles

;--------------------------------

; The stuff to install
Section "install" ;No components page, name is not important

  ; Set output path to the installation directory.
  SetOutPath "$INSTDIR"
  
  ; Put file there
  File "RussianCheckers.exe"
  File "menu.xml"
  File "logo.ico"
  File "logo.png"
  CreateShortCut "$Desktop\RussianCheckers.lnk" "$INSTDIR\RussianCheckers.exe" "" "$INSTDIR\logo.ico"
  
SectionEnd ; end the section

