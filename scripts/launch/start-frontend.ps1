$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location (Join-Path $root "frontend")

if (-not (Test-Path "node_modules")) {
  Write-Host "Installing frontend dependencies..."
  npm install
}

Write-Host "Starting Frontend on http://localhost:3000 ..."
npm run dev
