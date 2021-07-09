package jmu.shijh.tinymall.common.util;

import java.util.Random;

public class CryptoKeys {
    public static final String AES_KEY = "SHI+JH+AES+KEY";

    /**
     * 生成唯一的主键
     * 格式:时间+随机数
     * */
    public static synchronized String randomNumKey(){//为了防止重复 加个synchronized
        Random random=new Random();
        Integer number=random.nextInt(900000)+100000;//六位随机数
        return System.currentTimeMillis()+String.valueOf(number);
    }
}
