param(
  [Parameter(Mandatory = $true)]
  [string]$RootPassword
)

$mysql = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
$sqlFile = Join-Path $PSScriptRoot "..\docker\mysql\init.sql" | Resolve-Path

Write-Host "Creating medilabo databases and user..."
& $mysql -u root "-p$RootPassword" --default-character-set=utf8mb4 -e "source $($sqlFile.Path)"

if ($LASTEXITCODE -eq 0) {
  Write-Host 'OK - databases medilabo_patient + medilabo_gateway ready'
  Write-Host 'DB user: medilabo / medilabo'
  Write-Host 'Start services: .\scripts\launch\start-all.ps1'
} else {
  Write-Host "FAILED - check root password and MySQL80 service"
  exit 1
}
