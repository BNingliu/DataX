package com.alibaba.datax.plugin.unstructuredstorage.reader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

public class ColumnEntry {
    private Integer index;
    private String type;
    private String value;
    private String partitionName;
    private String format;
    private DateFormat dateParse;
    private String CIBIndex;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        if (StringUtils.isNotBlank(this.format)) {
            this.dateParse = new SimpleDateFormat(this.format);
        }
    }

    public DateFormat getDateFormat() {
        return this.dateParse;
    }

    public String toJSONString() {
        return ColumnEntry.toJSONString(this);
    }

    public static String toJSONString(ColumnEntry columnEntry) {
        return JSON.toJSONString(columnEntry);
    }

    public String getPartitionName() {
        return partitionName;
    }

    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }

    public String getCIBIndex() {
        return CIBIndex;
    }

    public void setCIBIndex(String CIBIndex) {
        this.CIBIndex = CIBIndex;
    }
}
