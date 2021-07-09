package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.domain.dto.SmsCode;

public interface SmsService {

    void SendCode(SmsCode smsCode) throws Exception;
}
