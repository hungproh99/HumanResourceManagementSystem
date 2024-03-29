package com.csproject.hrm.jwt.config;

import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.csproject.hrm.common.constant.Constants.AUTHORIZATION;
import static com.csproject.hrm.common.constant.Constants.BEARER;

public class AuthTokenFilter extends OncePerRequestFilter {

  private static final Logger Logger = LoggerFactory.getLogger(AuthTokenFilter.class);
  @Autowired private JwtUtils jwtUtils;
  @Autowired private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String id = jwtUtils.getIdFromJwtToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(id);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }
    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {

    String headerAuth = request.getHeader(AUTHORIZATION);

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      return headerAuth.substring(7, headerAuth.length());
    }
    return null;
  }
}