DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM usage.interval_block ib WHERE ib.uuid = 'FE9A61BB-6913-42D4-88BE-9634A218EF53') THEN
    INSERT INTO usage.interval_block
    VALUES ('FE9A61BB-6913-42D4-88BE-9634A218EF53', NULL, '2012-03-02 05:00:00', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/173', 'self', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock', 'up', '2012-03-02 05:00:00'),
      ('0E49B341-CABD-445B-8F88-7ACF791B1863', NULL, '2012-03-03 05:00:00', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/174', 'self', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock', 'up', '2012-03-03 05:00:00'),
      ('1A1A106F-7108-4532-A5A6-FD48F7DA3B8D', NULL, '2012-03-04 05:00:00', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/175', 'self', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock', 'up', '2012-03-04 05:00:00');

    INSERT INTO usage.interval_reading
    VALUES (1, 974, 1330578000, 900, 282, NULL, NULL, NULL, 'FE9A61BB-6913-42D4-88BE-9634A218EF53'),
      (2, 965, 1330578900, 900, 323, NULL, NULL, NULL, 'FE9A61BB-6913-42D4-88BE-9634A218EF53'),
      (3, 922, 1334445553, 900, 350, NULL, NULL, NULL, 'FE9A61BB-6913-42D4-88BE-9634A218EF53'),
      (4, 960, 1330234234, 900, 402, NULL, NULL, NULL, '0E49B341-CABD-445B-8F88-7ACF791B1863'),
      (5, 978, 1330987644, 900, 299, NULL, NULL, NULL, '1A1A106F-7108-4532-A5A6-FD48F7DA3B8D');

    INSERT INTO usage.reading_quality
    VALUES (1, 'VALID'::usage.quality_of_reading, 1),
      (2, 'RAW'::usage.quality_of_reading, 1),
      (3, 'DERIVED'::usage.quality_of_reading, 1),
      (4, 'OTHER'::usage.quality_of_reading, 2),
      (5, 'VALID'::usage.quality_of_reading, 2),
      (6, 'QUESTIONABLE'::usage.quality_of_reading, 3),
      (7, 'VALID'::usage.quality_of_reading, 3);
  END IF;
END $$;
