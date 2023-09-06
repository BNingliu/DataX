package com.alibaba.datax.plugin.writer.qianbasewriter;

import java.util.List;

import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.common.plugin.RecordReceiver;
import com.alibaba.datax.common.spi.Writer;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.rdbms.util.DBUtilErrorCode;
import com.alibaba.datax.plugin.rdbms.writer.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QianBaseWriter extends Writer {
	private static final Logger LOG = LoggerFactory.getLogger(QianBaseWriter.class);

	public static class Job extends Writer.Job {
		private Configuration originalConfig = null;
		private CopyWriterJob copyJob;

		@Override
		public void init() {
			this.originalConfig = super.getPluginJobConf();

			// warn：not like mysql, PostgreSQL only support insert mode, don't
			// use
			String writeMode = this.originalConfig.getString(Key.WRITE_MODE);
			if (null != writeMode) {
				LOG.info(String.format(
						" 因为QianBase Database不支持配置参数项 writeMode: %s, " +
								"QianBase Database仅使用insert sql 插入数据. 所以写入模式(writeMode)实际不生效.",
						writeMode));
			}

			int segment_reject_limit = this.originalConfig.getInt("segment_reject_limit", 0);

			if (segment_reject_limit != 0 && segment_reject_limit < 2) {
				throw DataXException.asDataXException(DBUtilErrorCode.CONF_ERROR, "segment_reject_limit 必须为0或者大于等于2");
			}

			this.copyJob = new CopyWriterJob();
			this.copyJob.init(this.originalConfig);
		}

		@Override
		public void prepare() {
			this.copyJob.prepare(this.originalConfig);
		}

		@Override
		public List<Configuration> split(int mandatoryNumber) {
			return this.copyJob.split(this.originalConfig, mandatoryNumber);
		}

		@Override
		public void post() {
			this.copyJob.post(this.originalConfig);
		}

		@Override
		public void destroy() {
			this.copyJob.destroy(this.originalConfig);
		}

	}

	public static class Task extends Writer.Task {
		private Configuration writerSliceConfig;
		private CopyWriterTask copyTask;

		@Override
		public void init() {
			this.writerSliceConfig = super.getPluginJobConf();
			this.copyTask = new CopyWriterTask();
			this.copyTask.init(this.writerSliceConfig);
		}

		@Override
		public void prepare() {
			this.copyTask.prepare(this.writerSliceConfig);
		}

		public void startWrite(RecordReceiver recordReceiver) {
			this.copyTask.startWrite(recordReceiver, this.writerSliceConfig,
					super.getTaskPluginCollector());
		}

		@Override
		public void post() {
			this.copyTask.post(this.writerSliceConfig);
		}

		@Override
		public void destroy() {
			this.copyTask.destroy(this.writerSliceConfig);
		}
	}
}
