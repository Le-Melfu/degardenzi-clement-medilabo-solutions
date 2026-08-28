param(
  [Parameter(Mandatory = $true)]
  [int]$Port
)

$ErrorActionPreference = "SilentlyContinue"

function Get-ListeningPids {
  param([int]$ListenPort)

  $pids = @()
  foreach ($line in (netstat -ano)) {
    if ($line -notmatch 'LISTENING') {
      continue
    }
    if ($line -notmatch "^\s*TCP\s+(\S+):$ListenPort\s+\S+\s+LISTENING\s+(\d+)\s*$") {
      continue
    }
    $procId = [int]$Matches[2]
    if ($procId -gt 0) {
      $pids += $procId
    }
  }
  return $pids | Select-Object -Unique
}

$pids = @(Get-ListeningPids -ListenPort $Port)
if ($pids.Count -eq 0) {
  Write-Host "Port $Port is free."
  exit 0
}

foreach ($procId in $pids) {
  Write-Host "Killing PID $procId (port $Port)..."
  Stop-Process -Id $procId -Force
}

Start-Sleep -Milliseconds 500
Write-Host "Port $Port cleared."
exit 0
