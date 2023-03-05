SELECT CASE WHEN "ИМЯ" IS NULL THEN '' ELSE "ИМЯ" END,
       CASE WHEN "ФАМИЛИЯ" IS NULL THEN '' ELSE "ФАМИЛИЯ" END,
       CASE WHEN "ОТЧЕСТВО" IS NULL THEN '' ELSE "ОТЧЕСТВО" END,
       COUNT(*) AS "Кол-во"
FROM "Н_ЛЮДИ"
WHERE "Н_ЛЮДИ"."ОТЧЕСТВО" IN (
    SELECT "ОТЧЕСТВО" FROM "Н_ЛЮДИ"
        JOIN "Н_ОБУЧЕНИЯ" ON "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД" = "Н_ЛЮДИ"."ИД"
        JOIN "Н_УЧЕНИКИ" USING ("ВИД_ОБУЧ_ИД", "ЧЛВК_ИД")
        JOIN "Н_ПЛАНЫ" ON "Н_УЧЕНИКИ"."ПЛАН_ИД" = "Н_ПЛАНЫ"."ИД"
        JOIN "Н_ФОРМЫ_ОБУЧЕНИЯ" ON "Н_ПЛАНЫ"."ФО_ИД" = "Н_ФОРМЫ_ОБУЧЕНИЯ"."ИД"
    WHERE "Н_ФОРМЫ_ОБУЧЕНИЯ"."НАИМЕНОВАНИЕ" = 'Очная' AND "ОТЧЕСТВО" IS NOT NULL
    GROUP BY "Н_ЛЮДИ"."ОТЧЕСТВО"
    HAVING COUNT("Н_ЛЮДИ"."ОТЧЕСТВО") < 10
    )
GROUP BY GROUPING SETS ("ОТЧЕСТВО", "ФАМИЛИЯ", "ИМЯ", ())
ORDER BY "ИМЯ" DESC, "ФАМИЛИЯ" DESC, "ОТЧЕСТВО" DESC, "Кол-во"