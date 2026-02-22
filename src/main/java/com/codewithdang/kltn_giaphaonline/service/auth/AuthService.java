package com.codewithdang.kltn_giaphaonline.service.auth;

import com.codewithdang.kltn_giaphaonline.dto.request.AuthReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AuthRes;
import com.codewithdang.kltn_giaphaonline.dto.response.IntrospectRes;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;

public interface AuthService {
    AuthRes authenticate(AuthReq authReq, HttpServletResponse response) throws ParseException;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws ParseException, JOSEException;

    IntrospectRes introspect(HttpServletRequest request) throws ParseException, JOSEException;

    void logout(HttpServletRequest request, HttpServletResponse response) throws ParseException;
}
