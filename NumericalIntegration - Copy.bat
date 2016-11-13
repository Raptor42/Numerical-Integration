@echo off

SET a=-2
SET b=5
SET fn = 

SET n=1


:loop1
echo %n%
IF %n% GEQ 100 GOTO loop2 
java NumericalIntegration -a %a% -b %b% -n %n% -fn cmdtest 
SET /a n=%n%+1
GOTO loop1

:loop2
echo %n%
IF %n% GEQ 1000 GOTO loop3
java NumericalIntegration -a %a% -b %b% -n %n% -fn cmdtest 
SET /a n=%n%+10
GOTO loop2

:loop3
echo %n%
IF %n% GEQ 10000 GOTO exit
java NumericalIntegration -a %a% -b %b% -n %n% -fn cmdtest 
SET /a n=%n%+100
GOTO loop3

:exit
pause
exit