package com.alibaba.datax.core.transport.transformer;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.transformer.Transformer;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * @program: DataX
 * @description:
 * @author: liuningbo
 * @create: 2022/12/01 10:38
 */
public class AESTransformer extends Transformer {
    public AESTransformer() {
        setTransformerName("dx_aes");
    }
    public static final String AES_KEY = "W($3%_w@";

    @Override
    public Record evaluate(Record record, Object... paras) {
        int columnIndex;
        try {
            if (paras.length != 1) {
                throw new RuntimeException("dx_aes paras must be 1");
            }
            columnIndex = (Integer) paras[0];
        } catch (Exception e) {
            throw DataXException.asDataXException(TransformerErrorCode.TRANSFORMER_ILLEGAL_PARAMETER, "paras:" + Arrays.asList(paras).toString() + " => " + e.getMessage());
        }
        Column column = record.getColumn(columnIndex);

        try {

            if (!(column instanceof StringColumn)){
                return  record;
            }
            String oriValue = column.asString();
            //如果字段为空，跳过replace处理
            if(oriValue == null){
                return  record;
            }
            String newValue =  aesEncrypt(oriValue);
            record.setColumn(columnIndex, new StringColumn(newValue));
        } catch (Exception e) {
            throw DataXException.asDataXException(TransformerErrorCode.TRANSFORMER_RUN_EXCEPTION, e.getMessage(),e);
        }
        return record;
    }

    public  String aesEncrypt(String content) {
        try {
            byte[] keyBytes = Arrays.copyOf(AES_KEY.getBytes("ASCII"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = content.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(Hex.encodeHex(ciphertextBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
