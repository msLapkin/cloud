package server;

import common.AbstractMessage;
import common.AuthMessage;
import common.CommandMessage;
import common.FileListMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    private boolean authOK;
    private String path = "Files/Server/";
    private String cPath;
    private String cLogin;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) return;
        if (!authOK) {
            if (msg instanceof AuthMessage) {
                if (SQLConnect.checkAutorisation(((AuthMessage) msg).getLogin(), ((AuthMessage) msg).getPass())) {
                    cLogin = ((AuthMessage) msg).getLogin();
                    authOK = true;
                    //ctx.writeAndFlush(new CommandMessage(CommandMessage.AUTH_OK));
                    cPath = path + cLogin;
                  //  System.out.println("aaaaaaa");
                    AbstractMessage.checkDir(cPath);
                    ctx.writeAndFlush(new FileListMessage(FileListMessage.getFileList(cPath)));
                    ctx.fireChannelRead(msg);
                } else {
                    ctx.writeAndFlush(new CommandMessage(CommandMessage.AUTH_FAIL));
                    authOK = false;
                }
            } else {
                return;
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public boolean status(){
        return authOK;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
