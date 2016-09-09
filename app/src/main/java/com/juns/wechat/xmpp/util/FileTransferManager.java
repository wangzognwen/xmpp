package com.juns.wechat.xmpp.util;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.FileUtil;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.PhotoUtil;
import com.juns.wechat.xmpp.XmppConnUtil;
import com.juns.wechat.xmpp.XmppExceptionHandler;
import com.juns.wechat.xmpp.iq.FileTransferIQ;

import org.jivesoftware.smack.AbstractXMPPConnection;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by 王者 on 2016/8/19.
 */
public class FileTransferManager {
    private static final int PORT = 7700;
    private final String account = AccountManager.getInstance().getUserName();
    private int lastProgress = 0;

    public static final int ACTION_READ = 1;
    public static final int ACTION_WRITE = 2;

    public void sendFile(File file, String otherName, ProgressListener listener) {
        try {
            Socket socket = new Socket(ConfigUtil.getXmppServer(), PORT);
            OutputStream out = socket.getOutputStream();
            out.write((byte) 5);  //version
            out.write(ACTION_WRITE);

            byte[] fromData = ConfigUtil.getXmppJid(account).getBytes();
            fromData = Arrays.copyOf(fromData, 32);
            byte[] toData = ConfigUtil.getXmppJid(otherName).getBytes();
            toData = Arrays.copyOf(toData, 32);
            out.write(fromData);
            out.write(toData);
            String mimeType = "image/*";
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
            byte[] data = new byte[8];
            socketIn.read(data);
            socket.close();
            String result = new String(data).trim();
            if("success".equals(result)){
                listener.transferFinished(true);
                return;
            }

        } catch (IOException e) {
            XmppExceptionHandler.handleIOException(e);
        }
        listener.transferFinished(false);
    }

    public void downloadFile(String fileName, int fileSize,  ProgressListener listener){
        LogUtil.i("fileName: " + fileName);
        File file = new File(PhotoUtil.PHOTO_PATH, fileName);
        if(file.exists()){  //由于文件名都是唯一的，说明这张图片是由同一个手机上发出并在本手机上接收。
            listener.transferFinished(true);
            return;
        }

        try {
            Socket socket = new Socket(ConfigUtil.getXmppServer(), PORT);
            OutputStream out = socket.getOutputStream();
            out.write((byte) 5);  //version
            out.write(ACTION_READ);
            out.write(fileName.length());
            out.write(fileName.getBytes());
            out.flush();
            socket.shutdownOutput();

            LogUtil.i("socket write finished!");

            InputStream socketIn = new DataInputStream(socket.getInputStream());
            byte[] data = new byte[7]; //字符串末位为'\0';
            socketIn.read(data);
            String result = new String(data).trim();
            LogUtil.i("result: " + result);
            if(!"success".equals(result)){
                listener.transferFinished(false);
                return;
            }

            OutputStream fileOut = FileUtil.getOutputStream(file);
            if(fileOut == null){
                listener.transferFinished(false);
                return;
            }
            byte[] buffer = new byte[1024];
            int wrote = 0;
            int len = 0;
            while ((len = socketIn.read(buffer)) != -1){
                fileOut.write(buffer, 0, len);
                wrote += len;
                notifyProgressUpdated(listener, wrote, fileSize);
            }
            fileOut.close();
            socket.close();
            listener.transferFinished(true);
        } catch (IOException e) {
            e.printStackTrace();
            listener.transferFinished(false);
        }
    }

    private void notifyProgressUpdated(ProgressListener listener, int wrote, int amount){
        int progress = (int) ((((float) wrote) / amount) * 100);
        if(progress == 100 || (progress - lastProgress > 10 + 10 *Math.random())){
            lastProgress = progress;
            listener.progressUpdated(progress);
        }
    }

    public interface ProgressListener {
        void progressUpdated(int progress);
        void transferFinished(boolean success);
    }
}
