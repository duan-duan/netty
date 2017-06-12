package bullionClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author: wangruirui
 * @date: 2017/6/2
 * @description:
 */
public class BullionClient {
    public void connect(int port,String host,final String msg) throws Exception{
        //配置服务端的NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new BullionClientHandler(msg));

                        }
                    });
            //发起异步连接操作
            ChannelFuture f = b.connect(host,port).sync();
            if (f.isSuccess()) {
                SocketChannel socketChannel = (SocketChannel) f.channel();
                System.out.println("----------------connect server success----------------");
            }

            //等待客户端链路端口关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出，释放线程池资源
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 9999;
        if(args !=  null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        new BullionClient().connect(port,"localhost","bullion");
        BullionClientHandler handler = new BullionClientHandler("2222");
        handler.send("88888");


    }
}
