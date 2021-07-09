package jmu.shijh.tinymall.shiro.sms;

import org.apache.shiro.authc.AuthenticationToken;

public class SmsToken implements AuthenticationToken {

    private final String telephone;
    private final String verifyCode;

    public SmsToken(String telephone, String verifyCode) {
        this.telephone = telephone;
        this.verifyCode = verifyCode;
    }

    public String getPhone() {
        return telephone;
    }

    @Override
    public Object getPrincipal() {
        return telephone;
    }

    @Override
    public Object getCredentials() {
        return verifyCode;
    }
}
