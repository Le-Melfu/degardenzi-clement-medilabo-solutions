$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location $root

Write-Host "Starting MongoDB (docker compose)..."
docker compose up -d mongo
if ($LASTEXITCODE -ne 0) {
  Write-Host "Mongo failed. Start Docker Desktop, then: docker compose up -d mongo"
  exit $LASTEXITCODE
}

$mysqlListen = netstat -ano | Select-String ":3306\s+.+\s+LISTENING"
if ($mysqlListen) {
  Write-Host "Port 3306 already in use (local MySQL). Skipping docker mysql."
} else {
  Write-Host "Starting MySQL (docker compose)..."
  docker compose up -d mysql
  if ($LASTEXITCODE -ne 0) {
    Write-Host "Docker MySQL failed. Patient/Gateway need a MySQL on 3306."
    exit $LASTEXITCODE
  }
}

Write-Host "Infra ready (mongo:27017, mysql:3306)."
exit 0
