# Đề tài CNJ62: Quản lý nhân sự, chấm công và tiền lương

Dự án này là ứng dụng desktop xây dựng bằng **Java Swing**, **JDBC** và hệ quản trị cơ sở dữ liệu **MySQL**, ứng dụng mô hình kiến trúc 3 lớp (3-tier architecture).

## 1. Giới thiệu
Phần mềm cung cấp giải pháp toàn diện cho việc:
- Quản lý hồ sơ nhân viên.
- Theo dõi chấm công hàng ngày/tháng.
- Tính toán và quản lý tiền lương tự động.

## 2. Công nghệ sử dụng
- **Ngôn ngữ lập trình:** Java (Java SE)
- **Giao diện người dùng:** Java Swing
- **Cơ sở dữ liệu:** MySQL
- **Kết nối CSDL:** JDBC
- **Kiến trúc phần mềm:** Mô hình 3 lớp (3-tier: DAL - BUS - GUI)

## 3. Các chức năng chính
- **Quản lý nhân sự:** CRUD phòng ban/chức vụ, thêm/sửa/tìm nhân viên; nghỉ việc được lưu trạng thái để giữ lịch sử.
- **Quản lý chấm công:** Ghi nhận theo ngày các trạng thái đi làm, đi trễ, nghỉ phép/không phép; tạo và duyệt đơn xin phép; tổng hợp công theo tháng và xuất CSV. Màn hình điểm danh hỗ trợ tìm nhân viên, lọc trạng thái và tải nhanh ngày hiện tại. Khi duyệt đơn, hệ thống tự ghi nhận nghỉ phép từ thứ Hai đến thứ Sáu trong cùng giao dịch; nếu ngày đó đã có chấm công, đơn không được duyệt và dữ liệu hiện có được giữ nguyên. Theo quy tắc demo hiện tại, ngày nghỉ phép không được tính lương.
- **Tra cứu:** Danh sách đơn xin phép hỗ trợ tìm theo mã đơn/nhân viên/lý do và lọc trạng thái; báo cáo công hỗ trợ tìm theo mã nhân viên, tên hoặc phòng ban. Xuất CSV báo cáo công sẽ xuất các dòng đang hiển thị sau khi lọc.
- **Quản lý tiền lương:** Chọn tháng/năm, tính và lưu bảng lương, xem phiếu lương, in/xuất CSV; dữ liệu được đọc từ MySQL.
- **Phân quyền:** Admin quản trị toàn bộ hệ thống. User chỉ xem hồ sơ, chấm công và phiếu lương của chính mình, được gửi đơn xin phép và đổi mật khẩu.

### Quy tắc tính lương demo
- Lương theo công = `5.000.000 × hệ số lương / 22 × số ngày Đi làm hoặc Đi trễ`.
- Phụ cấp chức vụ được chia theo 22 ngày công chuẩn và prorate theo số ngày công, tối đa 22 ngày.
- Thưởng và khấu trừ mặc định bằng 0; chưa tính OT, thuế TNCN, bảo hiểm, tạm ứng hay lịch ngày nghỉ/lễ.
- Tính lại một kỳ sẽ ghi đè dữ liệu bảng lương của kỳ đó; ứng dụng yêu cầu xác nhận trước khi lưu. Nhân viên nghỉ việc không được tính lại trong kỳ.
- Báo cáo tổng hợp công dùng mốc demo 22 ngày/tháng; cột công còn lại chỉ là chênh lệch tham khảo, không thay thế lịch làm việc/ngày nghỉ thực tế.
- Gửi email hàng loạt chưa tích hợp SMTP. Các khoản khấu trừ thuế/bảo hiểm chưa được triển khai và không bị giả lập.

## 4. Cấu trúc thư mục (Mô hình 3 lớp)
```text
CNJ62-QuanLyNhanSu/
├── src/                    # Chứa toàn bộ mã nguồn Java
│   ├── model/              # (Model) Các lớp đối tượng (Entity/DTO) như NhanVien, ChamCong, Luong...
│   ├── dal/                # (Data Access Layer) Các lớp thao tác trực tiếp với CSDL (CRUD)
│   ├── bus/                # (Business Logic Layer) Các lớp xử lý nghiệp vụ, tính toán
│   ├── gui/                # (Graphical User Interface) Các lớp giao diện Java Swing
│   ├── utils/              # DBHelper, chính sách truy cập và tiện ích
│   └── test/java/          # Unit test cho quy tắc nghiệp vụ
├── database/               # Chứa các file script SQL để tạo database và bảng
├── lib/                    # Chứa các thư viện bên ngoài (ví dụ: mysql-connector-java.jar)
└── README.md               # File thông tin dự án
```

## 5. Hướng dẫn cài đặt
1. Cài JDK 11 trở lên, Maven, MySQL/XAMPP và VS Code.
2. Mở thư mục `CNJ62-QuanLyNhanSu` trong VS Code, cài các extension được khuyến nghị, chờ VS Code import Maven project.
3. Khởi động MySQL. Profile `demo` mặc định kết nối `localhost:3307` với user `root` và mật khẩu rỗng.
4. Mở **Database Client** (extension `cweijan.vscode-mysql-client2`), tạo kết nối MySQL tới `localhost:3307` (mặc định XAMPP; thay cổng nếu máy bạn dùng cổng khác). Không lưu mật khẩu trong workspace.
5. Để làm mới từ đầu đúng database `quanlynhansu`, mở và chạy `database/reset_quanlynhansu.sql`, sau đó chạy toàn bộ `database/quan_ly_nhan_su.sql`. **Bước reset xóa vĩnh viễn mọi bảng và dữ liệu trong `quanlynhansu`**; tuyệt đối không chạy nếu cần giữ dữ liệu. Không chạy reset trên database khác.
6. Nếu database chưa cần reset, chỉ chạy `database/quan_ly_nhan_su.sql`: script tạo database/bảng còn thiếu và thêm dữ liệu demo bằng `INSERT IGNORE`, không xóa hay ghi đè bản ghi hiện có.
7. Với profile `demo`, có thể bỏ qua SQL thủ công: ứng dụng tự khởi tạo database `quanlynhansu`, các bảng và dữ liệu mẫu khi khởi động. Tài khoản MySQL demo cần quyền `CREATE DATABASE`, `CREATE`, `INSERT` và `UPDATE`.
8. Nhấn `F5` và chọn **Run HR Management System** (hoặc chạy task **Build HR management app**).
9. Đăng nhập demo: `admin` / `admin123`, `user1` / `user123`, `user2` / `user123`.

### Cấu hình triển khai

- Profile mặc định là `HR_DB_PROFILE=demo`. Các giá trị mặc định `localhost:3307`, `root` và mật khẩu rỗng chỉ dành cho máy demo; ứng dụng tự tạo schema và seed dữ liệu mẫu.
- Môi trường triển khai phải đặt `HR_DB_PROFILE=production` cùng các biến `HR_DB_HOST`, `HR_DB_PORT`, `HR_DB_NAME`, `HR_DB_USER`, `HR_DB_PASSWORD`. Profile này không có giá trị mặc định cho kết nối, từ chối tài khoản `root`/mật khẩu rỗng và yêu cầu TLS `VERIFY_IDENTITY` (chứng thư máy chủ phải hợp lệ và khớp hostname). Không đặt mật khẩu trong source code, README, workspace hoặc tham số dòng lệnh; lấy bí mật từ secret manager của môi trường chạy.
- Profile `production` chỉ kiểm tra kết nối, **không** tạo database, bảng hay tài khoản mẫu. Tài khoản ứng dụng chỉ nên được cấp đúng quyền cần thiết trên database đã được chuẩn bị. Các SQL hiện tại chưa phải migration có phiên bản; `database/quan_ly_nhan_su.sql` có dữ liệu mẫu và không nên chạy nguyên trạng trên dữ liệu thật. Cần rà soát schema/script, sao lưu và áp dụng thay đổi schema có kiểm soát trước khi nâng cấp ứng dụng.
- Chưa có công cụ migration/versioning schema tự động. Trước mỗi lần triển khai, sao lưu nhất quán database (ví dụ `mysqldump --single-transaction`), lưu bản sao ở nơi tách biệt/được bảo vệ, và thử khôi phục vào môi trường staging định kỳ. Chỉ khôi phục sau khi xác nhận đúng máy chủ/database đích; không dùng script reset trên dữ liệu cần giữ.
- Quyền Admin/User hiện được giới hạn trong ứng dụng và các truy vấn theo mã nhân viên; database không có user/role riêng cho từng nhân viên. Người có thông tin kết nối MySQL trực tiếp có thể vượt qua phân quyền giao diện. Đây là giới hạn của bản demo, không thay thế kiểm soát truy cập và kiểm toán ở cấp dịch vụ/database cho triển khai thực tế.

### Quy tắc nghiệp vụ và vận hành

- Công thức lương demo: `5.000.000 × hệ số lương / 22 × số ngày Đi làm hoặc Đi trễ`. Phụ cấp chức vụ prorate theo `min(số ngày công, 22) / 22`. Nghỉ phép đã duyệt, OT, thuế, bảo hiểm và ngày lễ không được đưa vào tính lương.
- Duyệt đơn nghỉ tạo chấm công “Nghỉ phép” từ thứ Hai đến thứ Sáu trong cùng transaction. Nếu ngày làm việc đã có bản ghi chấm công thì từ chối duyệt; cuối tuần không tạo bản ghi nghỉ. Quy tắc này chưa xử lý lịch ngày lễ hoặc lịch làm việc riêng của từng nhân viên.
- Đăng nhập và kiểm tra/khởi tạo database chạy nền để tránh chặn cửa sổ đăng nhập. Các màn hình dữ liệu được khởi tạo khi mở; tải danh sách nhân viên cũng chạy nền và giữ dữ liệu đang hiển thị nếu lần tải mới lỗi. Một số màn hình khác vẫn có thể chờ trong lần mở đầu tiên do truy vấn chưa được chuyển hết sang nền.
- Ứng dụng là desktop dùng JDBC trực tiếp, chưa có cơ chế triển khai nhiều máy khách an toàn qua dịch vụ trung gian, migration tự động, lịch backup tự động hay cam kết thời gian hỗ trợ. Các việc này cần được bổ sung theo yêu cầu vận hành thực tế; không thể bảo đảm tuyệt đối thời gian chạy 5 năm chỉ bằng các thay đổi trong ứng dụng.

### Kiểm thử

Chạy kiểm thử nghiệp vụ độc lập, không cần MySQL:

```sh
mvn test
```

Hiện test bao phủ công thức lương demo, kiểm tra hồ sơ nhân viên, quy tắc quyền ở ứng dụng và quy tắc ngày làm việc khi duyệt nghỉ. Transaction/khóa dữ liệu chống duyệt trùng chưa có kiểm thử tích hợp tự động; cần kiểm thử thêm trên MySQL staging trước khi dùng dữ liệu thật.

Có thể cấu hình MySQL demo bằng các biến `HR_DB_HOST`, `HR_DB_PORT`, `HR_DB_NAME`, `HR_DB_USER`, `HR_DB_PASSWORD`. Nếu đổi tên database qua `HR_DB_NAME`, hãy cập nhật script SQL tương ứng. Tài khoản demo được tạo từ dữ liệu mẫu; khi đăng nhập thành công, ứng dụng nâng cấp mật khẩu legacy sang PBKDF2-HMAC-SHA256. Không dùng tài khoản hoặc mật khẩu demo trong môi trường thật.
