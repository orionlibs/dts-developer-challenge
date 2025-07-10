#!/usr/bin/env pwsh
<#
.SYNOPSIS
  Generate a private RSA key and a self‑signed certificate under src\main\resources\localhost‑ssl.
.DESCRIPTION
  These certificates are for consistent HTTPS in your local development environment only.
#>

# Paths (use forward slashes or backslashes—PowerShell handles both)
$localhostSslFolder = "src\main\resources\localhost-ssl"
$confFile           = Join-Path $localhostSslFolder "openssl-req.cnf"
$keyFile            = Join-Path $localhostSslFolder "localhost.key"
$crtFile            = Join-Path $localhostSslFolder "localhost.crt"

# Only generate if one of the files is missing
if (-not (Test-Path $keyFile) -or -not (Test-Path $crtFile)) {

    # Create the folder (no-op if it already exists)
    New-Item -ItemType Directory -Path $localhostSslFolder -Force | Out-Null

    # Run openssl (assumes openssl is on your PATH)
    & openssl req `
        -nodes `
        -x509 `
        -newkey rsa:4096 `
        -keyout  $keyFile `
        -out     $crtFile `
        -sha256 `
        -days    3650 `
        -config  $confFile `
        -subj    "/C=GB/ST=A/L=B/O=C/OU=D/CN=E" `
        -extensions v3_req
}
