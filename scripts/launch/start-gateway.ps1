$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location (Join-Path $root "service\gateway")

Write-Host "Starting Gateway on http://localhost:8080 (MySQL) ..."
mvn spring-boot:run
