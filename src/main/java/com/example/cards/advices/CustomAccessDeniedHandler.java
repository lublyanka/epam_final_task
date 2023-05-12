package com.example.cards.advices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

/** The Custom access denied handler. */
@CommonsLog
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  /**
   * Handles an access denied failure.
   *
   * @param request that resulted in an <code>AccessDeniedException</code>
   * @param response so that the user agent can be advised of the failure
   * @param exc that caused the invocation
   * @throws IOException the io exception
   */
  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc)
      throws IOException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      log.warn(
          "User: "
              + auth.getName()
              + " attempted to access the protected URL: "
              + request.getRequestURI());
    }

    response.sendRedirect(request.getContextPath() + "/error/403");
  }
}
