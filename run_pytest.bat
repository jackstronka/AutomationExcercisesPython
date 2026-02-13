@echo off
REM Run all Python E2E tests. Requires: Python 3.10+, pip install -r requirements.txt
cd /d "%~dp0"
python -m pytest tests/ -v --tb=short %*
if errorlevel 1 exit /b 1
exit /b 0
