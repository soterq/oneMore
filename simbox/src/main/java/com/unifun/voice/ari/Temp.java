package com.unifun.voice.ari;

import ch.loway.oss.ari4java.generated.ActionChannels;
import ch.loway.oss.ari4java.generated.Channel;

public class Temp {
	Channel channel;
	ActionChannels ac;
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public ActionChannels getAc() {
		return ac;
	}
	public void setAc(ActionChannels ac) {
		this.ac = ac;
	}
	public Temp(Channel channel, ActionChannels ac) {
		super();
		this.channel = channel;
		this.ac = ac;
	}
	
	
}
