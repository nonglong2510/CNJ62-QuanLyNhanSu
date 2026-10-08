-- Tùy chọn: chuyển mật khẩu cũ "123456" sang SHA-256.
-- Ứng dụng hỗ trợ dạng này và tự nâng cấp sang PBKDF2 khi đăng nhập thành công.

UPDATE TaiKhoan 
SET MatKhau = '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92' 
WHERE MatKhau = '123456';
