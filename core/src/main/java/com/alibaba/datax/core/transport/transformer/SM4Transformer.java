package com.alibaba.datax.core.transport.transformer;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.core.util.SM4Util;
import com.alibaba.datax.core.util.SMHelper;
import com.alibaba.datax.transformer.Transformer;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * @program: DataX
 * @description:
 * @author: liuningbo
 * @create: 2022/12/01 10:38
 */
public class SM4Transformer extends Transformer {
    public SM4Transformer() {
        setTransformerName("dx_sm4");
    }

    @Override
    public Record evaluate(Record record, Object... paras) {
        int columnIndex;
        try {
            if (paras.length != 1) {
                throw new RuntimeException("dx_sm4 paras must be 1");
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

            String newValue =  SMHelper.sm4Encrypt(oriValue);
            record.setColumn(columnIndex, new StringColumn(newValue));
        } catch (Exception e) {
            throw DataXException.asDataXException(TransformerErrorCode.TRANSFORMER_RUN_EXCEPTION, e.getMessage(),e);
        }
        return record;
    }

}
