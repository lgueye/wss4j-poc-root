package org.diveintojee.poc.wss4j.security;
/**
 * 
 */


import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author louis.gueye@gmail.com
 *
 */
@Component(ServerPasswordMatcherHandler.BEAN_ID)
public class ServerPasswordMatcherHandler implements CallbackHandler {

	public static final String BEAN_ID = "serverPasswordMatcherHandler"; 
	
	@Autowired
	private AuthenticationManager providerManager;
	
	/**
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		WSPasswordCallback pc = null;
		for (Callback callback : callbacks) {
			if (callback instanceof WSPasswordCallback) {
				pc = (WSPasswordCallback)callback; break;
			}
		}
		
		if (providerManager == null) throw new IllegalStateException("authenticationProvider should've been wired");
		
		if (pc != null && StringUtils.hasText(pc.getIdentifier())) {
			
			Authentication authentication  = providerManager.authenticate(new UsernamePasswordAuthenticationToken(pc.getIdentifier(), pc.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		}

	}

}
