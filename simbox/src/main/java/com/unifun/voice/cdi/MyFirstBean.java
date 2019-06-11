package com.unifun.voice.cdi;

import java.util.Locale;

import javax.enterprise.inject.Produces;

public class MyFirstBean {
	@Produces
	@MyQualifier
	public Locale obtainLocale() {
		return Locale.UK;
	}
	
	@Produces
	public Locale obtainUsLocale() {
		return Locale.US;
	}
}
