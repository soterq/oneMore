package com.unifun.voice;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unifun.voice.ari.AriManager;

import ch.loway.oss.ari4java.tools.ARIException;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

public class AppLifecycleBean {
	@Inject
	AriManager am;
	private static final Logger LOGGER = LoggerFactory.getLogger("ListenerBean");

	void onStart(@Observes StartupEvent ev) { 
		try {
			am.initWebSocketConnection();
		} catch (ARIException e) {
			e.printStackTrace();
		}
		LOGGER.info("The application is starting...");
	}

	void onStop(@Observes ShutdownEvent ev) { 
		LOGGER.info("The application is stopping...");
	}

}