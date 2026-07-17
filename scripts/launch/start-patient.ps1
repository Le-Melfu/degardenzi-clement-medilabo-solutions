param(
  [ValidateSet("local", "mysql")]
  [string]$Profile = "local"
)

$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location (Join-Path $root "service\patient")

Write-Host "Starting Patient on http://localhost:8081 (profile=$Profile) ..."
if ($Profile -eq "mysql") {
  mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"
} else {
  mvn spring-boot:run
}
