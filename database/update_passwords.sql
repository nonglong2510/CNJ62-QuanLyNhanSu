-- Chạy script này nếu bạn muốn mã hóa toàn bộ mật khẩu cũ (từ "123456" thành mã SHA-256)
-- Mật khẩu "123456" sau khi băm SHA-256 sẽ là chuỗi 64 ký tự bên dưới:

UPDATE TaiKhoan 
SET MatKhau = '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92' 
WHERE MatKhau = '123456';
