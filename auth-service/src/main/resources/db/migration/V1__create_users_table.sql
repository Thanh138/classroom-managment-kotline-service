-- Create the auth schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS auth;

-- Create users table
CREATE TABLE IF NOT EXISTS auth.users (
                                          id BIGSERIAL PRIMARY KEY,
                                          username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

-- Create roles table
CREATE TABLE IF NOT EXISTS auth.roles (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(20) UNIQUE NOT NULL
    );

-- Create user_roles join table
CREATE TABLE IF NOT EXISTS auth.user_roles (
                                               user_id BIGINT NOT NULL,
                                               role_id BIGINT NOT NULL,
                                               PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES auth.users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES auth.roles (id) ON DELETE CASCADE
    );

-- Insert default roles
INSERT INTO auth.roles (name) VALUES
                                  ('ROLE_ADMIN'),
                                  ('ROLE_TEACHER'),
                                  ('ROLE_STUDENT')
    ON CONFLICT (name) DO NOTHING;

-- Assign admin role to the admin user
INSERT INTO auth.user_roles (user_id, role_id)
SELECT u.id, r.id
FROM auth.users u
         CROSS JOIN auth.roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
    ON CONFLICT DO NOTHING;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON auth.users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON auth.users (email);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON auth.user_roles (user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON auth.user_roles (role_id);

-- Add a function to update the updated_at timestamp
CREATE OR REPLACE FUNCTION auth.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create a trigger to automatically update the updated_at column
DROP TRIGGER IF EXISTS update_users_updated_at ON auth.users;
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON auth.users
    FOR EACH ROW
    EXECUTE FUNCTION auth.update_updated_at_column();