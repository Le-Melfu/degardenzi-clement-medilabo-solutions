$launchDir = $PSScriptRoot
$root = Resolve-Path (Join-Path $launchDir "..\..")

function Start-ServiceWindow([string]$Title, [string]$ScriptPath) {
  $command = "& '$ScriptPath'"
  Start-Process powershell -WorkingDirectory $root -ArgumentList @(
    "-NoExit",
    "-Command",
    "Write-Host '=== $Title ===' -ForegroundColor Cyan; $command"
  )
}

Write-Host "Starting MySQL + MongoDB..."
& (Join-Path $launchDir "start-infra.ps1")
if ($LASTEXITCODE -ne 0) {
  Write-Host "Infra failed. Notes needs Mongo, Patient/Gateway need MySQL."
  exit $LASTEXITCODE
}

Write-Host "Launching Medilabo backends in separate windows..."
Start-ServiceWindow "Patient" (Join-Path $launchDir "start-patient.ps1")
Start-Sleep -Seconds 3
Start-ServiceWindow "Notes" (Join-Path $launchDir "start-notes.ps1")
Start-Sleep -Seconds 3
Start-ServiceWindow "Risk assessment" (Join-Path $launchDir "start-risk-assessment.ps1")
Start-Sleep -Seconds 3
Start-ServiceWindow "Gateway" (Join-Path $launchDir "start-gateway.ps1")
Start-Sleep -Seconds 2
Start-ServiceWindow "Frontend" (Join-Path $launchDir "start-frontend.ps1")

Write-Host "Done. Check each window. URLs:"
Write-Host "  Patient   http://localhost:8081"
Write-Host "  Notes     http://localhost:8082"
Write-Host "  Risk      http://localhost:8083"
Write-Host "  Gateway   http://localhost:8080"
Write-Host "  Frontend  http://localhost:3000"
