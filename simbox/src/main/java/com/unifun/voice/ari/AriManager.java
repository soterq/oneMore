package com.unifun.voice.ari;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriFactory;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.ActionEvents;
import ch.loway.oss.ari4java.tools.ARIException;

@ApplicationScoped
public class AriManager {
	private static final Logger logger = Logger.getLogger(AriManager.class);
	//FIXME: put this options in propertie file.
	private String ARI_URL="http://localhost:8088/";
	private String USER="unifun";
	private String PASSWORD="unifun";
	private String APPLICATION="Unifun-ARI";
	private AriWsEventHandler ariHandler;
	private ARI ari;
	
	public ARI getAri() {
		return ari;
	}

	public AriManager() {
		logger.info("Initiatiing ARI");
		ariHandler = new AriWsEventHandler(this);
		try {
			this.ari = AriFactory.nettyHttp(ARI_URL, USER, PASSWORD, AriVersion.ARI_3_0_0, APPLICATION);
		} catch (Exception e) {
			logger.error("Uri syntax exception...",e);
		} 
	}
	
	public void initWebSocketConnection() throws ARIException {
		ActionEvents ae = ari.getActionImpl(ActionEvents.class);
		ae.eventWebsocket(APPLICATION, true, ariHandler);
	}
}
