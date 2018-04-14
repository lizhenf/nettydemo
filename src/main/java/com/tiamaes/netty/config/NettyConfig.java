package com.tiamaes.netty.config;


import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tiamaes.netty.handler.SomethingChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Configuration
public class NettyConfig {
	@ConfigurationProperties(prefix="spring.netty")
	public class NettyProperties{
		private int port;
		private int bossThreadCount;
		private int workerThreadCount;
		private boolean soKeepalive;
		private int soBackLog;
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public int getBossThreadCount() {
			return bossThreadCount;
		}
		public void setBossThreadCount(int bossThreadCount) {
			this.bossThreadCount = bossThreadCount;
		}
		public int getWorkerThreadCount() {
			return workerThreadCount;
		}
		public void setWorkerThreadCount(int workerThreadCount) {
			this.workerThreadCount = workerThreadCount;
		}
		public boolean isSoKeepalive() {
			return soKeepalive;
		}
		public void setSoKeepalive(boolean soKeepalive) {
			this.soKeepalive = soKeepalive;
		}
		public int getSoBackLog() {
			return soBackLog;
		}
		public void setSoBackLog(int soBackLog) {
			this.soBackLog = soBackLog;
		}
		
	}
	
	@Bean(name="NettyProperties")
	public NettyProperties nettyProperties(){
		return new NettyProperties();
	}

	@Autowired
	private SomethingChannelInitializer somethingChannelInitializer;
	
	@Bean(name="bossGroup", destroyMethod="shutdownGracefully")
	public NioEventLoopGroup bossGroup(NettyProperties nettyProperties){
		return new NioEventLoopGroup(nettyProperties.getBossThreadCount());
		
	}
	
	@Bean(name="workerGroup", destroyMethod="shutdownGracefully")
	public NioEventLoopGroup workerGroup(NettyProperties nettyProperties){
		return new NioEventLoopGroup(nettyProperties.getWorkerThreadCount());
		
	}
	@Bean
	@SuppressWarnings({"unchecked","rawtypes"})
	public ServerBootstrap bootStrap(NettyProperties nettyProperties){
		ServerBootstrap bootStrap = new ServerBootstrap();
		bootStrap.group(bossGroup(nettyProperties), workerGroup(nettyProperties))
		.channel(NioServerSocketChannel.class)
		.childHandler(somethingChannelInitializer);
		
		Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions(nettyProperties);
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		for(ChannelOption option : keySet){
			bootStrap.option(option, tcpChannelOptions.get(option));
		}
		return bootStrap;
	}
	@Bean
	public InetSocketAddress tcpPort(NettyProperties nettyProperties){
		return new InetSocketAddress(nettyProperties.getPort());
	}
	@Bean
	public Map<ChannelOption<?>, Object> tcpChannelOptions(NettyProperties nettyProperties){
		Map<ChannelOption<?>, Object> option = new HashMap<ChannelOption<?>, Object>();
		option.put(ChannelOption.SO_KEEPALIVE, nettyProperties.isSoKeepalive());
		option.put(ChannelOption.SO_BACKLOG, nettyProperties.getSoBackLog());
		return option;
	}
}
