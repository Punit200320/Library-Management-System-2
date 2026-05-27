INSERT INTO books (title, author, isbn, genre, total_copies, available_copies, created_at)
VALUES
  ('The Hobbit', 'J.R.R. Tolkien', '9780261103344', 'Fantasy', 5, 4, CURRENT_TIMESTAMP),
  ('Clean Code', 'Robert C. Martin', '9780132350884', 'Software', 3, 3, CURRENT_TIMESTAMP),
  ('The Pragmatic Programmer', 'Andrew Hunt', '9780201616224', 'Software', 4, 4, CURRENT_TIMESTAMP);

INSERT INTO members (name, email, phone, membership_date, is_active, created_at)
VALUES
  ('Anita Singh', 'anita@example.com', '9876543210', CURRENT_TIMESTAMP, true, CURRENT_TIMESTAMP),
  ('Rahul Sharma', 'rahul@example.com', '9123456780', CURRENT_TIMESTAMP, true, CURRENT_TIMESTAMP);

INSERT INTO borrow_records (book_id, member_id, borrowed_at, due_date, status)
VALUES
  (1, 1, CURRENT_TIMESTAMP, DATEADD('DAY', 14, CURRENT_TIMESTAMP), 'BORROWED');
