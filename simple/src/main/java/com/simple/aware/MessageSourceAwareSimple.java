package com.simple.aware;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author fangkun
 * @date 2020/9/12 20:20
 * @description:
 */
@Component
public class MessageSourceAwareSimple implements MessageSourceAware {
	@Override
	public void setMessageSource(MessageSource messageSource) {
		System.out.println(messageSource.hashCode());
	}
}
