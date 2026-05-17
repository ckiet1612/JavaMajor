# TÀI LIỆU HƯỚNG DẪN SỬ DỤNG

## 1. Giới thiệu

**Orbital Simulation** là ứng dụng mô phỏng mạng vệ tinh quanh Trái Đất. Ứng dụng cho phép người dùng thêm vệ tinh, quan sát quỹ đạo, xem thông tin vệ tinh và mô phỏng liên kết tín hiệu giữa các vệ tinh trong mạng.

Tài liệu này mô tả cách sử dụng các chức năng chính của ứng dụng sau khi đã cài đặt thành công.

## 2. Khởi động ứng dụng

### Windows

Mở thư mục đã giải nén từ file `Orbital Simulation Windows.zip`, sau đó chạy:

```text
Orbital Simulation.exe
```

### macOS

Mở thư mục đã giải nén từ file `Orbital Simulation MacOS.zip`, sau đó chạy:

```text
Orbital Simulation.app
```

Sau khi khởi động, màn hình chính của ứng dụng sẽ xuất hiện.

## 3. Bố cục giao diện

Giao diện chính gồm hai khu vực:

| Khu vực | Chức năng |
| --- | --- |
| Mô hình 3D | Hiển thị Trái Đất, vệ tinh, quỹ đạo và đường liên kết tín hiệu. |
| Bảng điều khiển | Quản lý vệ tinh, nhập thông tin vệ tinh mới và tạo liên kết tín hiệu. |

## 4. Khu vực mô hình 3D

Khu vực bên trái hiển thị mô hình Trái Đất và các vệ tinh đang có trong hệ thống.

Các thao tác cơ bản:

- Kéo chuột trái để xoay góc nhìn.
- Cuộn chuột để phóng to hoặc thu nhỏ.
- Chọn vệ tinh trong danh sách để xem vị trí được đánh dấu trên mô hình.
- Quan sát đường liên kết tín hiệu khi chức năng liên kết được bật.

Mô hình 3D giúp người dùng hình dung trực quan vị trí vệ tinh và trạng thái kết nối trong mạng.

## 5. Bảng trạng thái nhiệm vụ

Panel **Trạng thái nhiệm vụ** hiển thị phản hồi của hệ thống sau mỗi thao tác.

Ví dụ:

- Hệ thống đã sẵn sàng.
- Vệ tinh đã được thêm thành công.
- Dữ liệu nhập chưa hợp lệ.
- Liên kết tín hiệu đang được theo dõi.
- Liên kết bị che khuất hoặc mất tín hiệu.

Khi thao tác không thành công, người dùng nên kiểm tra nội dung trong panel này để biết nguyên nhân.

## 6. Thêm vệ tinh mới

Chức năng **Phóng vệ tinh** dùng để thêm một vệ tinh mới vào mạng theo dõi.

### Thông tin cần nhập

| Trường | Ý nghĩa |
| --- | --- |
| Tên | Tên hoặc mã định danh của vệ tinh. |
| Vĩ độ | Vĩ độ ban đầu của vệ tinh, đơn vị độ. |
| Kinh độ | Kinh độ ban đầu của vệ tinh, đơn vị độ. |
| Độ cao | Độ cao quỹ đạo, đơn vị km. |
| Nghiêng | Độ nghiêng quỹ đạo, đơn vị độ. |
| Cấu hình | Kích cỡ hiển thị và màu đại diện của vệ tinh. |

### Các bước thực hiện

1. Nhập tên vệ tinh vào ô **Tên**.
2. Nhập các giá trị số cho **Vĩ độ**, **Kinh độ**, **Độ cao** và **Nghiêng**.
3. Nhập kích cỡ hiển thị nếu muốn thay đổi kích thước vệ tinh trên mô hình.
4. Chọn màu đại diện bằng ô chọn màu.
5. Nhấn nút **PHÓNG VỆ TINH**.

Nếu dữ liệu hợp lệ, vệ tinh mới sẽ xuất hiện trong mô hình 3D và trong danh sách **Mạng theo dõi**.

### Ví dụ dữ liệu

```text
Tên: SAT-01
Vĩ độ: 10
Kinh độ: 106
Độ cao: 550
Nghiêng: 45
Kích cỡ: 0.35
```

Lưu ý: các trường số chỉ nhập số, không nhập kèm đơn vị như `km` hoặc `độ`.

## 7. Xem danh sách vệ tinh

Panel **Mạng theo dõi** hiển thị các vệ tinh hiện có trong hệ thống.

Mỗi vệ tinh trong danh sách có các thông tin tóm tắt:

- Tên vệ tinh.
- Nhóm quỹ đạo, ví dụ LEO, MEO hoặc GEO.
- Độ cao.
- Vận tốc.
- Màu đại diện.

Người dùng có thể chọn một vệ tinh để xem chi tiết hoặc chọn nhiều vệ tinh để xóa cùng lúc.

## 8. Xem thông tin chi tiết vệ tinh

Khi chọn một vệ tinh trong **Mạng theo dõi**, ứng dụng sẽ:

- Đánh dấu vệ tinh tương ứng trên mô hình 3D.
- Hiển thị panel **Hồ sơ vệ tinh** ở góc dưới bên phải của vùng mô hình.
- Hiển thị các thông tin chi tiết như tên, độ cao, vận tốc và thông tin quỹ đạo.

Nếu chọn nhiều vệ tinh cùng lúc, panel chi tiết sẽ được ẩn để tránh hiển thị sai thông tin.

## 9. Mở liên kết tín hiệu

Chức năng **Liên kết tín hiệu** dùng để mô phỏng đường truyền giữa hai vệ tinh.

### Các bước thực hiện

1. Chọn vệ tinh nguồn tại ô **Điểm phát**.
2. Chọn vệ tinh đích tại ô **Điểm thu**.
3. Nhấn nút **MỞ LIÊN KẾT**.

Sau khi mở liên kết, ứng dụng sẽ tính toán đường truyền phù hợp trong mạng vệ tinh. Nếu tìm được đường truyền, đường liên kết sẽ được hiển thị trên mô hình 3D.

Nếu không tìm được đường truyền hợp lệ, panel trạng thái sẽ thông báo liên kết bị che khuất hoặc mất tín hiệu.

## 10. Ngắt liên kết tín hiệu

Để dừng việc hiển thị liên kết hiện tại, nhấn nút:

```text
NGẮT
```

Sau khi ngắt, đường liên kết trên mô hình 3D sẽ được xóa và hệ thống trở về trạng thái theo dõi bình thường.

## 11. Xóa vệ tinh

### Xóa vệ tinh được chọn

Các bước thực hiện:

1. Chọn một hoặc nhiều vệ tinh trong danh sách **Mạng theo dõi**.
2. Nhấn nút **XÓA CHỌN**.

Các vệ tinh được chọn sẽ bị xóa khỏi danh sách, mô hình 3D và dữ liệu lưu trữ của ứng dụng.

### Xóa toàn bộ vệ tinh

Nhấn nút:

```text
XÓA TẤT CẢ
```

Chức năng này xóa toàn bộ vệ tinh đang có trong hệ thống. Người dùng nên cân nhắc trước khi thực hiện vì dữ liệu sẽ bị xóa khỏi cơ sở dữ liệu của ứng dụng.

## 12. Một số lưu ý khi sử dụng

- Tên vệ tinh không được để trống.
- Không nên nhập trùng tên vệ tinh đã có.
- Các trường vĩ độ, kinh độ, độ cao, độ nghiêng và kích cỡ phải là số.
- Cần chọn đủ **Điểm phát** và **Điểm thu** trước khi mở liên kết tín hiệu.
- Điểm phát và điểm thu phải là hai vệ tinh khác nhau.
- Khi xóa vệ tinh đang nằm trong liên kết hiện tại, ứng dụng sẽ tự động ngắt liên kết.

## 13. Quy trình sử dụng đề xuất

Để kiểm tra đầy đủ các chức năng chính, người dùng có thể thực hiện theo thứ tự sau:

1. Mở ứng dụng.
2. Quan sát mô hình Trái Đất 3D.
3. Thêm một vệ tinh mới bằng panel **Phóng vệ tinh**.
4. Chọn vệ tinh trong **Mạng theo dõi** để xem thông tin chi tiết.
5. Chọn hai vệ tinh trong panel **Liên kết tín hiệu**.
6. Nhấn **MỞ LIÊN KẾT** để hiển thị đường truyền.
7. Xoay và phóng to mô hình 3D để quan sát liên kết.
8. Nhấn **NGẮT** để dừng liên kết.
9. Thử xóa một vệ tinh hoặc xóa toàn bộ mạng vệ tinh.

## 14. Kết luận

Orbital Simulation cung cấp các chức năng cơ bản để mô phỏng và quản lý một mạng vệ tinh ở mức trực quan. Giao diện được thiết kế theo hướng bảng điều khiển nhiệm vụ, giúp người dùng dễ quan sát trạng thái, thao tác với dữ liệu vệ tinh và theo dõi liên kết tín hiệu trong mô hình 3D.
