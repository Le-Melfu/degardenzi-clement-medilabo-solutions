param(
  [Parameter(Mandatory = $true)]
  [int]$Port
)

$conns = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
if (-not $conns) {
  Write-Host "Port $Port is free."
  exit 0
}

$pids = $conns | Select-Object -ExpandProperty OwningProcess -Unique | Where-Object { $_ -gt 0 }
foreach ($procId in $pids) {
  Write-Host "Killing PID $procId (port $Port)..."
  Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue
}

Start-Sleep -Milliseconds 500
Write-Host "Port $Port cleared."
