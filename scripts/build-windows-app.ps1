param(
    [switch]$OpenAfterBuild = $true,
    [switch]$Zip
)

$ErrorActionPreference = "Stop"

$AppName = "Orbital Simulation"
$AppVersion = "1.0.0"
$MainJar = "orbital-app-0.0.1-SNAPSHOT.jar"
$RuntimeModules = "java.base,java.desktop,java.instrument,java.logging,java.management,java.naming,java.net.http,java.prefs,java.scripting,java.sql,java.transaction.xa,java.xml,java.xml.crypto,jdk.crypto.ec,jdk.unsupported"

$RootDir = Resolve-Path (Join-Path $PSScriptRoot "..")
$TargetDir = Join-Path $RootDir "target"
$DistDir = Join-Path $TargetDir "windows-app"
$AppDir = Join-Path $DistDir $AppName
$ExePath = Join-Path $AppDir "$AppName.exe"
$BuildDir = Join-Path ([System.IO.Path]::GetTempPath()) ("orbital-jpackage-" + [System.Guid]::NewGuid().ToString("N"))
$InputDir = Join-Path $BuildDir "input"
$PackageDir = Join-Path $BuildDir "output"

function Resolve-MavenCommand {
    $LocalMaven = Join-Path $RootDir "apache-maven-3.9.6\bin\mvn.cmd"
    if (Test-Path $LocalMaven) {
        return $LocalMaven
    }

    $SystemMaven = Get-Command "mvn.cmd" -ErrorAction SilentlyContinue
    if ($SystemMaven) {
        return $SystemMaven.Source
    }

    $SystemMaven = Get-Command "mvn" -ErrorAction SilentlyContinue
    if ($SystemMaven) {
        return $SystemMaven.Source
    }

    throw "Maven was not found. Install Maven or put apache-maven-3.9.6 in the project folder."
}

try {
    Set-Location $RootDir

    if (-not (Get-Command "jpackage" -ErrorAction SilentlyContinue)) {
        throw "jpackage was not found. Install JDK 21 and add it to PATH."
    }

    $Maven = Resolve-MavenCommand
    & $Maven -q -DskipTests package

    if (Test-Path $DistDir) {
        Remove-Item $DistDir -Recurse -Force
    }

    New-Item -ItemType Directory -Force -Path $InputDir, $PackageDir, $DistDir | Out-Null
    Copy-Item (Join-Path $TargetDir $MainJar) $InputDir
    Copy-Item (Join-Path $RootDir "src\main\resources\fonts") (Join-Path $InputDir "fonts") -Recurse

    jpackage `
        --type app-image `
        --name $AppName `
        --app-version $AppVersion `
        --vendor "Orbital" `
        --add-modules $RuntimeModules `
        --input $InputDir `
        --main-jar $MainJar `
        --dest $PackageDir `
        --java-options "-Dfile.encoding=UTF-8" `
        --java-options "-Xmx1024m"

    Copy-Item (Join-Path $PackageDir $AppName) $DistDir -Recurse

    if ($Zip) {
        $ZipPath = Join-Path $RootDir "Orbital Simulation Windows.zip"
        if (Test-Path $ZipPath) {
            Remove-Item $ZipPath -Force
        }
        Compress-Archive -Path $AppDir -DestinationPath $ZipPath -Force
        Write-Host "Built Windows zip: $ZipPath"
    }

    Write-Host "Built Windows app: $ExePath"

    if ($OpenAfterBuild -and (Test-Path $ExePath)) {
        Start-Process $ExePath
    }
}
finally {
    if (Test-Path $BuildDir) {
        Remove-Item $BuildDir -Recurse -Force
    }
}
