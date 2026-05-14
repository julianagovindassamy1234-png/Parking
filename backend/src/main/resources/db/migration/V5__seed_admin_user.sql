-- Default admin: admin@parking.com / Admin1234!
-- BCrypt hash of "Admin1234!"
INSERT INTO users (email, password_hash, first_name, last_name, role, level, is_active)
VALUES (
    'admin@parking.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'System',
    'Admin',
    'ADMIN',
    NULL,
    TRUE
);
