package time.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import time.Message;
import time.OperateType;

import java.util.concurrent.TimeUnit;

/**
 * @author fandong
 * @create 2018/11/7
 */
public class Client {

    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;

    public static void main(String[] args) throws InterruptedException {
        new Client().connect();
    }

    public void connect() throws InterruptedException {

        try {
            if (bootstrap == null){
                bootstrap = new Bootstrap();
                bootstrap.group(workerGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("inout0", new IdleStateHandler(60, 20, 120, TimeUnit.SECONDS));
                        pipeline.addLast("in0", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast("in1", new Decoder());
                        pipeline.addLast("out2", new LengthFieldPrepender(4));
                        pipeline.addLast("out0", new Encoder());
                        pipeline.addLast("in2", new ClientHandler(Client.this));
                    }
                });
            }

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8000).sync();
            //断线重连
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()){
                        System.out.println("Connect success");
                    }else {
                        System.out.println("Connect fail Reconnect...");
                        EventLoop eventLoop = channelFuture.channel().eventLoop();
                        eventLoop.schedule(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    connect();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 5, TimeUnit.SECONDS);
                    }
                }
            });

            Channel channel = channelFuture.channel();
            Message message = new Message();
            message.setFromUsername("fan");
            message.setToUsername("dong");
            message.setType(OperateType.SEND);
            for (int i = 0; i < 5; i++) {
                message.setMsg("在干嘛 " + i);
                channel.writeAndFlush(message).sync();
                Thread.sleep(2000);
            }
            message.setType(OperateType.QUIT);
            message.setAuth("mima");
            channel.writeAndFlush(message).sync();

            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
