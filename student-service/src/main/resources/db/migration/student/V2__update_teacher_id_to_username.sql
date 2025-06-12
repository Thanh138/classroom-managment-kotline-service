ALTER TABLE student_schema.classes
    RENAME COLUMN teacher_id TO teacher_username;

ALTER TABLE student_schema.classes
ALTER COLUMN teacher_username TYPE VARCHAR(50);

-- Update the index
DROP INDEX IF EXISTS student_schema.idx_class_teacher;
CREATE INDEX idx_class_teacher ON student_schema.classes(teacher_username);