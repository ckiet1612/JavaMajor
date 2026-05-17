# Tài liệu hướng dẫn cài đặt

## 1. Giới thiệu

Tài liệu này hướng dẫn cách cài môi trường, chạy project từ mã nguồn và build app desktop cho project **Orbital Simulation**.

Ứng dụng được viết bằng JavaFX kết hợp Spring Boot, dùng H2 Database để lưu dữ liệu vệ tinh. Khi chạy ở chế độ dev, database được tạo tự động trong thư mục `data/`, nên không cần cài thêm MySQL hay PostgreSQL.

## 2. Yêu cầu môi trường

Máy cần có các thành phần sau:

- JDK 21
- Maven 3.9.x hoặc thư mục Maven local `apache-maven-3.9.6`
- Git nếu muốn clone project từ GitHub
- Windows 10/11 hoặc macOS

Kiểm tra Java:

```bash
java -version
javac -version
jpackage --version
```

Nếu `jpackage` không chạy được thì thường là máy đang dùng JRE hoặc JDK chưa được thêm vào `PATH`.

## 3. Lấy mã nguồn

Nếu dùng Git:

```bash
git clone https://github.com/ckiet1612/JavaMajor.git
cd JavaMajor
```

Nếu nhận project dạng file zip thì giải nén ra một thư mục dễ nhớ, ví dụ:

```text
C:\JavaMajor
```

hoặc trên macOS:

```text
/Users/macbook/Documents/University Library/JavaMajor
```

## 4. Cài JDK 21

### Windows

Mở PowerShell bằng quyền Administrator, sau đó chạy:

```powershell
winget install --id EclipseAdoptium.Temurin.21.JDK -e --accept-source-agreements --accept-package-agreements
```

Sau khi cài xong, đóng PowerShell rồi mở lại và kiểm tra:

```powershell
java -version
jpackage --version
```

### macOS

Có thể cài JDK 21 từ trang Eclipse Adoptium, hoặc dùng Homebrew nếu máy đã có:

```bash
brew install --cask temurin@21
```

Sau đó kiểm tra:

```bash
java -version
jpackage --version
```

## 5. Cài Maven

Project có thể dùng Maven local trong thư mục `apache-maven-3.9.6`. Nếu thư mục này đã có sẵn trong project thì không cần cài Maven vào hệ thống.

### Windows

Nếu chưa có Maven, tải Maven local vào project bằng PowerShell:

```powershell
cd "C:\JavaMajor"

$MavenVersion = "3.9.6"
$MavenZip = "apache-maven-$MavenVersion-bin.zip"
$MavenUrl = "https://archive.apache.org/dist/maven/maven-3/$MavenVersion/binaries/$MavenZip"

Invoke-WebRequest -Uri $MavenUrl -OutFile $MavenZip
Expand-Archive $MavenZip -DestinationPath .
Remove-Item $MavenZip

.\apache-maven-3.9.6\bin\mvn.cmd -version
```

### macOS

Nếu project đã có `apache-maven-3.9.6`, kiểm tra bằng:

```bash
./apache-maven-3.9.6/bin/mvn -version
```

Nếu muốn dùng Maven hệ thống:

```bash
brew install maven
mvn -version
```

## 6. Chạy project từ mã nguồn

### Windows

```powershell
cd "C:\JavaMajor"
.\apache-maven-3.9.6\bin\mvn.cmd javafx:run
```

Nếu Maven đã có trong `PATH`:

```powershell
cd "C:\JavaMajor"
mvn javafx:run
```

### macOS

```bash
cd "/Users/macbook/Documents/University Library/JavaMajor"
./apache-maven-3.9.6/bin/mvn javafx:run
```

Nếu Maven đã có trong `PATH`:

```bash
mvn javafx:run
```

## 7. Build app macOS

Chạy script build:

```bash
cd "/Users/macbook/Documents/University Library/JavaMajor"
chmod +x scripts/build-macos-app.sh
OPEN_AFTER_BUILD=false ./scripts/build-macos-app.sh
```

App sau khi build nằm ở:

```text
target/macos-app/Orbital Simulation.app
```

Mở app:

```bash
open "target/macos-app/Orbital Simulation.app"
```

## 8. Build app Windows

Trên Windows VM, mở PowerShell trong thư mục project:

```powershell
cd "C:\JavaMajor"
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\build-windows-app.ps1 -Zip
```

File chạy sau khi build:

```text
target\windows-app\Orbital Simulation\Orbital Simulation.exe
```

File zip sau khi build:

```text
Orbital Simulation Windows.zip
```

Chạy app đã build:

```powershell
& "C:\JavaMajor\target\windows-app\Orbital Simulation\Orbital Simulation.exe"
```

## 9. Database

Khi chạy bằng Maven, dữ liệu được lưu ở:

```text
data/orbitaldb
```

Khi chạy bản app đã đóng gói bằng `jpackage`, dữ liệu được lưu trong thư mục người dùng:

```text
.orbital-simulation/orbitaldb
```

Database dùng H2 nên project tự tạo bảng và dữ liệu mẫu khi chạy lần đầu.

## 10. Một số lỗi thường gặp

### Lỗi `mvn is not recognized`

Máy chưa có Maven trong `PATH`. Cách nhanh nhất là dùng Maven local:

```powershell
.\apache-maven-3.9.6\bin\mvn.cmd javafx:run
```

### Lỗi `jpackage was not found`

Máy chưa cài JDK đầy đủ hoặc đang trỏ sang JRE. Cài lại JDK 21 và mở terminal mới.

### PowerShell không cho chạy script

Chạy lệnh này trong đúng cửa sổ PowerShell hiện tại:

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
```

Sau đó chạy lại script build.

### Font tiếng Việt bị ô vuông trên Windows

Nguyên nhân thường là đang chạy bản build cũ. Hãy cập nhật lại code mới nhất, sau đó chạy lại:

```powershell
.\apache-maven-3.9.6\bin\mvn.cmd javafx:run
```

Nếu dùng file `.exe`, cần build lại app Windows để CSS mới được đóng gói vào app.

## 11. Kiểm tra nhanh trước khi nộp

Chạy build Maven:

```bash
./apache-maven-3.9.6/bin/mvn -q -DskipTests package
```

Chạy app:

```bash
./apache-maven-3.9.6/bin/mvn javafx:run
```

Kiểm tra các thao tác chính:

- Thêm vệ tinh mới
- Chọn vệ tinh trong danh sách
- Xem thông tin chi tiết
- Mở liên kết tín hiệu
- Ngắt liên kết
- Xóa vệ tinh được chọn
- Xóa toàn bộ mạng vệ tinh
- Kéo và zoom mô hình 3D
