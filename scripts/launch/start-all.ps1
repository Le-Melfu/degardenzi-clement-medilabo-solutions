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

Write-Host "Launching Medilabo stack (MySQL) in separate windows..."
Start-ServiceWindow "Patient" (Join-Path $launchDir "start-patient.ps1")
Start-Sleep -Seconds 3
Start-ServiceWindow "Gateway" (Join-Path $launchDir "start-gateway.ps1")
Start-Sleep -Seconds 2
Start-ServiceWindow "Frontend" (Join-Path $launchDir "start-frontend.ps1")

Write-Host "Done. Check each window. URLs:"
Write-Host "  Patient   http://localhost:8081"
Write-Host "  Gateway   http://localhost:8080"
Write-Host "  Frontend  http://localhost:3000"
