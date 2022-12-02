package org.greenbuttonalliance.gbaresourceserver.customer.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class AssetContainer extends Asset{
}
