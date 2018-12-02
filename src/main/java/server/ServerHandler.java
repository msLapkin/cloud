package server;

import common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private String path = "Files/Server/";
    private String cLogin;
    private String cPath;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof AuthMessage){
                cLogin = ((AuthMessage) msg).getLogin();
                cPath = path+cLogin+"/";
                System.out.println("login"+cLogin);
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get(cPath + fr.getFilename()))) {
                    System.out.println(cPath);
                    FileMessage fm = new FileMessage(Paths.get(cPath + fr.getFilename()));
                    ctx.writeAndFlush(fm);
                }
            }
            if (msg instanceof FileMessage) {
                System.out.println("команда 1");
                FileMessage fm = (FileMessage) msg;
                Files.write(Paths.get(cPath + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                ctx.writeAndFlush(new FileListMessage(FileListMessage.getFileList(cPath)));
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
