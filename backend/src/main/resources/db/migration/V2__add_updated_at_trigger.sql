-- Create or replace the function (safe to run multiple times)
CREATE
OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    IF
NEW IS DISTINCT FROM OLD THEN
        NEW.updated_at = now();
END IF;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;


-- USERS --------------------------------------------------

DROP TRIGGER IF EXISTS trigger_set_updated_at ON users;

CREATE TRIGGER trigger_set_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();


-- PATTERNS ----------------------------------------------

DROP TRIGGER IF EXISTS trigger_set_updated_at ON patterns;

CREATE TRIGGER trigger_set_updated_at
    BEFORE UPDATE
    ON patterns
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();


-- FILES -------------------------------------------------

DROP TRIGGER IF EXISTS trigger_set_updated_at ON files;

CREATE TRIGGER trigger_set_updated_at
    BEFORE UPDATE
    ON files
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();