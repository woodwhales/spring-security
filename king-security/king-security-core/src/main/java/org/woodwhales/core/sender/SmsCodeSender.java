package org.woodwhales.core.sender;

public interface SmsCodeSender {
    void send(String mobile, String code);
}
