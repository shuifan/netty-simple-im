package time.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author fandong
 * @create 2018/11/6
 */
public class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel socketChannel) throws Exception {
                     ChannelPipeline pipeline = socketChannel.pipeline();
                     pipeline.addLast("in2", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                     pipeline.addLast("in1", new Decoder());
                     pipeline.addLast("out0", new LengthFieldPrepender(4));
                     pipeline.addLast("out1", new Encoder());
                     pipeline.addLast("in3", new ServerHandler());
                 }
             })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //绑定端口 开始接收连接
            //绑定多个端口
            ChannelFuture f = b.bind(port);
            ChannelFuture f1 = b.bind(8001);

            f.channel().closeFuture().sync();
            f1.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server(8000).run();
    }
}
