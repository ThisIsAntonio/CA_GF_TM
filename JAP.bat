:: ---------------------------------------------------------------------
:: JAP COURSE - BATCH SCRIPT
:: SCRIPT CST8221 - Fall 2023
:: Names: Marcos Astudillo Carrasco
:: ---------------------------------------------------------------------
:: Batch File Script to Run JAP Project for F23
:: ---------------------------------------------------------------------

CLS
@echo off
:: LOCAL VARIABLES ....................................................

SET JAVAFXDIR=C:\AmazonCoretto\javafx-sdk-20.0.2\lib
SET EXT_ERR=err
SET EXT_OUT=out
SET SRCDIR=src
SET BINDIR=bin
SET BINERR=jap-javac.err
SET JARNAME=JAP.jar
SET MAINCLASSBIN=cs.CSModel
SET BINLIST=cs/*.class ca/*.class support/*.class gl/controller/*.class gl/model/*.class gl/view/*.class tm/server/view/*.class tm/server/model/*.class tm/server/controller/*.class tm/client/view/*.class tm/client/model/*.class tm/client/controller/*.class
SET PACKAGELIST=src/cs/*.java src/ca/*.java src/support/*.java src/gl/controller/*.java src/gl/model/*.java src/gl/view/*.java src/tm/server/view/*.java src/tm/server/model/*.java src/tm/server/controller/*.java src/tm/client/view/*.java src/tm/client/model/*.java 
SET RESOURCES=resources
SET JAROUT=jap-jar.out
SET JARERR=jap-jar.err
SET LIBDIR=lib
SET DOCDIR=doc
SET DOCERR=jap-javadoc.err
SET DOCPACK=cs
SET CAPACK=ca
SET GLPACK=gl
SET TMPACK=tm
SET SUPPACK=support
SET MODULELIST=javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics

ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @            FALL - 2 0 2 3          @  @"
ECHO "@          ###    ##        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@        ###    ##                                                  @"
ECHO "@        ##    ###                 ###                              @"
ECHO "@         ##    ###                ###                              @"
ECHO "@           ##    ##               ###   #####  ##     ##  #####    @"
ECHO "@         (     (      ((((()      ###       ## ###   ###      ##   @"
ECHO "@     ((((     ((((((((     ()     ###   ######  ###  ##   ######   @"
ECHO "@        ((                ()      ###  ##   ##   ## ##   ##   ##   @"
ECHO "@         ((((((((((( ((()         ###   ######    ###     ######   @"
ECHO "@         ((         ((           ###                               @"
ECHO "@          (((((((((((                                              @"
ECHO "@   (((                      ((                                     @"
ECHO "@    ((((((((((((((((((((() ))                                      @"
ECHO "@         ((((((((((((((((()                                        @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

ECHO "BEGIN JAP SCRIPT"

ECHO "0. Clear ...................."
rmdir /s /q %BINDIR%
del *.%EXT_ERR%
del *.%EXT_OUT%

ECHO "1. Compiling ...................."
javac -Xlint -cp ".;%SRCDIR%;%JAVAFXDIR%/*" %PACKAGELIST% -d %BINDIR% 2> %BINERR%
mkdir %BINDIR%\%RESOURCES%
xcopy %SRCDIR%\%RESOURCES% %BINDIR%\%RESOURCES% /I


ECHO "2. Creating Jar ..................."
cd bin
jar cvfe %JARNAME% %MAINCLASSBIN% %BINLIST% %RESOURCES% > ../%JAROUT% 2> ../%JARERR%

ECHO "3. Creating Javadoc ..............."
cd ..
javadoc -cp ".;%BINDIR%;%JAVAFXDIR%" --module-path "%JAVAFXDIR%" --add-modules %MODULELIST% -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %CAPACK%:%GLPACK%:%DOCPACK%:%SUPPACK% -d %DOCDIR% 2> %DOCERR%


ECHO "4. Running Jar ...................."
cd bin
start java --module-path "%JAVAFXDIR%" --add-modules %MODULELIST% -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (A13 - F23)
:: ---------------------------------------------------------------------
