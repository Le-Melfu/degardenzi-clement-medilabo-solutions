param(
  [ValidateSet("local", "mysql")]
  [string]$Profile = "local"
)

$launchDir = $PSScriptRoot
$root = Resolve-Path (Join-Path $launchDir "..\..")

function Start-ServiceWindow([string]$Title, [string]$ScriptPath, [string]$ExtraArgs = "") {
  $command = "& '$ScriptPath' $ExtraArgs"
  Start-Process powershell -WorkingDirectory $root -ArgumentList @(
    "-NoExit",
    "-Command",
    "Write-Host '=== $Title ===' -ForegroundColor Cyan; $command"
  )
}

$profileArg = "-Profile $Profile"

Write-Host "Launching Medilabo stack (profile=$Profile) in separate windows..."
Start-ServiceWindow "Patient" (Join-Path $launchDir "start-patient.ps1") $profileArg
Start-Sleep -Seconds 3
Start-ServiceWindow "Gateway" (Join-Path $launchDir "start-gateway.ps1") $profileArg
Start-Sleep -Seconds 2
Start-ServiceWindow "Frontend" (Join-Path $launchDir "start-frontend.ps1")

Write-Host "Done. Check each window. URLs:"
Write-Host "  Patient   http://localhost:8081"
Write-Host "  Gateway   http://localhost:8080"
Write-Host "  Frontend  http://localhost:3000"
