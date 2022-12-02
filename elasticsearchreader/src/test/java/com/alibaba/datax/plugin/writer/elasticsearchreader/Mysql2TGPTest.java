package com.alibaba.datax.plugin.writer.elasticsearchreader;

import com.alibaba.datax.core.Engine;
import org.junit.Ignore;
import org.junit.Test;
@Ignore
public class Mysql2TGPTest {

    @Test
    public void mysql2tdengine() throws Throwable {
        String[] params = {"-mode", "standalone", "-jobid", "-1", "-job", "src/test/resources/textMysql.json"};
        System.setProperty("datax.home", "../target/datax/datax");
        Engine.entry(params);
    }

}
