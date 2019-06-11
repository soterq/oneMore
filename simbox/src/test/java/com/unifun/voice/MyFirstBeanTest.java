package com.unifun.voice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Locale;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.unifun.voice.cdi.MyQualifier;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MyFirstBeanTest {
	
	@Inject
	@MyQualifier
	Locale myLocale;
	@Test
	public void testLocaleInjections() {
		assertNotNull(myLocale);
	}
	@Test
	public void checkValue() {
		assertEquals(Locale.UK, myLocale);
	}

}
