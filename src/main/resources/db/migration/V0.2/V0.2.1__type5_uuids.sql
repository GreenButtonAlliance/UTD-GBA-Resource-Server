CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- update all UUIDs in database to be v5
WITH _ AS (
    UPDATE usage.interval_reading ir_outer SET block_uuid = uuid_generate_v5(
        uuid_ns_url(),
      (
        SELECT ib.self_link_href FROM usage.interval_reading ir_inner
          INNER JOIN usage.interval_block ib ON ir_inner.block_uuid = ib.uuid
        WHERE ir_inner.id = ir_outer.id
      )
    )
)
UPDATE usage.interval_block SET uuid = uuid_generate_v5(uuid_ns_url(), self_link_href);
WITH _ AS (
    UPDATE usage.interval_block ib_outer SET meter_reading_uuid = uuid_generate_v5(
        uuid_ns_url(),
      (
        SELECT mr.self_link_href FROM usage.interval_block ib_inner
          INNER JOIN usage.meter_reading mr ON ib_inner.meter_reading_uuid = mr.uuid
        WHERE ib_inner.uuid = ib_outer.uuid
      )
    )
)
UPDATE usage.meter_reading SET uuid = uuid_generate_v5(uuid_ns_url(), self_link_href);
