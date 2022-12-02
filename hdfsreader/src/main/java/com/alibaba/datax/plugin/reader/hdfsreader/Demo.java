package com.alibaba.datax.plugin.reader.hdfsreader;

import com.google.common.collect.Lists;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat;
import org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.mapred.*;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.format.converter.ParquetMetadataConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.metadata.ParquetMetadata;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.MessageColumnIO;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @program: DataX
 * @description:
 * @author: liuningbo
 * @create: 2022/09/28 09:35
 */
public class Demo {
    private static org.apache.hadoop.conf.Configuration hadoopConf = null;

    public static void main(String[] args) throws IOException, SerDeException {

        String str="String";
        System.out.println(str.toLowerCase());

        hadoopConf = new org.apache.hadoop.conf.Configuration();
        hadoopConf.set("fs.defaultFS", "hdfs://192.168.2.131:8020");
        hadoopConf.setBoolean("fs.hdfs.impl.disable.cache", true);// 解决Filesystem closed

        String patt="hdfs://192.168.2.131:8020/warehouse/tablespace/managed/hive/portrait_89.db/portrait_tag_0/tag_code=md5phone/2a48c35e0083a1ca-dd20dab600000000_1923147184_data.0.parq";

        patt ="hdfs://192.168.2.131:8020/warehouse/tablespace/managed/hive/portrait_89.db/portrait_tag_0/tag_code=workflow_test/7e457f48e0451396-2178ecc800000000_644026456_data.0.parq";
        JobConf conf = new JobConf(hadoopConf);

        Path parquetFilePath = new Path(patt);
        Properties p = new Properties();

        List<List<Object>> dataList = Lists.newArrayList();
        ParquetMetadata readFooter = ParquetFileReader.readFooter(conf, parquetFilePath, ParquetMetadataConverter.NO_FILTER);
        MessageType schema = readFooter.getFileMetaData().getSchema();
        ParquetFileReader r = new ParquetFileReader(conf, readFooter.getFileMetaData(), parquetFilePath, readFooter.getBlocks(), schema.getColumns());



        PageReadStore pages = null;
        try {
            while (null != (pages = r.readNextRowGroup())) {
                final long rows = pages.getRowCount();

// logger.info(file.getName()+" 行数: " + rows);
                final MessageColumnIO columnIO = new ColumnIOFactory().getColumnIO(schema);
                final org.apache.parquet.io.RecordReader<Group> recordReader = columnIO.getRecordReader(pages, new GroupRecordConverter(schema));

//                final MessageColumnIO columnIO = new ColumnIOFactory().getColumnIO(schema);
//
//                final RecordReader recordReader = (RecordReader) columnIO.getRecordReader(pages, new GroupRecordConverter(schema));

                for (int i = 0; i < rows; i++) {
//                    System.out.println(i+"=================>"+rows);
// System.out.println(recordReader.shouldSkipCurrentRecord());
                    final Group g = recordReader.read();
                    GroupType groupType = g.getType();

                    StringBuilder sb =new StringBuilder();
                    sb.append(i);
                    List<Type> fields = groupType.getFields();
                    for (int j = 0; j < fields.size(); j++) {
                        String valueToString = g.getValueToString(j, 0);
//                        Type type = fields.get(j);
                        sb.append(",").append(valueToString);
                    }
                    System.out.println(sb.toString());

//                    if (i == 0) {
//                        table.setColumns(parquetColumn(g));
//                        i++;
//                    }
// 获取行数据
//                    List<Object> row = getparquetData(table.getColumns(), g);
//                    dataList.add(row);
                }
            }
        } finally {
            r.close();
        }


//        ParquetHiveSerDe serde = new ParquetHiveSerDe();
//        serde.initialize(conf, p);
//        StructObjectInspector inspector = (StructObjectInspector) serde.getObjectInspector();
        InputFormat<?, ?> in = new MapredParquetInputFormat();
        FileInputFormat.setInputPaths(conf, parquetFilePath.toString());

//        ParquetReader reader = new ParquetReader(new Path(inPath),readSupport);


        InputSplit[] splits = in.getSplits(conf, 1);
        RecordReader reader = in.getRecordReader(splits[0], conf, Reporter.NULL);
        Object key = reader.createKey();
        Object value = reader.createValue();

        while (reader.next(key, value)) {
            System.out.println(key);
            System.out.println(value);
        }
        reader.close();

    }



//    private static List getparquetData(List columns, Group line) {
//
//        List row = new ArrayList<>();
//
//        Object cellStr = null;
//
//        for (int i = 0; i < columns.size(); i++) {
//
//            try {
//
//                switch (columns.get(i).getType()) {
//
//                    case "DOUBLE":
//
//                        cellStr = line.getDouble(i, 0);
//
//                        break;
//
//                    case "FLOAT":
//
//                        cellStr = line.getFloat(i, 0);
//
//                        break;
//
//                    case "BOOLEAN":
//
//                        cellStr = line.getBoolean(i, 0);
//
//                        break;
//
//                    case "INT96":
//
//                        cellStr = line.getInt96(i, 0);
//
//                        break;
//
//                    case "LONG":
//
//                        cellStr = line.getLong(i, 0);
//
//                        break;
//
//                    default:
//
//                        cellStr = line.getValueToString(i, 0);
//
//                }
//
//            } catch (RuntimeException e) {
//
//            } finally {
//
//                row.add(cellStr);
//
//            }
//
//        }
//
//        return row;
//
//    }
}
