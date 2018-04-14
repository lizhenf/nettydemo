package com.tiamaes.netty.api;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
@Component
public class TcpServer {

	@Autowired
	private ServerBootstrap bootStrap;
	@Autowired
	private InetSocketAddress port;
	private Channel serverChannel;
	@PostConstruct
	public void start() throws InterruptedException{
		serverChannel = bootStrap.bind(port).sync().channel();
		
	}
	@PreDestroy
	public void stop(){
		serverChannel.close();
		serverChannel.parent().close();
	}
}
