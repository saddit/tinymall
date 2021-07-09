package jmu.shijh.tinymall;

import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.CryptoUtils;
import jmu.shijh.tinymall.common.util.SFIdWoker;
import jmu.shijh.tinymall.common.util.Str;
import org.apache.commons.lang3.ClassUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

public class StaticTest {
    @Test
    public void snowFlakeId() throws Exception {
        for (int i = 0; i < 3; i++) {
            System.out.println(new SFIdWoker(1,1,1).generate());
            Thread.sleep(10);
        }
    }

    @Test
    void md5Hash() throws Exception {
        System.out.println(CryptoUtils.MD5Hash("123", "8fec55e7-0452-49f5-8244-25cdb82bb151").toHex());
    }

    @Test
    void cast() throws Exception {
        List<String> list = Cl.list("1","2","3");
        System.out.println((String[]) list.toArray());
    }

    @Test
    void regex() throws Exception {
        System.out.println("sjh12345678".matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"));
    }

    @Test
    void file() throws Exception {
        File file = new File("D:/code-project/tinymall/file/image/123");
        if (file.exists()) {
            System.out.println("exist");
        } else {
            boolean mkdirs = file.mkdirs();
            System.out.println("make dirs:" + mkdirs);
        }

    }

    @Test
    void strContain() throws Exception {
        String[] split = "a,abc,ab,acb".split(",");
        System.out.println(Arrays.asList(split).contains("ab"));
    }

    @Test
    void uuid() throws Exception {
        for (int i = 0; i < 3; i++) {
            System.out.println(UUID.randomUUID().toString());
            Thread.sleep(10);
        }
    }
}
