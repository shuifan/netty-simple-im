package time.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import time.Message;
import time.OperateType;

/**
 * @author fandong
 * @create 2018/11/7
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Client timeClient;

    public ClientHandler(Client timeClient) {
        this.timeClient = timeClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message message = new Message();
        message.setType(OperateType.LOGIN);
        message.setFromUsername("fan");
        message.setAuth("mima");
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message m = (Message) msg;
        System.out.println("接收：" + m);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("事件触发");
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent){
            System.out.println("空闲事件");
            IdleStateEvent e = (IdleStateEvent) evt;
            IdleState state = e.state();
            if (IdleState.READER_IDLE.equals(state)){
                //长期未收到服务器数据  可以选择重连

            } else if (IdleState.WRITER_IDLE.equals(state)){
                //长期未向服务器发送数据  发送心跳包
                System.out.println("heartbeat");
                ctx.writeAndFlush(new Message("heartbeat"));
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("Connect fail Reconnect...");
        timeClient.connect();
    }
}
