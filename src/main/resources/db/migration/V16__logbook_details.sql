ALTER TABLE logbook_details ADD COLUMN created_at DATETIME;
ALTER TABLE logbook_details MODIFY COLUMN entry TEXT;
ALTER TABLE logbook_details ADD FOREIGN KEY (logbook_id) REFERENCES logbook(id) ON DELETE CASCADE;