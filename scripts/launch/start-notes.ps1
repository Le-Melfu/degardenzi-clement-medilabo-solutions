$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location (Join-Path $root "service\notes")

Write-Host "Starting Notes on http://localhost:8082 (MongoDB) ..."
mvn spring-boot:run
