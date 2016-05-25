package apm.modules.sys.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import apm.common.config.Global;
import apm.common.utils.SpringContextHolder;
import apm.common.utils.StringUtils;
import apm.modules.sys.entity.Log;
import apm.modules.sys.entity.User;
import apm.modules.sys.service.LogService;
import apm.modules.sys.support.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 访问日志 拦截器
 * @author Zaric
 * @version 2013-6-6
 */
public class LogInterceptor implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	private static LogService logService = SpringContextHolder.getBean(LogService.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String uri = request.getRequestURI();
		String uriPrefix = request.getContextPath() + Global.getAdminPath();
		
		// 拦截所有来自管理端的POST请求
		if ("POST".equals(request.getMethod()) && StringUtils.startsWith(uri, uriPrefix) && (StringUtils.endsWith(uri, "/save")
				|| StringUtils.endsWith(uri, "/delete") || StringUtils.endsWith(uri, "/import")
				|| StringUtils.endsWith(uri, "/updateSort")) || ex!=null){
					
			User user = Users.currentUser();
			if (user != null && StringUtils.isNotEmpty(user.getId())){
				Log log = new Log();
				
				log.setUri(uri.substring(request.getContextPath().length()));
		        // method
		        log.setMethod(request.getMethod());
		        // 访问者ip
		        log.setAccessorIp(getRemoteIp(request));

				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()){ 
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase((String)param, "password")
							? "" : request.getParameter((String)param), 100));
				}
		        log.setParams(params.toString());
		        
				log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
				
				log.setUser(user);
				log.setUserName(user.getName());
				log.setLoginName(user.getLoginName());
				log.setHandlers(handler.toString());
				log.setException(ex != null ? ex.toString() : "");
				
				logService.save(log);
				logger.info("save log {type: {}, loginName: {}, uri: {}}, ", log.getType(), user.getLoginName(), log.getUri());
			}
		}
	}
	
    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String rip = request.getHeader("X-Real-IP");
        if (rip == null) {
            rip = request.getHeader("X-Forwarded-For");
        }
        return rip != null ? rip : ip;
    }

}