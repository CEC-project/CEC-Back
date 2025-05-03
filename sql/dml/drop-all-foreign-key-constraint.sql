DO $$
    DECLARE
        r RECORD;
    BEGIN
        FOR r IN
            SELECT tc.table_schema, tc.table_name, tc.constraint_name
            FROM information_schema.table_constraints AS tc
            WHERE tc.constraint_type = 'FOREIGN KEY'
            LOOP
                EXECUTE format('ALTER TABLE %I.%I DROP CONSTRAINT %I;',
                               r.table_schema, r.table_name, r.constraint_name);
            END LOOP;
    END
$$;