package com.tiamaes.netty.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
@Component
public class SomethingChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	private ServerHandler serverHandler;
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new IdleStateHandler(0, 0, 120));
		pipeline.addLast(new StringDecoder());
		pipeline.addLast(serverHandler);
		pipeline.addLast(new StringEncoder());
	}

}
