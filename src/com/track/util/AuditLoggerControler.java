package com.track.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.track.audit.core.IAuditTopicPublisher;

/**
 * 
 * @author Shayanthan K
 * @version 1.0.0
 * @created Sep 8, 2010 - 3:50:39 PM
 */
public class AuditLoggerControler {
	public static IAuditTopicPublisher auditTopicPublisher;
	private static ApplicationContext applicationContext;
	static {
		if (applicationContext == null) {
			applicationContext = new ClassPathXmlApplicationContext(
					"WEB-INF/track-auditing-context.xml");
		}
		auditTopicPublisher = (IAuditTopicPublisher) applicationContext
				.getBean("auditTopicPublisher");
	}
}
