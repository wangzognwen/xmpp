package com.juns.wechat.xmpp.iq;

import org.jivesoftware.smack.packet.IQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 王者 on 2016/11/20.
 * 发送语音聊天请求IQ
 */

public class DialIQ extends IQ {
    public static final String ELEMENT = "dial";
    public static final String NAMESPACE = "urn:xmpp:rayo:1";

    private Header<String, String> header;
    private String fromAddress;
    private String toAddress;

    public DialIQ(String fromAddress, String toAddress) {
        super(ELEMENT, NAMESPACE);
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        header = new Header();
    }

    public void addHeaderParam(String key, String value){
        header.put(key, value);
    }

    public void removeHeaderParam(String key){
        header.remove(key);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("from", fromAddress);
        xml.attribute("to", toAddress);
        xml.rightAngleBracket();
        for(String key : header.keySet()){
            xml.append("<header ").append("name=\"").append(key).
                    append("\" value=\"").append(header.get(key)).append("\" />");
        }
        return xml;
    }

    public static class Header<K, V> extends HashMap<K, V>{

    }
}
