package com.juns.wechat.xmpp.util;

import android.os.UserManager;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
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
import java.io.StringReader;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Created by 王者 on 2016/8/19.
 */
public class FileTransferManager {
    private static final FileTransferManager mInstance = new FileTransferManager();
    private AbstractXMPPConnection xmppConnection;
    private static final int PORT = 7700;
    private final String account = AccountManager.getInstance().getUserName();
    private int lastProgress = 0;

    public static FileTransferManager getInstance(){
        return mInstance;
    }

    private FileTransferManager(){
        xmppConnection = XmppConnUtil.getXmppConnection();
    }

    public void sendFile(File file, String otherName, FileTransferListener listener) {
        try {
            Socket socket = new Socket(ConfigUtil.getXmppServer(), PORT);
            OutputStream out = socket.getOutputStream();
            out.write((byte) 5);  //version

            byte[] fromData = ConfigUtil.getXmppJid(account).getBytes();
            fromData = Arrays.copyOf(fromData, 32);
            byte[] toData = ConfigUtil.getXmppJid(otherName).getBytes();
            toData = Arrays.copyOf(toData, 32);
            out.write(fromData);
            out.write(toData);
            String mimeType = URLConnection.guessContentTypeFromName(file.getPath());
            byte[] mimeTypeData = Arrays.copyOf(mimeType.getBytes(), 16);
            out.write(mimeTypeData);
            int fileNameLength = file.getName().length();
            out.write(fileNameLength);
            out.write(file.getName().getBytes());
            int fileSize = (int) file.length();
            byte[] fileSizeData = Arrays.copyOf(String.valueOf(fileSize).getBytes(), 4);
            out.write(fileSizeData);

            lastProgress = 0;
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
        }
        listener.transferFinished(false);
    }

    private void notifyProgressUpdated(FileTransferListener listener, int wrote, int amount){
        int progress = (int) ((((float) wrote) / amount) * 100);
        if(progress == 100 || (progress - lastProgress > 5 + 5 *Math.random())){
            lastProgress = progress;
            listener.progressUpdated(progress);
        }
    }

    public interface FileTransferListener{
        void progressUpdated(int progress);
        void transferFinished(boolean success);
    }
}
