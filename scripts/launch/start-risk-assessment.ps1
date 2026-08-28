$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location (Join-Path $root "service\risk-assessment")

Write-Host "Starting Risk assessment on http://localhost:8083 ..."
mvn spring-boot:run
