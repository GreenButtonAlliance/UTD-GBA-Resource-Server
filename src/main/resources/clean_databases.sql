DROP SCHEMA usage CASCADE;
DROP SCHEMA customer CASCADE;
DELETE FROM public.flyway_schema_history;
DROP TYPE IF EXISTS public.enrollment_status;
DROP TYPE IF EXISTS public.currency;
DROP TYPE IF EXISTS public.unit_multiplier_kind;
DROP TYPE IF EXISTS public.unit_symbol_kind;
