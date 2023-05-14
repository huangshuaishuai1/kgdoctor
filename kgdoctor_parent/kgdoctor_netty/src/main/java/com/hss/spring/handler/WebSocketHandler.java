package com.hss.spring.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
    // 用于记录和管理所有客户端的channel
    // 客户端组
    public static ChannelGroup channels;

    static {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    /**
     * 接收客户端传来的消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            // 第一次连接成功后，给客户端发送消息
            Channel channel = ctx.channel();
            channel.writeAndFlush(new TextWebSocketFrame("连接客户端成功！"));

            // 获取当前channel绑定的Ip地址
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            String address = ipSocket.getAddress().getHostAddress();
            log.debug("address为{}",address);

//            if ()
        }
        // 二进制消息
        if (msg instanceof BinaryWebSocketFrame) {
            log.debug("收到二进制消息：{}",((BinaryWebSocketFrame) msg).content().readableBytes());
            BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(Unpooled.buffer().writeBytes("hello".getBytes()));
            ctx.writeAndFlush(binaryWebSocketFrame);
        }

        // ping 消息
        if (msg instanceof PongWebSocketFrame) {
            log.debug("客户端ping成功");
        }

        // 关闭消息
        if (msg instanceof CloseWebSocketFrame) {
            log.debug("客户端关闭，通道关闭");
            ctx.channel().close();
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开，channel对呀的长id为：{}",ctx.channel().id().asLongText());
        log.info("客户端断开，channel对呀的短id为：{}",ctx.channel().id().asShortText());
    }

    // 异常处理

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("连接异常：{}",cause.getMessage());
        cause.printStackTrace();
        ctx.channel().close();
        channels.remove(ctx.channel());
    }
}
