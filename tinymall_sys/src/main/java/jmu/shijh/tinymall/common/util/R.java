package jmu.shijh.tinymall.common.util;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.Writer;

public class R {

    public static class JRespBuildAction {
        private final JsonResp resp;

        private JRespBuildAction(Integer code, Boolean success) {
            resp = new JsonResp();
            resp.setSuccess(success);
            resp.setCode(code);
            if (success) resp.setMessage("请求成功");
        }

        public JRespBuildAction msg(String m) {
            resp.setMessage(m);
            return this;
        }

        public JRespBuildAction data(Object data) {
            resp.setData(data);
            return this;
        }

        public JRespBuildAction help(String m) {
            resp.setHelp(m);
            return this;
        }

        public JsonResp build() { return resp;}

        public void writeTo(Writer writer) throws IOException {
            writer.write(JSON.toJSONString(resp));
        }
    }

    /**
     * 自定义状态码
     *
     * @param code 状态码
     * @return response构造器
     */
    public static JRespBuildAction cus(int code) {
        return new JRespBuildAction(code,code == HttpStatus.OK.value());
    }
    public static JRespBuildAction ok() {
        return cus(HttpStatus.OK.value());
    }
    public static JRespBuildAction error() {
        return cus(HttpStatus.BAD_REQUEST.value());
    }
}
