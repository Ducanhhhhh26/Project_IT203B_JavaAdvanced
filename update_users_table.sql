USE meeting_room_mgmt;
ALTER TABLE users ADD COLUMN phone VARCHAR(50);
ALTER TABLE users ADD COLUMN department VARCHAR(100);
ALTER TABLE bookings ADD COLUMN total_cost DECIMAL(10, 2) DEFAULT 0.00;
ALTER TABLE bookings ADD COLUMN support_staff_id INT DEFAULT NULL;
ALTER TABLE bookings ADD CONSTRAINT fk_support_staff FOREIGN KEY (support_staff_id) REFERENCES users(id) ON DELETE SET NULL;
