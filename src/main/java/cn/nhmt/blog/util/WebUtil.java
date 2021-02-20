package cn.nhmt.blog.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: ~~~
 * @Date: 2020-07-04 14:32
 * @Author: PinJyu
 * @Version: 1.0
 **/
public abstract class WebUtil {

    public static final String X_REQUESTED_WITH = "X-Requested-With";

    public static final String XMLHttpRequest = "XMLHttpRequest";

    public static final StopExceptionHandlerExecException unresolverException = new StopExceptionHandlerExecException();

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return XMLHttpRequest.equals(request.getHeader(X_REQUESTED_WITH));
    }

    public static boolean isAjaxWebRequest(WebRequest webRequest) {
        return XMLHttpRequest.equals(webRequest.getHeader(X_REQUESTED_WITH));
    }

    public static void print(HttpServletResponse response, Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        response.getWriter().print(s);
        response.flushBuffer();
    }

    /** @ExceptionHandler method throw this.
     * {@link org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver}
     * will return null, cause {@link HandlerExceptionResolverComposite} do next ExceptionResolver
     */
    public static class StopExceptionHandlerExecException extends RuntimeException {

        private static final String DEFAULT_MESSAGE = "Don't Handler AccessDeniedException or AuthenticationException "
                + "in @ExceptionHandler, throw it to AccessDeniedHandlerFilter.";

        public StopExceptionHandlerExecException(String message) {
            super(message == null ? DEFAULT_MESSAGE : message);
        }

        public StopExceptionHandlerExecException() {
            super(DEFAULT_MESSAGE);
        }

    }

}
