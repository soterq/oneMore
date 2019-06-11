package com.unifun.voice.endpoint;

import com.unifun.voice.helpers.Constants;
import com.unifun.voice.jwt.LoginUser;
import com.unifun.voice.jwt.TokenManager;
import com.unifun.voice.orm.model.SessionTable;
import com.unifun.voice.orm.repository.SessionTableRepository;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

@WebFilter(urlPatterns = "/*", filterName = "RequestsFilter")
@ApplicationScoped
public class ReqFilter implements Filter {
	@Inject
	SessionTableRepository sessionTableRepository;
	private final String auth_header = "Authorization";
	SessionTable sessionTable;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 final HttpServletResponse servletResponse = (HttpServletResponse) response;
	        final HttpServletRequest servletRequest = (HttpServletRequest) request;

	        servletResponse.setHeader("Access-Control-Allow-Origin", "*");
	        servletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
	        servletResponse.setHeader("Access-Control-Max-Age", "1000");
	        servletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, origin, authorization, accept, client-security-token, Session-Token");
			if (servletRequest.getMethod().equals(HttpMethod.OPTIONS)) {
	            servletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
	            return;
	        }


			sessionTableRepository.checkForExpiredTokensAndRemoveThem();
			if(!LoginUser.getRefreshTokenInstance().isEmpty()) {
				String ipAddr = servletRequest.getRemoteAddr();
				String authBearer = servletRequest.getHeader(auth_header).substring(7);
				TokenManager tokenManager = new TokenManager();
				sessionTable = sessionTableRepository.getNewSession(ipAddr, authBearer);
				if (tokenManager.verifyIfTokenIsValid(authBearer, Constants.SECRET_KEY))
				{
					if(!sessionTableRepository.checkIfTokenIsAlreadyInDB(authBearer)) {
						sessionTableRepository.addSession(sessionTable);
					}
				} else {
					sessionTableRepository.removeSession(sessionTable);
				}
			}
			chain.doFilter(request, response);
	}

}

