# Tài liệu hướng dẫn sử dụng

## 1. Mục đích ứng dụng

**Orbital Simulation** là ứng dụng mô phỏng mạng vệ tinh quanh Trái Đất. Người dùng có thể thêm vệ tinh, theo dõi danh sách vệ tinh, xem thông tin chi tiết và mô phỏng đường liên kết tín hiệu giữa hai vệ tinh.

Giao diện được chia thành hai phần chính:

- Khu vực 3D bên trái để xem Trái Đất, vệ tinh, quỹ đạo và đường liên kết.
- Bảng điều khiển bên phải để nhập dữ liệu, mở liên kết và quản lý danh sách vệ tinh.

## 2. Khởi động ứng dụng

Nếu chạy từ mã nguồn trên macOS:

```bash
./apache-maven-3.9.6/bin/mvn javafx:run
```

Nếu chạy từ mã nguồn trên Windows:

```powershell
.\apache-maven-3.9.6\bin\mvn.cmd javafx:run
```

Nếu đã build app Windows:

```powershell
& "C:\JavaMajor\target\windows-app\Orbital Simulation\Orbital Simulation.exe"
```

Nếu đã build app macOS:

```bash
open "target/macos-app/Orbital Simulation.app"
```

## 3. Các khu vực trên giao diện

### Viewport 3D

Đây là khu vực hiển thị Trái Đất và các vệ tinh. Người dùng có thể:

- Kéo chuột trái để xoay góc nhìn.
- Cuộn chuột để zoom gần hoặc xa.
- Click vào vùng trống để bỏ chọn vệ tinh.
- Quan sát đường quỹ đạo và đường liên kết tín hiệu khi có route.

### Trạng thái nhiệm vụ

Panel này hiển thị trạng thái hiện tại của hệ thống, ví dụ:

- Hệ thống sẵn sàng.
- Đã phóng vệ tinh mới.
- Thiếu dữ liệu nhập.
- Đang theo dõi tín hiệu.
- Liên kết bị che khuất hoặc mất tín hiệu.

Khi thao tác lỗi, nên đọc dòng trạng thái này trước vì app thường ghi rõ lý do.

### Phóng vệ tinh

Đây là form để thêm vệ tinh mới vào mạng theo dõi.

Các trường cần nhập:

- **Tên**: tên hoặc callsign của vệ tinh, ví dụ `SAT-01`.
- **Vĩ độ**: vị trí ban đầu theo độ.
- **Kinh độ**: vị trí ban đầu theo độ.
- **Độ cao**: độ cao quỹ đạo, đơn vị km.
- **Nghiêng**: độ nghiêng quỹ đạo, đơn vị độ.
- **Cấu hình**: gồm kích cỡ hiển thị và màu vệ tinh.

Sau khi nhập xong, bấm:

```text
PHÓNG VỆ TINH
```

Nếu dữ liệu hợp lệ, vệ tinh sẽ xuất hiện trong mô hình 3D và trong danh sách **Mạng theo dõi**.

## 4. Thêm vệ tinh mới

Các bước thao tác:

1. Nhập tên vệ tinh.
2. Nhập vĩ độ, kinh độ, độ cao và độ nghiêng.
3. Nhập kích cỡ nếu muốn đổi kích thước hiển thị.
4. Chọn màu vệ tinh bằng color picker.
5. Bấm **PHÓNG VỆ TINH**.

Ví dụ dữ liệu:

```text
Tên: SAT-01
Vĩ độ: 10
Kinh độ: 106
Độ cao: 550
Nghiêng: 45
Kích cỡ: 0.35
```

Lưu ý:

- Tên vệ tinh không được để trống.
- Không nên dùng trùng tên vệ tinh đã có trong mạng.
- Các ô số phải nhập bằng số, không nhập thêm chữ như `km` hoặc `độ`.

## 5. Chọn và xem chi tiết vệ tinh

Ở panel **Mạng theo dõi**, mỗi dòng là một vệ tinh.

Khi chọn một vệ tinh:

- Vệ tinh được highlight trong mô hình 3D.
- Panel **Hồ sơ vệ tinh** hiện ở góc dưới bên phải của viewport.
- App hiển thị các thông tin như tên, độ cao, vận tốc và thông tin quỹ đạo.

Có thể chọn nhiều vệ tinh để xóa cùng lúc. Nếu bấm lại vào một vệ tinh đang được chọn, app sẽ bỏ chọn vệ tinh đó.

## 6. Mở liên kết tín hiệu

Panel **Liên kết tín hiệu** dùng để mô phỏng đường truyền giữa hai vệ tinh.

Các bước thao tác:

1. Chọn vệ tinh ở ô **Điểm phát**.
2. Chọn vệ tinh ở ô **Điểm thu**.
3. Bấm **MỞ LIÊN KẾT**.

Khi liên kết được mở:

- App sẽ tìm đường truyền phù hợp trong mạng vệ tinh.
- Đường liên kết được vẽ trong viewport 3D.
- Trạng thái nhiệm vụ sẽ cập nhật số trạm đang được theo dõi.

Nếu hai vệ tinh bị che khuất hoặc không tìm được đường truyền, app sẽ báo mất tín hiệu.

## 7. Ngắt liên kết tín hiệu

Để xóa đường liên kết đang hiển thị, bấm:

```text
NGẮT
```

Sau khi ngắt, route line trong mô hình 3D sẽ biến mất và trạng thái nhiệm vụ trở về trạng thái bình thường.

## 8. Xóa vệ tinh

### Xóa vệ tinh được chọn

1. Chọn một hoặc nhiều vệ tinh trong **Mạng theo dõi**.
2. Bấm **XÓA CHỌN**.

Các vệ tinh được chọn sẽ bị xóa khỏi:

- Database
- Danh sách theo dõi
- Mô hình 3D
- ComboBox chọn điểm phát và điểm thu

Nếu vệ tinh đang nằm trong đường liên kết hiện tại, app sẽ tự ngắt liên kết.

### Xóa toàn bộ vệ tinh

Bấm:

```text
XÓA TẤT CẢ
```

Chức năng này xóa toàn bộ mạng vệ tinh hiện tại. Nên cân nhắc trước khi dùng vì dữ liệu sẽ bị xóa khỏi database.

## 9. Cách đọc danh sách vệ tinh

Trong **Mạng theo dõi**, mỗi vệ tinh có các thông tin tóm tắt:

- Tên vệ tinh
- Vùng quỹ đạo như LEO, MEO hoặc GEO
- Độ cao
- Vận tốc
- Màu đại diện

Danh sách này giúp chọn nhanh vệ tinh để xem chi tiết, mở route hoặc xóa.

## 10. Một số tình huống hay gặp

### Bấm phóng nhưng không thêm được vệ tinh

Kiểm tra lại:

- Tên có bị trống không.
- Tên có bị trùng không.
- Các trường số có nhập đúng định dạng không.

Ví dụ đúng:

```text
550
```

Ví dụ dễ lỗi:

```text
550 km
```

### Không mở được liên kết

Kiểm tra lại:

- Đã chọn đủ điểm phát và điểm thu chưa.
- Hai điểm có phải là hai vệ tinh khác nhau không.
- Có đủ vệ tinh trong mạng để tạo đường truyền không.

Nếu trạng thái báo liên kết bị che khuất, nghĩa là tại thời điểm đó app không tìm thấy đường truyền hợp lệ.

### Không thấy panel chi tiết

Panel chi tiết chỉ hiện khi chọn đúng một vệ tinh. Nếu chọn nhiều vệ tinh hoặc bỏ chọn hết, panel sẽ ẩn.

### Chữ tiếng Việt bị lỗi trên Windows

Nếu chữ có dấu bị ô vuông, có thể bạn đang chạy bản build cũ. Hãy cập nhật code mới và build lại app Windows.

## 11. Gợi ý sử dụng khi demo

Khi demo bài tập lớn, có thể đi theo flow này:

1. Mở app và giới thiệu viewport 3D.
2. Thêm một vệ tinh mới bằng form **Phóng vệ tinh**.
3. Chọn vệ tinh trong **Mạng theo dõi** để xem highlight và hồ sơ chi tiết.
4. Chọn hai vệ tinh trong **Liên kết tín hiệu**.
5. Bấm **MỞ LIÊN KẾT** để hiển thị route.
6. Kéo xoay mô hình 3D để xem đường truyền.
7. Bấm **NGẮT** để xóa route.
8. Xóa vệ tinh được chọn hoặc xóa toàn bộ mạng nếu cần reset dữ liệu.
