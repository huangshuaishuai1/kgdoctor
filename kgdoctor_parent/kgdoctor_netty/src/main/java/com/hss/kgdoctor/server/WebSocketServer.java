package com.hss.kgdoctor.server;


import com.hss.kgdoctor.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WebSocketServer {

    @Value("${netty.port}")
    private int port;

    public void run() throws InterruptedException {
        NioEventLoopGroup boos = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(boos,worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // web基于http协议的解码器
                            ch.pipeline().addLast(new HttpServerCodec());
                            // 对大数据流的支持
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            // 对http message进行聚合， 聚合成FullHttpRequest或者FullHttpResponse
                            ch.pipeline().addLast(new HttpObjectAggregator(1024*64));
                            // websocket 服务器处理对协议，用于指定给服务端连接访问的路径
                            // 该handler会帮你处理一些繁重的复杂的事情
                            // 会帮你处理握手动作：handshaking(close,ping,pong) ping + pong = 心跳
                            // 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                            // 添加自定义的channel处理器
                            ch.pipeline().addLast(new WebSocketHandler());
                        }
                    });
            log.debug("服务器启动中，websocket的端口为：{}",port);
            ChannelFuture future = serverBootstrap.bind(port);
            future.sync();
            future.channel().closeFuture().sync();
        }finally {
            // 关闭主从线程池
            worker.shutdownGracefully();
            boos.shutdownGracefully();
        }
    }
}
