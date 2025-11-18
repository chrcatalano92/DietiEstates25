@echo off
:: Script per ottenere l'IPV4 corrente e salvarlo in un file per Android Studio

set androidAssetsDir=app\src\main\assets
set androidFile=%androidAssetsDir%\ip_address.txt

:: Ottieni l'indirizzo IPv4 (escludendo gli IP virtuali e localhost)
for /f "tokens=2 delims=:" %%A in ('ipconfig ^| findstr "IPv4"') do (
    for /f "tokens=*" %%B in ("%%A") do set ip=%%B
)

:: Verifica se è stato trovato un IP
if "%ip%"=="" (
    echo Nessun indirizzo IPv4 trovato.
    pause
    exit /b 1
)

:: Pulisci l'IP da spazi bianchi
set ip=%ip: =%

:: Mostra l'IP trovato
echo Indirizzo IPv4 trovato: %ip%

:: Verifica e crea la cartella assets se non esiste
if not exist "%androidAssetsDir%" (
    echo Creazione della cartella assets: %androidAssetsDir%
    mkdir "%androidAssetsDir%"
)

:: Salva l'IP nel file per Android Studio
echo %ip% > "%androidFile%"

echo.
echo Indirizzo IPv4 salvato in:
echo - %androidFile% (per Android Studio)
echo.
echo Puoi ora utilizzare questo indirizzo aggiornato in Android Studio come stringa per Retrofit.

:: Apre il file con il blocco note (opzionale)
:: notepad "%androidFile%"

pause