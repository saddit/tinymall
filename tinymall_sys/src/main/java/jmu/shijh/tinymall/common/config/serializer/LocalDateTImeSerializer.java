package jmu.shijh.tinymall.common.config.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class LocalDateTImeSerializer implements ObjectSerializer {
    private final DateTimeFormatter dateTimeFormatter;

    public LocalDateTImeSerializer(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.CHINA);
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object o1, Type type, int i) throws IOException {
        if (object == null) {
            serializer.out.writeNull();
        } else {
            LocalDateTime date = (LocalDateTime)object;
            String text = date.format(dateTimeFormatter);
            serializer.write(text);
        }
    }
}
