package net.chinanets.spring;

import org.hibernate.FlushMode;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;

public class MyOpenSessionFilter extends OpenSessionInViewFilter {

	public MyOpenSessionFilter() {
		super.setFlushMode(FlushMode.AUTO);
	}
	

}
