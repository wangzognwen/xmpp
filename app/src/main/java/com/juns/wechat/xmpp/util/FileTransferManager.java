package com.juns.wechat.xmpp.util;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.util.FileUtil;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.xmpp.XmppConnUtil;
import com.juns.wechat.xmpp.XmppExceptionHandler;
import com.juns.wechat.xmpp.XmppManager;
import com.juns.wechat.xmpp.XmppManagerImpl;
import com.juns.wechat.xmpp.iq.FileTransferIQ;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaIdFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLConnection;

/**
 * Created by 王者 on 2016/8/19.
 */
public class FileTransferManager {
    private static final FileTransferManager mInstance = new FileTransferManager();
    private AbstractXMPPConnection xmppConnection;
    private static final int PORT = 7700;

    public static FileTransferManager getInstance(){
        return mInstance;
    }

    private FileTransferManager(){
        xmppConnection = XmppConnUtil.getXmppConnection();
    }

    public void sendFile(File file, String otherName, FileTransferListener listener) {
        FileTransferIQ fileTransferIQ = new FileTransferIQ();
        fileTransferIQ.setStanzaId(StanzaIdUtil.newStanzaId());
        fileTransferIQ.setFrom(XmppConnUtil.getXmppConnection().getUser());
        fileTransferIQ.setTo(ConfigUtil.getBaseJid(otherName));
        fileTransferIQ.setMimeType(URLConnection.guessContentTypeFromName(file.getName()));
        fileTransferIQ.setFile(new FileTransferIQ.File(file.getName(), file.length()));

        try {
            PacketCollector packetCollector = xmppConnection.createPacketCollectorAndSend(
                    new StanzaIdFilter(fileTransferIQ.getStanzaId()), fileTransferIQ);
            FileTransferIQ fileTransferIQResponse = packetCollector.nextResult(5000);

            if(fileTransferIQResponse == null) {
                listener.transferFinished(false);
                return;
            }

            String digest = fileTransferIQResponse.getDigest();
            Socket socket = new Socket(ConfigUtil.getXmppServer(), PORT);
            OutputStream out = socket.getOutputStream();
            out.write((byte) 5);  //version
            out.write((byte) digest.length()); //digest length
            out.write(digest.getBytes()); //digest
            InputStream in = FileUtil.readFile(file);
            int count = in.available();
            int wrote = 0;
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1){
                out.write(buffer, 0, len);
                wrote += len;
                notifyProgressUpdated(listener, wrote, count);
            }
            in.close();
            out.flush();
            socket.shutdownOutput();

            InputStream socketIn = new DataInputStream(socket.getInputStream());
            byte[] data = new byte[7];
            socketIn.read(data);
            socket.close();
            String result = new String(data);
            if("success".equals(result)){
                listener.transferFinished(true);
                return;
            }

        } catch (IOException e) {
            XmppExceptionHandler.handleIOException(e);
        } catch (SmackException e) {
            XmppExceptionHandler.handleSmackException(e);
        }
        listener.transferFinished(false);
    }

    private void notifyProgressUpdated(FileTransferListener listener, int wrote, int amount){
        int progress = (int) ((((float) wrote) / amount) * 100);
        listener.progressUpdated(progress);
    }

    public interface FileTransferListener{
        void progressUpdated(int progress);
        void transferFinished(boolean success);
    }
}
