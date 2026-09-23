$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location $root

Write-Host "Starting MySQL and MongoDB (docker compose)..."
docker compose up -d mysql mongo
if ($LASTEXITCODE -ne 0) {
  Write-Host "Infra failed. Start Docker Desktop, then: docker compose up -d mysql mongo"
  exit $LASTEXITCODE
}

Write-Host "Infra ready (mongo:27017, mysql:3306)."
exit 0
