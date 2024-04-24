package com.zhangyj.oneclick.component.business.readpdf;

import com.google.common.collect.Lists;
import com.zhangyj.oneclick.component.entity.bo.ExpenseBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangyj
 */
@Slf4j
public abstract class AbstractTripTableHandler implements ITripTableHandler {


	/**
	 * 获取目标数据内容
	 * @param contents 内容
	 * @return 数据内容
	 */
	@Override
	public List<ExpenseBO> getTargetContents(List<String> contents){
		if(CollectionUtils.isEmpty(contents)){
			return null;
		}
		List<ExpenseBO> result = Lists.newArrayList();
		boolean start = false;
		int index = 0;
		for (String content : contents) {
			if(start) {
				Pattern p = Pattern.compile("^\\d+");
				Matcher m = p.matcher(content);
				if (m.find() && Integer.parseInt(m.group()) == ++index) {
					log.info(content);
					try {
						result.add(getTargetContent(content));
					}catch (Exception e){
						log.error("解析pdf文本对象异常：" + content);
						throw e;
					}
				}
			}else {
				if(content.startsWith(getStartSign())){
					start = true;
				}
			}
		}
		return result;
	}

	/**
	 * 获取目标数据内容
	 * @param content 原始数据内容
	 * @return 目标数据内容
	 */
	protected abstract ExpenseBO getTargetContent(String content);

	/**
	 * 是否匹配当前规则
	 * @param contents 内容
	 * @param keyWord 关键字
	 * @return 是否匹配
	 */
	protected boolean match(List<String> contents, String keyWord){
		for (String content : contents) {
			if (content.contains(keyWord)) {
				return true;
			}
		}
		return false;
	}
}
