package com.hss.kgdoctor.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.hss.kgdoctor.pojo.ChatMsgVO;
import com.hss.kgdoctor.utils.ChatTypeVerificationUtil;
import com.hss.kgdoctor.utils.UserChannelRel;
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
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;


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
        Channel currentChannel = ctx.channel();
        if (msg instanceof TextWebSocketFrame) {
            String message = ((TextWebSocketFrame) msg).text();
            System.out.println("收到客户端消息：" + message);
            //json消息转换为Javabean对象
            ChatMsgVO chatMsgVO = null;

            try {
                chatMsgVO = JSONUtil.toBean(message, ChatMsgVO.class, true);
            }catch (JSONException e) {
                e.printStackTrace();
                System.out.println("json解析异常，发送的消息应该为json格式");
                return;
            }
            //得到消息的动作类型
            Integer action = chatMsgVO.getAction();
            //客户端第一次连接websocket或者重连时执行
            if (action.equals(1)) {
                //当websocket第一次open的时候，初始化channel,把用的channel和userId关联起来
                Integer fromUserId = chatMsgVO.getFromUserId();
                UserChannelRel.put(fromUserId, currentChannel);
                //测试
                channels.forEach(channel -> log.info(channel.id().asLongText()));
                UserChannelRel.output();
            } else if (action.equals(2)) {
                //聊天类型的消息，把聊天记录保存到redis，同时标记消息的签收状态[是否签收]
                Integer toUserId = chatMsgVO.getToUserId();
                //设置发送消息的时间
                chatMsgVO.setDateTime(new DateTime());
                /* 发送消息给指定用户 */
                //判断消息是否符合定义的类型
                if (ChatTypeVerificationUtil.verifyChatType(chatMsgVO.getChatMessageType())) {
                    //发送消息给指定用户
                    if (toUserId > 0 && UserChannelRel.isContainsKey(toUserId)) {
                        sendMessage(toUserId, JSONUtil.toJsonStr(chatMsgVO));
                    }
                } else {
                    //消息不符合定义的类型的处理
                }
            } else if (action.equals(3)) {
                //心跳类型的消息
                log.info("收到来自channel为[" + currentChannel + "]的心跳包");
            }

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
        System.out.println("与客户端连接成功");
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

    @Override
    public void userEventTriggered ( ChannelHandlerContext ctx, Object evt ) throws Exception {
        //IdleStateEvent是一个用户事件，包含读空闲/写空闲/读写空闲
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("进入读空闲");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("进入写空闲");
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("channel关闭前，用户数量为:" + channels.size());
                //关闭无用的channel，以防资源浪费
                ctx.channel().close();
                log.info("channel关闭后，用户数量为:" + channels.size());
            }

        }
    }

    /**
     * 给指定用户发内容
     * 后续可以掉这个方法推送消息给客户端
     */
    public void sendMessage ( Integer toUserId, String message ) {
        Channel channel = UserChannelRel.get(toUserId);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }



}
