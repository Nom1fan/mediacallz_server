package com.mediacallz.server.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//	ONLINE,
//	OFFLINE,
@RequiredArgsConstructor
public enum UserStatus {

	UNREGISTERED("UNREGISTERED"),
	REGISTERED("REGISTERED");

	@Getter
	private final String str;

}
