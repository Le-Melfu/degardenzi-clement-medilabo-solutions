$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location (Join-Path $root "service\patient")

Write-Host "Starting Patient on http://localhost:8081 (MySQL) ..."
mvn spring-boot:run
