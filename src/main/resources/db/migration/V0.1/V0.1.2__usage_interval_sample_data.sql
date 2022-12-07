DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM usage.interval_block ib WHERE ib.uuid = '55df1aa4-c63f-5cd5-abd4-59164c448ee0') THEN
    INSERT INTO usage.interval_block
    VALUES ('55df1aa4-c63f-5cd5-abd4-59164c448ee0', NULL, '2012-03-02 05:00:00', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/173', 'self', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock', 'up', '2012-03-02 05:00:00'),
      ('e6d44d83-a357-58df-b8fb-296cebf4fca0', NULL, '2012-03-03 05:00:00', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/174', 'self', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock', 'up', '2012-03-03 05:00:00'),
      ('549cca03-0083-5fd5-ab90-1fbadf535fd6', NULL, '2012-03-04 05:00:00', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock/175', 'self', '/espi/1_1/resource/RetailCustomer/9B6C7066/UsagePoint/5446AF3F/MeterReading/01/IntervalBlock', 'up', '2012-03-04 05:00:00');

    INSERT INTO usage.interval_reading
    VALUES (1, 974, 1330578000, 900, 282, NULL, NULL, NULL, '55df1aa4-c63f-5cd5-abd4-59164c448ee0'),
      (2, 965, 1330578900, 900, 323, NULL, NULL, NULL, '55df1aa4-c63f-5cd5-abd4-59164c448ee0'),
      (3, 922, 1334445553, 900, 350, NULL, NULL, NULL, '55df1aa4-c63f-5cd5-abd4-59164c448ee0'),
      (4, 960, 1330234234, 900, 402, NULL, NULL, NULL, 'e6d44d83-a357-58df-b8fb-296cebf4fca0'),
      (5, 978, 1330987644, 900, 299, NULL, NULL, NULL, '549cca03-0083-5fd5-ab90-1fbadf535fd6');

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
