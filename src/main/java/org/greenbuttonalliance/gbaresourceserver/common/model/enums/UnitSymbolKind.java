/*
 * Copyright (c) 2022 Green Button Alliance, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.greenbuttonalliance.gbaresourceserver.common.model.enums;

import java.util.EnumSet;

public enum UnitSymbolKind {
	V_A(61),
	W(38),
	V_A_R(63),
	V_A_H(71),
	W_H(72),
	V_A_RH(73),
	V(29),
	OHM(30),
	A(5),
	F(25),
	HEN(28),
	DEG_C(23),
	SEC(27),
	MIN(159),
	H(160),
	DEG(9),
	RAD(10),
	J(31),
	N(32),
	SIEMENS(53),
	NONE(0),
	HZ(33),
	G(3),
	PA(39),
	M(2),
	M2(41),
	M3(42),
	A2(69),
	A2_H(105),
	A2_S(70),
	A_H(106),
	A_PER_A(152),
	A_PER_M(103),
	A_S(68),
	B(79),
	B_M(113),
	BQ(22),
	BTU(132),
	BTU_PER_H(133),
	CD(8),
	CHAR(76),
	HZ_PER_SEC(75),
	CODE(114),
	COS_THETA(65),
	COUNT(111),
	FT3(119),
	FT3_COMPENSATED(120),
	FT3_COMPENSATED_PER_H(123),
	G_M2(78),
	G_PER_G(144),
	GY(21),
	HZ_PER_HZ(150),
	CHAR_PER_SEC(77),
	IMPERIAL_GAL(130),
	IMPERIAL_GAL_PER_H(131),
	J_PER_K(51),
	J_PER_KG(165),
	K(6),
	KAT(158),
	KG_M(47),
	KG_PER_M3(48),
	LITRE(134),
	LITRE_COMPENSATED(157),
	LITRE_COMPENSATED_PER_H(138),
	LITRE_PER_H(137),
	LITRE_PER_LITRE(143),
	LITRE_PER_SEC(82),
	LITRE_UNCOMPENSATED(156),
	LITRE_UNCOMPENSATED_PER_H(139),
	LM(35),
	LX(34),
	M2_PER_SEC(49),
	M3_COMPENSATED(167),
	M3_COMPENSATED_PER_H(126),
	M3_PER_H(125),
	M3_PER_SEC(45),
	M3_UNCOMPENSATED(166),
	M3_UNCOMPENSATED_PER_H(127),
	ME_CODE(118),
	MOL(7),
	MOL_PER_KG(147),
	MOL_PER_M3(145),
	MOL_PER_MOL(146),
	MONEY(80),
	M_PER_M(148),
	M_PER_M3(46),
	M_PER_SEC(43),
	M_PER_SEC2(44),
	OHM_M(102),
	PA_A(155),
	PA_G(140),
	PSI_A(141),
	PSI_G(142),
	Q(100),
	Q45(161),
	Q45_H(163),
	Q60(162),
	Q60_H(164),
	Q_H(101),
	RAD_PER_SEC(54),
	REV(154),
	REV_PER_SEC(4),
	SEC_PER_SEC(149),
	SR(11),
	STATUS(109),
	SV(24),
	T(37),
	THERM(169),
	TIMESTAMP(108),
	US_GAL(128),
	US_GAL_PER_H(129),
	V2(67),
	V2_H(104),
	V_A_H_PER_REV(117),
	V_A_RH_PER_REV(116),
	V_PER_HZ(74),
	V_PER_V(151),
	V_SEC(66),
	WB(36),
	W_H_PER_M3(107),
	W_H_PER_REV(115),
	W_PER_M_K(50),
	W_PER_SEC(81),
	W_PER_V_A(153),
	W_PER_W(168);

	public final int schemaValue;

	UnitSymbolKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static UnitSymbolKind getUnitSymbolKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(UnitSymbolKind.class).stream()
			.filter(usk -> usk.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + UnitSymbolKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
