# TÀI LIỆU HƯỚNG DẪN CÀI ĐẶT

## 1. Thông tin chung

**Tên phần mềm:** Orbital Simulation  
**Nền tảng hỗ trợ:** Windows và macOS  
**Mục đích:** Hướng dẫn giảng viên/người dùng cài đặt và khởi chạy ứng dụng mô phỏng mạng vệ tinh.

Ứng dụng đã được đóng gói sẵn thành file chạy độc lập. Người dùng không cần cài JDK, Maven hoặc build lại mã nguồn để sử dụng phần mềm.

## 2. Gói cài đặt

Tùy theo hệ điều hành đang sử dụng, chọn đúng file cài đặt tương ứng:

| Hệ điều hành | File cần tải |
| --- | --- |
| Windows | `Orbital Simulation Windows.zip` |
| macOS | `Orbital Simulation MacOS.zip` |

Các file này có thể được cung cấp kèm bài nộp hoặc tải từ đường dẫn do nhóm phát triển cung cấp.

## 3. Yêu cầu hệ thống

### Windows

- Windows 10 hoặc Windows 11.
- RAM tối thiểu 4 GB, khuyến nghị 8 GB.
- Dung lượng trống tối thiểu 300 MB.
- Máy có hỗ trợ hiển thị đồ họa cơ bản để chạy giao diện JavaFX 3D.

### macOS

- macOS 11 trở lên.
- RAM tối thiểu 4 GB, khuyến nghị 8 GB.
- Dung lượng trống tối thiểu 300 MB.
- Máy có hỗ trợ hiển thị đồ họa cơ bản để chạy giao diện JavaFX 3D.

## 4. Cài đặt trên Windows

### Bước 1: Tải file cài đặt

Tải file:

```text
Orbital Simulation Windows.zip
```

Sau khi tải xong, đặt file ở thư mục dễ tìm, ví dụ `Downloads` hoặc `Desktop`.

### Bước 2: Giải nén file

Nhấn chuột phải vào file zip và chọn:

```text
Extract All...
```

Sau đó chọn thư mục để giải nén.

Sau khi giải nén, bên trong sẽ có thư mục:

```text
Orbital Simulation
```

### Bước 3: Mở ứng dụng

Mở thư mục vừa giải nén và chạy file:

```text
Orbital Simulation.exe
```

Nếu Windows hiện cảnh báo bảo mật, chọn **More info** rồi chọn **Run anyway** để tiếp tục mở ứng dụng.

### Bước 4: Kiểm tra sau khi mở

Ứng dụng được cài đặt thành công khi màn hình chính hiển thị:

- Mô hình Trái Đất 3D ở bên trái.
- Bảng điều khiển nhiệm vụ ở bên phải.
- Danh sách vệ tinh và các chức năng phóng vệ tinh, mở liên kết tín hiệu.

## 5. Cài đặt trên macOS

### Bước 1: Tải file cài đặt

Tải file:

```text
Orbital Simulation MacOS.zip
```

Sau khi tải xong, đặt file ở thư mục dễ tìm, ví dụ `Downloads`.

### Bước 2: Giải nén file

Nhấp đúp vào file zip để giải nén.

Sau khi giải nén, macOS sẽ tạo ứng dụng:

```text
Orbital Simulation.app
```

### Bước 3: Di chuyển ứng dụng

Có thể chạy trực tiếp ứng dụng tại vị trí vừa giải nén. Nếu muốn cài gọn hơn, kéo file:

```text
Orbital Simulation.app
```

vào thư mục:

```text
Applications
```

### Bước 4: Mở ứng dụng

Nhấp đúp vào **Orbital Simulation.app** để mở.

Nếu macOS chặn ứng dụng do chưa được ký bởi nhà phát triển chính thức:

1. Nhấn chuột phải vào **Orbital Simulation.app**.
2. Chọn **Open**.
3. Chọn **Open** thêm một lần nữa trong hộp thoại xác nhận.

Thao tác này thường chỉ cần thực hiện ở lần mở đầu tiên.

## 6. Dữ liệu của ứng dụng

Ứng dụng dùng cơ sở dữ liệu H2 được nhúng sẵn. Người dùng không cần cài đặt thêm hệ quản trị cơ sở dữ liệu.

Khi chạy lần đầu, ứng dụng sẽ tự tạo dữ liệu cần thiết. Các vệ tinh được thêm mới trong quá trình sử dụng sẽ được lưu lại để dùng cho các lần mở sau.

## 7. Gỡ cài đặt

### Windows

Xóa thư mục đã giải nén:

```text
Orbital Simulation
```

Nếu muốn xóa cả dữ liệu đã lưu, có thể xóa thêm thư mục dữ liệu của ứng dụng trong thư mục người dùng.

### macOS

Xóa file:

```text
Orbital Simulation.app
```

Nếu đã kéo ứng dụng vào `Applications`, chỉ cần xóa ứng dụng khỏi thư mục đó.

## 8. Một số lỗi thường gặp

### Không mở được file zip

Kiểm tra lại file tải về đã hoàn tất chưa. Nếu file bị lỗi, tải lại gói cài đặt từ nguồn ban đầu.

### Windows báo ứng dụng không rõ nguồn gốc

Đây là cảnh báo bình thường với các ứng dụng bài tập lớn chưa ký chứng chỉ phát hành. Chọn **More info** rồi chọn **Run anyway** nếu file được tải từ nguồn tin cậy.

### macOS báo không thể mở ứng dụng

Sử dụng cách mở bằng chuột phải:

```text
Right click -> Open -> Open
```

Nếu vẫn không mở được, kiểm tra xem file `.app` đã được giải nén hoàn toàn chưa.

### Ứng dụng mở nhưng giao diện hiển thị chậm

Ứng dụng có sử dụng mô hình 3D, nên lần khởi động đầu tiên có thể mất vài giây. Nếu máy đang chạy nhiều chương trình nặng, nên đóng bớt trước khi mở ứng dụng.

## 9. Kết quả mong đợi

Sau khi cài đặt đúng, người dùng có thể mở **Orbital Simulation** và sử dụng các chức năng chính:

- Quan sát mô hình Trái Đất 3D.
- Thêm vệ tinh vào hệ thống.
- Xem danh sách và thông tin vệ tinh.
- Mở liên kết tín hiệu giữa hai vệ tinh.
- Xóa vệ tinh hoặc xóa toàn bộ mạng vệ tinh.
