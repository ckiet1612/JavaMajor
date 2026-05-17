# BÁO CÁO KẾT QUẢ ĐÁNH GIÁ ĐỊNH TUYẾN

## 1. Mục đích báo cáo

Báo cáo này trình bày kết quả đánh giá chức năng định tuyến tín hiệu trong chương trình **Orbital Simulation** khi số lượng vệ tinh trong mạng thay đổi.

Mục tiêu chính của thử nghiệm là kiểm tra xem chương trình có thể tìm được đường truyền giữa hai vệ tinh hay không, đồng thời quan sát sự thay đổi của tuyến đường khi mạng có thêm các vệ tinh trung gian.

## 2. Chức năng được đánh giá

Chức năng được đánh giá là **Liên kết tín hiệu** trong ứng dụng.

Khi người dùng chọn một vệ tinh làm **Điểm phát** và một vệ tinh làm **Điểm thu**, chương trình sẽ:

1. Kiểm tra khả năng truyền tín hiệu trực tiếp giữa các vệ tinh.
2. Loại bỏ các liên kết bị Trái Đất che khuất.
3. Tìm đường truyền ngắn nhất qua các vệ tinh trung gian bằng thuật toán Dijkstra.
4. Hiển thị tuyến đường tìm được trên mô hình 3D.

Nếu không có đường truyền hợp lệ, chương trình sẽ thông báo liên kết bị che khuất hoặc mất tín hiệu.

## 3. Kịch bản thử nghiệm

Tuyến định tuyến được chọn để đánh giá:

```text
Điểm phát: Starlink-1
Điểm thu: Starlink-5
```

Đây là hai vệ tinh thuộc nhóm LEO, có độ cao thấp hơn so với GEO và MEO. Do đó, khả năng nhìn thấy trực tiếp giữa hai vệ tinh bị giới hạn nhiều hơn. Kịch bản này phù hợp để kiểm tra vai trò của các vệ tinh trung gian trong quá trình định tuyến.

Kết quả được ghi nhận tại thời điểm ban đầu sau khi khởi động chương trình, trước khi quỹ đạo thay đổi đáng kể theo thời gian mô phỏng.

## 4. Dữ liệu thử nghiệm

Các bộ dữ liệu được tạo bằng cách thay đổi số lượng vệ tinh có trong mạng. Điểm phát và điểm thu luôn được giữ cố định là `Starlink-1` và `Starlink-5`.

| Lần thử | Số vệ tinh trong mạng | Danh sách vệ tinh sử dụng |
| --- | ---: | --- |
| 1 | 2 | Starlink-1, Starlink-5 |
| 2 | 3 | Starlink-1, Starlink-3, Starlink-5 |
| 3 | 4 | Starlink-1, Starlink-2, Starlink-4, Starlink-5 |
| 4 | 5 | Starlink-1, Starlink-2, Starlink-3, Starlink-4, Starlink-5 |
| 5 | 7 | Starlink-1 đến Starlink-5, GEO-Asia, GEO-America |
| 6 | 10 | Bộ dữ liệu mẫu đầy đủ gồm GEO, LEO và MEO |

## 5. Kết quả định tuyến

| Lần thử | Số vệ tinh | Kết quả | Tuyến đường tìm được | Số chặng | Tổng chiều dài xấp xỉ |
| --- | ---: | --- | --- | ---: | ---: |
| 1 | 2 | Không thành công | Không có tuyến hợp lệ | - | - |
| 2 | 3 | Không thành công | Không có tuyến hợp lệ | - | - |
| 3 | 4 | Không thành công | Không có tuyến hợp lệ | - | - |
| 4 | 5 | Thành công | Starlink-1 → Starlink-2 → Starlink-3 → Starlink-4 → Starlink-5 | 4 | 14,019.71 km |
| 5 | 7 | Thành công | Starlink-1 → Starlink-2 → Starlink-3 → Starlink-4 → Starlink-5 | 4 | 14,019.71 km |
| 6 | 10 | Thành công | Starlink-1 → Starlink-2 → Starlink-3 → Starlink-4 → Starlink-5 | 4 | 14,019.71 km |

## 6. Nhận xét kết quả

Với 2 vệ tinh, chương trình không tìm được tuyến vì `Starlink-1` và `Starlink-5` cách nhau quá xa trên quỹ đạo LEO. Tín hiệu trực tiếp giữa hai vệ tinh bị Trái Đất che khuất.

Với 3 vệ tinh, việc thêm `Starlink-3` vẫn chưa đủ để tạo tuyến. Khoảng cách góc giữa các vệ tinh còn lớn, nên các liên kết trung gian vẫn không thỏa điều kiện line-of-sight.

Với 4 vệ tinh, mạng có thêm vệ tinh trung gian nhưng vẫn bị thiếu `Starlink-3`. Do đó vẫn tồn tại khoảng trống lớn giữa `Starlink-2` và `Starlink-4`, làm tuyến bị đứt.

Với 5 vệ tinh LEO liên tiếp, chương trình tìm được tuyến thành công:

```text
Starlink-1 → Starlink-2 → Starlink-3 → Starlink-4 → Starlink-5
```

Mỗi chặng trong tuyến là liên kết giữa hai vệ tinh gần nhau, nên không bị Trái Đất che khuất. Đây là trường hợp thể hiện rõ vai trò của vệ tinh trung gian trong mạng truyền tín hiệu.

Khi tăng lên 7 và 10 vệ tinh, tuyến đường không thay đổi. Lý do là thuật toán Dijkstra chọn đường có tổng khoảng cách ngắn nhất. Các vệ tinh GEO hoặc MEO có thể hỗ trợ vùng phủ lớn hơn, nhưng khoảng cách truyền qua các vệ tinh này dài hơn nhiều so với chuỗi LEO gần nhau. Vì vậy tuyến LEO vẫn là kết quả tối ưu trong kịch bản này.

## 7. Đánh giá

Kết quả thử nghiệm cho thấy chức năng định tuyến hoạt động đúng theo logic đã thiết kế:

- Chương trình không tạo tuyến khi tín hiệu bị Trái Đất che khuất.
- Chương trình tìm được tuyến khi có đủ vệ tinh trung gian phù hợp.
- Khi có nhiều tuyến khả thi, chương trình ưu tiên tuyến có tổng khoảng cách ngắn hơn.
- Việc tăng số lượng vệ tinh không luôn làm thay đổi tuyến đường, nếu các vệ tinh thêm vào không tạo ra đường đi ngắn hơn.

Một điểm đáng chú ý là số lượng vệ tinh không phải yếu tố duy nhất quyết định khả năng định tuyến. Vị trí và độ cao của vệ tinh cũng ảnh hưởng trực tiếp đến kết quả. Trong thử nghiệm này, chỉ khi các vệ tinh LEO được bố trí đủ gần nhau thì tuyến từ `Starlink-1` đến `Starlink-5` mới được thiết lập thành công.

## 8. Kết luận

Qua quá trình thử nghiệm với số lượng vệ tinh khác nhau, chức năng định tuyến của chương trình cho kết quả hợp lý và dễ quan sát trên giao diện 3D.

Đối với tuyến `Starlink-1` đến `Starlink-5`, chương trình cần ít nhất 5 vệ tinh LEO liên tiếp để tạo được đường truyền ổn định tại thời điểm thử nghiệm. Khi mạng có thêm vệ tinh GEO và MEO, tuyến tối ưu vẫn đi qua chuỗi LEO vì tổng chiều dài truyền tín hiệu ngắn hơn.

Kết quả này cho thấy chương trình đã mô phỏng được hai yếu tố quan trọng trong định tuyến vệ tinh: điều kiện nhìn thấy trực tiếp giữa các vệ tinh và lựa chọn đường truyền ngắn nhất trong mạng.
