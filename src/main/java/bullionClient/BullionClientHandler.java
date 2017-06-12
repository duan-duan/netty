package bullionClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: wangruirui
 * @date: 2017/6/2
 * @description:
 */
public class BullionClientHandler extends ChannelHandlerAdapter{
    private ChannelHandlerContext ctx;

    private int counter;
    private byte[] req;


    public BullionClientHandler(String msg){
        req = (msg+"    query time order "+ System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i=0;i<100;i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("now receive order :"+body+"; the counter is :"+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public void send (String msg){
        ctx.writeAndFlush(msg);
    }

    public static void main(String[] args) {
        BullionClientHandler handler = new BullionClientHandler("2222");
        handler.send("88888");
    }

}
