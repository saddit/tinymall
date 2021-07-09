package jmu.shijh.tinymall.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import jmu.shijh.tinymall.domain.dto.SmsCode;
import jmu.shijh.tinymall.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    //@Autowired
    //private Client smsClient;
    //
    //@Value("${sms.sign-name}")
    //private String signName;

    @Override
    public void SendCode(SmsCode smsCode) throws Exception {
        //SendSmsRequest sendSmsRequest = new SendSmsRequest()
        //        .setPhoneNumbers(smsCode.getPhoneNumber())
        //        .setSignName(signName)
        //        .setTemplateCode(smsCode.getCode());
        //SendSmsResponse response = smsClient.sendSms(sendSmsRequest);
        //if (!response.getBody().getCode().equals("OK")) {
        //    log.warn("send sms to {} fail, {}", smsCode.getPhoneNumber(), response.getBody().getMessage());
        //}
    }
}
