package io.vicp.goradical.intellirec.struts2.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import io.vicp.goradical.intellirec.service.RightService;
import io.vicp.goradical.intellirec.util.ValidateUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * 捕获URL拦截器
 */
public class CatchUrlInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		ActionProxy proxy = actionInvocation.getProxy();
		//命名空间
		String ns = proxy.getNamespace();
		//actionName
		String actionName = proxy.getActionName();
		if (!ValidateUtil.isValid(ns) || "/".equals(ns)) {
			ns = "";
		}
		String url = ns + "/" + actionName;
		//取得在applicationspring容器
//		ApplicationContext ac = (ApplicationContext) actionInvocation.getInvocationContext().getApplication().get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		ServletContext sc = ServletActionContext.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		RightService rightService = (RightService) ac.getBean("rightService");
		rightService.appendRightByURL(url);
		return actionInvocation.invoke();
	}
}
