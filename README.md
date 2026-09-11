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
- **Quản lý nhân sự:** Thêm, sửa, xóa, tìm kiếm nhân viên, quản lý phòng ban, chức vụ.
- **Quản lý chấm công:** Cập nhật ngày công, quản lý nghỉ phép, đi trễ, làm thêm giờ.
- **Quản lý tiền lương:** Thiết lập mức lương cơ bản, phụ cấp, tính lương hàng tháng, in phiếu lương.
- **Báo cáo thống kê:** Thống kê nhân sự, báo cáo lương theo tháng/quý.

## 4. Cấu trúc thư mục (Mô hình 3 lớp)
```text
CNJ62-QuanLyNhanSu/
├── src/                    # Chứa toàn bộ mã nguồn Java
│   ├── model/              # (Model) Các lớp đối tượng (Entity/DTO) như NhanVien, ChamCong, Luong...
│   ├── dal/                # (Data Access Layer) Các lớp thao tác trực tiếp với CSDL (CRUD)
│   ├── bus/                # (Business Logic Layer) Các lớp xử lý nghiệp vụ, tính toán
│   ├── gui/                # (Graphical User Interface) Các lớp giao diện Java Swing
│   └── utils/              # Các lớp tiện ích như DBHelper (kết nối CSDL), Validation...
├── database/               # Chứa các file script SQL để tạo database và bảng
├── lib/                    # Chứa các thư viện bên ngoài (ví dụ: mysql-connector-java.jar)
└── README.md               # File thông tin dự án
```

## 5. Hướng dẫn cài đặt
1. **Cơ sở dữ liệu:** 
   - Cài đặt MySQL Server.
   - Chạy script SQL trong thư mục `database/` để tạo CSDL và các bảng.
2. **Cấu hình kết nối:**
   - Mở file `utils/DBHelper.java` (hoặc cấu hình tương đương) và cập nhật thông tin chuỗi kết nối, username, password của MySQL.
3. **Thư viện JDBC:**
   - Thêm file `mysql-connector-java.jar` trong thư mục `lib/` vào build path của dự án.
4. **Chạy ứng dụng:**
   - Chạy file Main tại lớp GUI chính (thường là `gui/MainForm.java` hoặc `gui/Login.java`).
