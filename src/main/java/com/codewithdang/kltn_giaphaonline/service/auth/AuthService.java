package com.codewithdang.kltn_giaphaonline.service.auth;

import com.codewithdang.kltn_giaphaonline.dto.request.LoginReq;
import com.codewithdang.kltn_giaphaonline.dto.request.RegisterByInvitationReq;
import com.codewithdang.kltn_giaphaonline.dto.request.RegisterReq;
import com.codewithdang.kltn_giaphaonline.dto.response.LoginRes;
import com.codewithdang.kltn_giaphaonline.dto.response.IntrospectRes;
import com.codewithdang.kltn_giaphaonline.dto.response.RegisterRes;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;

public interface AuthService {
    LoginRes authenticate(LoginReq loginReq, HttpServletResponse response) throws ParseException;

    RegisterRes register(RegisterReq registerReq, String requestedIp, String userAgent);

    void registerByInvitation(RegisterByInvitationReq req, String requestedIp, String userAgent);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws ParseException, JOSEException;

    IntrospectRes introspect(String token) throws ParseException, JOSEException;

    void logout(HttpServletRequest request, HttpServletResponse response) throws ParseException;

    String getTokenFromCookie(HttpServletRequest request, String cookieName);

}
