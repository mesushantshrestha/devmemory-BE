INSERT INTO app_user (id, google_sub, email, name)
VALUES ('00000000-0000-0000-0000-000000000001', 'local-dev', 'local@devmemory', 'Local Dev')
ON CONFLICT (google_sub) DO NOTHING;