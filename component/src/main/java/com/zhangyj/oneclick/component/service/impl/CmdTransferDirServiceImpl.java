package com.zhangyj.oneclick.component.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.zhangyj.oneclick.component.common.config.CmdTransferDirConfig;
import com.zhangyj.oneclick.core.common.util.StrUtils;
import com.zhangyj.oneclick.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * @author zhangyj
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CmdTransferDirServiceImpl extends AbstractCmdService<CmdTransferDirConfig> {

	private String basePath;

	private final List<Pattern> ignorePatterns = new ArrayList<>();

	@Override
	public void exec() throws Exception {
		File baseDir = new File(config.getPath());
		if (!baseDir.exists()) {
			throw new RuntimeException("文件路径不存在：" + config.getPath());
		}

		for (String ignoreRegex : config.getIgnoreRegex()) {
			ignorePatterns.add(Pattern.compile(ignoreRegex));
		}
		this.basePath = baseDir.getPath();
		processFiles(baseDir);
	}

	private void processFiles(File dirFile) throws Exception{
		if (dirFile.isDirectory()) {
			processDir(dirFile);
		}else {
			processFile(dirFile);
		}
	}

	private void processFile(File file) throws Exception{
		String relativePath = file.getPath().substring(this.basePath.length() + 1);
		for (Pattern ignorePattern : ignorePatterns) {
			if (ignorePattern.matcher(relativePath).find()) {
				log.info("跳过：" + relativePath);
				return;
			}
		}
		log.info("读取：" + relativePath);
		Map<String, Object> map = new HashMap<>();
		map.put("c", StrUtils.compressString(FileUtil.readString(file, Charset.defaultCharset())));
		map.put("n", StrUtils.compressString(relativePath));
		HttpResponse httpResponse = HttpRequest.post(config.getDataServerUrl() + "/acceptDir")
				.body(JSONUtil.toJsonStr(map))
				.execute();
		httpResponse.close();
	}

	private void processDir(File dirFile) throws Exception{
		File[] files = dirFile.listFiles();
		if(files == null){
			return;
		}
		for (File file : files) {
			processFiles(file);
		}
	}

	@Override
	public String getDesc() {
		return "迁移文件夹";
	}
}
