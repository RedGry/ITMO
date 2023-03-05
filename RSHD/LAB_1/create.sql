CREATE OR REPLACE FUNCTION table_columns_info(t text, schema text)
RETURNS VOID AS
$$
    DECLARE
        new_tab CURSOR FOR (
            SELECT tab.relname, attr.attnum, attr.attname, typ.typname, des.description, idx.indexrelid::regclass as idxname FROM pg_class tab
                JOIN pg_namespace space on tab.relnamespace = space.oid
                JOIN pg_attribute attr on attr.attrelid = tab.oid
                JOIN pg_type typ on attr.atttypid = typ.oid
                LEFT JOIN pg_description des on des.objoid = tab.oid and des.objsubid = attr.attnum
                LEFT JOIN pg_index idx on tab.oid = idx.indrelid and attr.attnum = any(idx.indkey)
            WHERE tab.relname = t and attnum > 0 and space.nspname = schema
            ORDER BY attnum
            );
        table_count int;
    BEGIN
        SELECT COUNT(DISTINCT nspname) INTO table_count FROM pg_class tab JOIN pg_namespace space on tab.relnamespace = space.oid WHERE relname = t and space.nspname = schema;

        IF table_count < 1 THEN
            RAISE EXCEPTION 'Таблица "%" не найдена в схеме "%"!', t, schema;
        ELSE
	    RAISE NOTICE ' ';
            RAISE NOTICE 'Таблица: %', t;
            RAISE NOTICE ' ';
            RAISE NOTICE 'No.  Имя столбца      Атрибуты';
            RAISE NOTICE '---  --------------   -------------------------------------------------';

            FOR col in new_tab
            LOOP
                RAISE NOTICE '% % Type    :  %', RPAD(col.attnum::text, 5, ' '), RPAD(col.attname, 16, ' '), col.typname;
                RAISE NOTICE '% Commen  :  "%"', RPAD('⠀', 22, ' '), CASE WHEN col.description is null THEN '' ELSE col.description END;
                RAISE NOTICE '% Index   :  "%"', RPAD('⠀', 22, ' '), CASE WHEN col.idxname is null THEN '' ELSE col.idxname::text END;
	        RAISE NOTICE ' ';
            END LOOP;
	END IF;
    END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION schemas_table(t text)
RETURNS VOID AS
$$
    DECLARE
        schema_tab CURSOR FOR (
            SELECT tab.relname, space.nspname FROM pg_class tab
                JOIN pg_namespace space on tab.relnamespace = space.oid
            WHERE tab.relname = t
            ORDER BY space.nspname
            );
        table_count int;
        schema text;
    BEGIN
        SELECT COUNT(DISTINCT nspname) INTO table_count FROM pg_class tab JOIN pg_namespace space on tab.relnamespace = space.oid WHERE relname = t;

        IF table_count < 1 THEN
            RAISE EXCEPTION 'Таблица "%" не найдена!', t;
        ELSE
	    RAISE NOTICE ' ';
            RAISE NOTICE 'Выберите схему, с которой вы хотите получить данные: ';

            FOR col in schema_tab
            LOOP
                RAISE NOTICE '%', col.nspname;
            END LOOP;
            RAISE NOTICE ' ';
	    END IF;
    END
$$ LANGUAGE plpgsql;