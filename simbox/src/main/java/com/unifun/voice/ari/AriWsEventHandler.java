package com.unifun.voice.ari;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.loway.oss.ari4java.generated.ActionChannels;
import ch.loway.oss.ari4java.generated.Channel;
import ch.loway.oss.ari4java.generated.ChannelDtmfReceived;
import ch.loway.oss.ari4java.generated.ChannelHangupRequest;
import ch.loway.oss.ari4java.generated.ChannelStateChange;
import ch.loway.oss.ari4java.generated.ChannelVarset;
import ch.loway.oss.ari4java.generated.Message;
import ch.loway.oss.ari4java.generated.Playback;
import ch.loway.oss.ari4java.generated.PlaybackFinished;
import ch.loway.oss.ari4java.generated.PlaybackStarted;
import ch.loway.oss.ari4java.generated.StasisEnd;
import ch.loway.oss.ari4java.generated.StasisStart;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;

public class AriWsEventHandler implements AriCallback<Message>{
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	List<AriWsEventHandler> listeners = new ArrayList<AriWsEventHandler>();
	private AriManager aim;
	
	public AriWsEventHandler(AriManager aim) {
		this.aim = aim ;
	}

	/**
	 * Minimum set of events that we should handle for start 
	 * StasisStart_impl_ari_3_0_0
	 * ChannelStateChange_impl_ari_3_0_0 PlaybackStarted_impl_ari_3_0_0
	 * PlaybackFinished_impl_ari_3_0_0 ChannelDtmfReceived_impl_ari_3_0_0
	 * ChannelDtmfReceived_impl_ari_3_0_0 ChannelDtmfReceived_impl_ari_3_0_0
	 * ChannelDtmfReceived_impl_ari_3_0_0 ChannelVarset_impl_ari_3_0_0
	 * ChannelVarset_impl_ari_3_0_0 ChannelVarset_impl_ari_3_0_0
	 * ChannelVarset_impl_ari_3_0_0 ChannelHangupRequest_impl_ari_3_0_0
	 * StasisEnd_impl_ari_3_0_0
	 */
	@Override
	public void onSuccess(Message result) {
		logger.trace("received Message {}", result.getClass());
		if (result instanceof StasisStart) {
			onStasisStart((StasisStart) result);
		} else if (result instanceof StasisEnd) {
			onStasisEnd((StasisEnd) result);
		} else if (result instanceof ChannelStateChange) {
			onChannelStateChange((ChannelStateChange)result);
		} else if (result instanceof PlaybackStarted) {
			onPlaybackStarted((PlaybackStarted)result);
		} else if (result instanceof PlaybackFinished) {
			onPlaybackFinished((PlaybackFinished)result);
		} else if (result instanceof ChannelDtmfReceived) {
			onDtmfReceived((ChannelDtmfReceived)result);
		} else if (result instanceof ChannelVarset) {
			//FIXME
			logger.warn("Event {} not handlet yet",result.getClass().getSimpleName());
		} else if (result instanceof ChannelHangupRequest) {
			onChannelHangupRequest((ChannelHangupRequest)result);
		}else {
			logger.warn("Event {} not handlet yet",result.getClass().getSimpleName());
		}
	}
	
	@Override
	public void onFailure(RestException e) {
		logger.error("Some ");
		
	}


	/**
	 * { "type": "StasisEnd", "timestamp": "2019-03-31T11:55:23.607+0300",
	 * "channel": { "id": "1554022516.20", "name": "SIP/6002-0000000d", "state":
	 * "Ring", "caller": { "name": "Example Joe", "number": "6002" }, "connected": {
	 * "name": "", "number": "" }, "accountcode": "", "dialplan": { "context":
	 * "unifun", "exten": "123", "priority": 2 }, "creationtime":
	 * "2019-03-31T11:55:16.713+0300", "language": "en" }, "asterisk_id":
	 * "02:42:ac:11:00:02", "application": "Unifun-ARI" }
	 */
	private void onStatsisEnd(StasisEnd event) {
		Channel channel = event.getChannel();
	}

	/**
	 * { "type": "StasisStart", "timestamp": "2019-03-31T11:57:40.562+0300", "args":
	 * [], "channel": { "id": "1554022660.21", "name": "SIP/6002-0000000e", "state":
	 * "Ring", "caller": { "name": "Example Joe", "number": "6002" }, "connected": {
	 * "name": "", "number": "" }, "accountcode": "", "dialplan": { "context":
	 * "unifun", "exten": "123", "priority": 2 }, "creationtime":
	 * "2019-03-31T11:57:40.561+0300", "language": "en" }, "asterisk_id":
	 * "02:42:ac:11:00:02", "application": "Unifun-ARI" }
	 * 
	 * @param event
	 */
	Map<String, Temp> achannel = new HashMap<>();
	private void onStasisStart(StasisStart event) {
		
		try {
		    Channel channel = event.getChannel();
		    String channelId = channel.getId();
		    String applicationName = event.getApplication();
		    logger.info("received message with channelId {} on application {}, channel state {}"
		    		+ "called number {}, calling number {}", 
		    		channelId,applicationName,channel.getState(), channel.getCaller().getNumber(), 
		    		channel.getDialplan().getExten());
		    ActionChannels ac = aim.getAri().getActionImpl(ActionChannels.class);
//		    if ("Ring".equals(channel.getState())) {
		    	ac.answer(channelId);
		    	Playback pb = ac.play(channelId, "sound:hello-world", "", 0, 0, "");
		    	achannel.put(pb.getId(), new Temp(channel, null));
		    	
//		    }
		    //ac.hangup(channelId, "normal"/*Allowed values: normal, busy, congestion, no_answer*/);
		    aim.getAri().closeAction(ac);
//                if(applicationName.equals(ariService.getTerminateStasis())) {
//                    ActionChannels actionChannels = ariService.getAri().getActionImpl(ActionChannels.class);
//                    actionChannels.answer(channelId);
//                    //actionChannels.startMoh(channelId,"");
//                    ariService.getAri().closeAction(actionChannels);
//                }

		} catch (Exception e) {
		    logger.error("Unknown Error",e);
		}
	}
	
	/**
	 * { "type": "ChannelDtmfReceived", "timestamp": "2019-03-31T12:31:09.352+0300",
	 * "digit": "2", "duration_ms": 160, "channel": { "id": "1554024563.24", "name":
	 * "SIP/6002-00000010", "state": "Up", "caller": { "name": "Example Joe",
	 * "number": "6002" }, "connected": { "name": "", "number": "" }, "accountcode":
	 * "", "dialplan": { "context": "unifun", "exten": "123", "priority": 2 },
	 * "creationtime": "2019-03-31T12:29:23.593+0300", "language": "en" },
	 * "asterisk_id": "02:42:ac:11:00:02", "application": "Unifun-ARI" }
	 * 
	 * @param event
	 */
	public void onDtmfReceived(ChannelDtmfReceived event) {
		Channel channel = event.getChannel();
		logger.info("Channel {}, Received digit {}",channel.getId(),event.getDigit());
	}
	
	/**
	 * { "type": "ChannelStateChange", "timestamp": "2019-03-31T12:30:01.377+0300",
	 * "channel": { "id": "1554024563.24", "name": "SIP/6002-00000010", "state":
	 * "Up", "caller": { "name": "Example Joe", "number": "6002" }, "connected": {
	 * "name": "", "number": "" }, "accountcode": "", "dialplan": { "context":
	 * "unifun", "exten": "123", "priority": 2 }, "creationtime":
	 * "2019-03-31T12:29:23.593+0300", "language": "en" }, "asterisk_id":
	 * "02:42:ac:11:00:02", "application": "Unifun-ARI" }
	 * 
	 * @param event
	 */
	public void onChannelStateChange(ChannelStateChange event) {
		
	}
	
	/**
	 * { "type": "PlaybackStarted", "playback": { "id":
	 * "04c496ad-6d1d-421c-b10f-bc9cd93c09dd", "media_uri": "sound:hello-world",
	 * "target_uri": "channel:1554024563.24", "language": "en", "state": "playing"
	 * }, "asterisk_id": "02:42:ac:11:00:02", "application": "Unifun-ARI" }
	 */
	private void onPlaybackStarted(PlaybackStarted event) {
		logger.info("Start palayback");

	}
	
	/**
	 * { "type": "PlaybackFinished", "playback": { "id":
	 * "04c496ad-6d1d-421c-b10f-bc9cd93c09dd", "media_uri": "sound:hello-world",
	 * "target_uri": "channel:1554024563.24", "language": "en", "state": "done" },
	 * "asterisk_id": "02:42:ac:11:00:02", "application": "Unifun-ARI" }
	 */
	private void onPlaybackFinished(PlaybackFinished event) {
		logger.info("Stop palayback");
		Temp tmp = achannel.get(event.getPlayback().getId());
		try {
			 ActionChannels ac = aim.getAri().getActionImpl(ActionChannels.class);
			 
			ac.hangup(tmp.getChannel().getId(), "normal");
			 aim.getAri().closeAction(ac);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * { "type": "StasisEnd", "timestamp": "2019-03-31T12:34:28.149+0300",
	 * "channel": { "id": "1554024563.24", "name": "SIP/6002-00000010", "state":
	 * "Up", "caller": { "name": "Example Joe", "number": "6002" }, "connected": {
	 * "name": "", "number": "" }, "accountcode": "", "dialplan": { "context":
	 * "unifun", "exten": "123", "priority": 2 }, "creationtime":
	 * "2019-03-31T12:29:23.593+0300", "language": "en" }, "asterisk_id":
	 * "02:42:ac:11:00:02", "application": "Unifun-ARI" }
	 */
	private void onStasisEnd(StasisEnd event) {
		
	}
	
	/**
	 * { "type": "ChannelHangupRequest", "timestamp":
	 * "2019-03-31T12:34:28.146+0300", "channel": { "id": "1554024563.24", "name":
	 * "SIP/6002-00000010", "state": "Up", "caller": { "name": "Example Joe",
	 * "number": "6002" }, "connected": { "name": "", "number": "" }, "accountcode":
	 * "", "dialplan": { "context": "unifun", "exten": "123", "priority": 2 },
	 * "creationtime": "2019-03-31T12:29:23.593+0300", "language": "en" },
	 * "asterisk_id": "02:42:ac:11:00:02", "application": "Unifun-ARI" }
	 */
	private void onChannelHangupRequest(ChannelHangupRequest event) {
		
	}
}