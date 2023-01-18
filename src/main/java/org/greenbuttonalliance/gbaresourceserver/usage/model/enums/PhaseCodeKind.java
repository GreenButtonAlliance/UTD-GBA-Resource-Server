/*
 * Copyright (c) 2022-2023 Green Button Alliance, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.greenbuttonalliance.gbaresourceserver.usage.model.enums;

import java.util.EnumSet;

public enum PhaseCodeKind {
	ABCN(225),
	ABC(224),
	ABN(193),
	ACN(41),
	BCN(97),
	AB(132),
	AC(96),
	BC(66),
	AN(129),
	BN(65),
	CN(33),
	A(128),
	B(64),
	C(32),
	N(16),
	S2N(272),
	S12NEUTRAL(784),
	S1N(528),
	S2(256),
	S12(768),
	S12N(769),
	NONE(0),
	A_TO_A_V(136),
	BA_V(72),
	CA_V(40),
	NG(17),
	S1(512);

	public final int schemaValue;

	PhaseCodeKind(int schemaValue) {
		this.schemaValue = schemaValue;
	}

	public static PhaseCodeKind getPhaseCodeKindFromSchemaValue(int schemaValue) {
		return EnumSet.allOf(PhaseCodeKind.class).stream()
			.filter(pck -> pck.schemaValue == schemaValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No " + PhaseCodeKind.class.getCanonicalName() + " with schemaValue " + schemaValue));
	}
}
