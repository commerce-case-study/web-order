package com.commerce.web.trade.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class MemberUnauthorizedException extends HttpStatusCodeException {
	
	private static final long serialVersionUID = 6335075841624074753L;

	public MemberUnauthorizedException() {
		super(HttpStatus.UNAUTHORIZED);
	}
}
