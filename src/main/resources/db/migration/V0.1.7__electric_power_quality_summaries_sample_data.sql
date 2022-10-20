DO $$
  BEGIN
    IF NOT EXISTS (SELECT FROM usage.electric_power_quality_summaries epqs WHERE epqs.uuid = '5e0cdf55-9460-43db-8b06-d6a7bca320e5') THEN
      INSERT INTO usage.electric_power_quality_summaries
      VALUES ('description','2022-03-01 05:00:00','/espi/1_1/resource/ElectricPowerQualitySummary/174', 'self', '/espi/1_1/resource/ElectricPowerQualitySummary', 'up', '2022-03-01 05:00:00', '5e0cdf55-9460-43db-8b06-d6a7bca320e5', 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, '0e526507-2570-456f-8a2b-849ed5251750'),
             ('description','2022-03-02 05:00:00','/espi/1_1/resource/ElectricPowerQualitySummary/175', 'self', '/espi/1_1/resource/ElectricPowerQualitySummary', 'up', '2022-03-02 05:00:00', '77912236-2ab4-4c02-8027-37c090cc17a6', 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, '02b7f931-a223-4c0f-8f95-714d8a0f6030'),
             ('description','2022-03-03 05:00:00','/espi/1_1/resource/ElectricPowerQualitySummary/176', 'self', '/espi/1_1/resource/ElectricPowerQualitySummary', 'up', '2022-03-03 05:00:00', '668908f3-bed8-4cfd-999a-3ce113e356a3', 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, '62118299-20b7-466b-b4af-3c52e8513eee');
    END IF;
  END $$;
