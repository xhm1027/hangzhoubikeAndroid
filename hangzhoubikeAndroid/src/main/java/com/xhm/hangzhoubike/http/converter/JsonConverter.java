package com.xhm.hangzhoubike.http.converter;

import com.xhm.hangzhoubike.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import retrofit.mime.TypedString;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * A {@link retrofit.converter.Converter} which uses SimpleXML for reading and writing entities.
 *
 * @author Fabien Ric (fabien.ric@gmail.com)
 */
public class JsonConverter implements Converter {
    private final static String DEFAULT_CHARSET = "UTF-8";


    @Override
    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
        String charset = DEFAULT_CHARSET;
        if (typedInput.mimeType() != null) {
            charset = MimeUtil.parseCharset(typedInput.mimeType());
        }
        try {
            InputStream body =  typedInput.in();
            String bodyStr = new String(ResponseUtil.readStream(body), charset);
//            Gson gson = new Gson();
//            return gson.fromJson(bodyStr,type);
            return JSONObject.parseObject(bodyStr, type);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object source) {
        return new TypedString(JSONObject.toJSONString(source));
    }
}

