package time.server;

import io.netty.channel.*;
import time.Message;
import time.OperateType;

/**
 * @author fandong
 * @create 2018/11/7
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message m = (Message) msg;
        String msg1 = m.getMsg();
        String heartBeat = "heartbeat";
        if (heartBeat.equals(msg1)){
            //回应心跳
            ctx.writeAndFlush(new Message(heartBeat));
            return;
        }
        System.out.println("server receive msg" + m);
        String type = m.getType();
        switch (type){
            case OperateType.LOGIN:
                handleLogin(ctx, m);
                break;
            case OperateType.QUIT:
                handleQuit(ctx, m);
                break;
            case OperateType.SEND:
                handleSend(ctx, m);
                break;
            default:
                break;
        }
    }

    private void handleLogin(ChannelHandlerContext ctx, Message message){
        String auth = message.getAuth();
        String username = message.getFromUsername();
        if (username == null){
            return;
        }
        if (checkauth(auth)){
            System.out.println("登陆：" + message.getFromUsername());
            UserChannel.save(username, ctx.channel());
        }
    }

    private void handleQuit(ChannelHandlerContext ctx, Message message){
        if (checkauth(message.getAuth())){
            System.out.println("注销：" + message.getFromUsername());
            UserChannel.delete(message.getFromUsername());
        }
    }

    private void handleSend(ChannelHandlerContext ctx, Message message){
        String username = message.getFromUsername();
        Channel channelByUsername = UserChannel.getChannelByUsername(username);
        if (channelByUsername == null){
            ctx.writeAndFlush(new Message("-1", "发送端用户尚未登陆"));
            return;
        }

        String toUsername = message.getToUsername();
        Channel channelByToUsername = UserChannel.getChannelByUsername(toUsername);
        if (channelByToUsername == null){
            //接收端用户尚未登陆
            //保存消息 用户登陆时查看是否有未接收的消息
            return;
        }
        Message m = new Message();
        m.setFromUsername(username);
        m.setMsg(message.getMsg());
        channelByToUsername.writeAndFlush(m);
    }

    private boolean checkauth(String auth){
        if (auth == null){
            return false;
        }
        String mima = "mima";
        return mima.equals(auth);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
