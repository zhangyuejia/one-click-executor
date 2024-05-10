package com.zhangyj.oneclick.component.common.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdTransferDirConfig extends CmdReplaceConfig {

	/**
	 * 文件夹路径
	 */
	private String path;


	/**
	 * 文件名前缀
	 */
	private String[] ignoreRegex;

	private String dataServerUrl;
}

