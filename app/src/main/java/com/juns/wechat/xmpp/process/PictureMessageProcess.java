package com.juns.wechat.xmpp.process;

import android.content.Context;

import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.chat.PictureMsg;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.xmpp.util.FileTransferManager;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;


/*******************************************************
 * Created by 王宗文 on 2015/11/27
 *******************************************************/
public class PictureMessageProcess extends MessageProcess {

    public PictureMessageProcess(Context context) {
        super(context);
    }

    @Override
    public void processMessage(MessageBean messageBean) {
        super.processMessage(messageBean);
        PictureMsg pictureMsg = (PictureMsg) messageBean.getMsgObj();
        FileTransferManager.getInstance().downloadFile(pictureMsg.imgName, new MyProgressListener(messageBean));
    }

    class MyProgressListener implements FileTransferManager.ProgressListener{
        private MessageBean messageBean;
        public MyProgressListener(MessageBean messageBean){
            this.messageBean = messageBean;
        }

        @Override
        public void progressUpdated(int progress) {
           updateProgress(progress);
        }

        @Override
        public void transferFinished(boolean success) {
            if(success){
                updateProgress(100);
            }
        }

        private void updateProgress(int progress){
            PictureMsg pictureMsg = (PictureMsg) messageBean.getMsgObj();
            pictureMsg.progress = progress;
            messageBean.setMsg(pictureMsg.toJson());
            WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.PACKET_ID, "=", messageBean.getPacketId());
            KeyValue keyValue = new KeyValue(MessageBean.MSG, messageBean.getMsg());
            MessageDao.getInstance().update(whereBuilder, keyValue);
        }
    }
}
