package com.bytehonor.sdk.server.bytehonor.cloud.bean.feign;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bytehonor.sdk.define.bytehonor.constant.ForceStatusHeader;
import com.bytehonor.sdk.define.bytehonor.constant.HeaderKey;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignCoreInterceptor implements RequestInterceptor {
    
//    private static final Logger LOG = LoggerFactory.getLogger(FeignCoreInterceptor.class);

	@Value("${spring.application.name:UNDEFINED}")
	private String applicationName;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header(HeaderKey.X_FROM_TERMINAL, applicationName);
		requestTemplate.header(ForceStatusHeader.FORCE_STATUS_OK_KEY, ForceStatusHeader.FORCE_STATUS_OK_VALUE);

//		long now = System.currentTimeMillis();
//		requestTemplate.header(HeaderKey.X_RPC_TIME, String.valueOf(now));
//		requestTemplate.header(HeaderKey.X_RPC_SIGN, buildSign(now));
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return;
		}

		ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = attributes.getRequest();
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String value = request.getHeader(name);
//				if (LOG.isDebugEnabled()) {
//				    LOG.info("key:{}, value:{}",name, value);
//				}
				requestTemplate.header(name, value);
			}
		}
	}
	
//	private String buildSign(long time) {
//		StringBuilder sb = new StringBuilder();
//		sb.append(applicationName).append("&").append(time);
//		return MD5Utils.md5(sb.toString()).toLowerCase();
//	}
}