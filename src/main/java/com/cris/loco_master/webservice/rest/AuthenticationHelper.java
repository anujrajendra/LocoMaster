package com.cris.loco_master.webservice.rest;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.onwbp.org.apache.commons.codec.binary.Base64;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.Session;

public final class AuthenticationHelper {
	private static final Logger LOGGER = Logger
			.getLogger(com.cris.loco_master.webservice.rest.AuthenticationHelper.class);

	public static final String MSG_PARAM_EBX_SESSION = "ebx.session";

	private static final String HTTP_HEADER_AUTHORIZATION = "Authorization";

	public static Session createSessionFromBasicAuthentication(Repository repository, HttpServletRequest request) {
		String authHeader = request.getHeader(HTTP_HEADER_AUTHORIZATION);
		if (authHeader != null) {

			String[] splitAuthHeaderArray = authHeader.split("\\s");

			for (int i = 0; i < splitAuthHeaderArray.length; i++) {

				String basic = splitAuthHeaderArray[i];

				if (basic.equalsIgnoreCase("Basic")) {
					try {
						String credentials = new String(Base64.decodeBase64(splitAuthHeaderArray[i + 1]), "UTF-8");
						int p = credentials.indexOf(":");
						if (p != -1) {
							String login = credentials.substring(0, p).trim();
							String password = credentials.substring(p + 1).trim();

							return repository.createSessionFromLoginPassword(login, password);
						}
						LOGGER.error("Invalid authentication token");

					} catch (UnsupportedEncodingException e) {
						LOGGER.warn("Couldn't retrieve authentication", e);
					}
				}
			}
		}
		return null;
	}

	public static String getLoginUserFromHeader(Repository repository, HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {

			String[] splitAuthHeaderArray = authHeader.split("\\s");

			for (int i = 0; i < splitAuthHeaderArray.length; i++) {

				String basic = splitAuthHeaderArray[i];

				if (basic.equalsIgnoreCase("Basic")) {
					try {
						String credentials = new String(Base64.decodeBase64(splitAuthHeaderArray[i + 1]), "UTF-8");
						int p = credentials.indexOf(":");
						if (p != -1) {
							String login = credentials.substring(0, p).trim();
							return login;
						}
						LOGGER.error("Invalid authentication token");

					} catch (UnsupportedEncodingException e) {
						LOGGER.warn("Couldn't retrieve authentication", e);
					}
				}
			}
		}
		return null;
	}
}
