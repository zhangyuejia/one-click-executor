package com.zhangyj.cmdexecutor.component.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.excel.util.StringUtils;
import com.google.common.collect.Lists;
import com.zhangyj.cmdexecutor.component.business.readpdf.BasePdfRule;
import com.zhangyj.cmdexecutor.component.common.config.CmdReadPdfConfig;
import com.zhangyj.cmdexecutor.component.entity.bo.ExpenseBO;
import com.zhangyj.cmdexecutor.core.common.util.FileUtils;
import com.zhangyj.cmdexecutor.core.common.util.PdfUtils;
import com.zhangyj.cmdexecutor.core.common.util.StrUtils;
import com.zhangyj.cmdexecutor.core.service.AbstractCmdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 刷新dns配置
 * @author zhangyj
 */
@Slf4j
@Service
public class CmdReadPdfServiceImpl extends AbstractCmdService<CmdReadPdfConfig> {

    @Resource
    private List<BasePdfRule> pdfRules;

    /**
     * 变量映射 key:变量名
     */
    private final Map<String, Object> paramMap = new HashMap<>();

    @Override
    public void exec() throws Exception {
        File[] files = new File(config.getPdfDir()).listFiles();
        if(files == null){
            return;
        }
        List<ExpenseBO> data = new ArrayList<>();
        for (File file : files) {
            String pdfContent = PdfUtils.getPdfContentUseIText(file.getCanonicalPath());
            List<String> pdfContentList = Lists.newArrayList(pdfContent.split("\n")).stream()
                    .filter(StringUtils::isNotBlank).map(s -> s.replaceAll(" +", " ").trim()).collect(Collectors.toList());
            for (BasePdfRule pdfRule : pdfRules) {
                if(pdfRule.match(pdfContentList)){
                    data.addAll(pdfRule.getTargetContents(pdfContentList));
                }
            }
        }
        generateWord(ListUtil.sortByProperty(data, "date"));
    }

    private void generateWord(List<ExpenseBO> list) throws Exception{
        XWPFDocument document = new XWPFDocument(new FileInputStream(FileUtils.getResourcePath() + "\\component\\file\\交通明细模板.docx"));
        XWPFTable table = document.getTables().get(0);

        int tmplRowNum = 5;
        XWPFTableRow tmplRow = table.getRow(tmplRowNum);
        for (ExpenseBO expenseBO : list) {
            XWPFTableRow xwpfTableRow = table.insertNewTableRow(tmplRowNum++);
            for (XWPFTableCell tableCell : tmplRow.getTableCells()) {
                XWPFTableCell cell = xwpfTableRow.createCell();
                String s = StrUtils.parseTplContent(tableCell.getText(), expenseBO);
                cell.setText(s);
                cell.getCTTc().setTcPr(tableCell.getCTTc().getTcPr());
            }
        }
        table.removeRow(tmplRowNum);
        
        initParamMap(list);
        // 填充变量
        for (XWPFTableRow tableRow : table.getRows()) {
            for (XWPFTableCell tableCell : tableRow.getTableCells()) {
                String cellText = tableCell.getText();
                if(StringUtils.isNotBlank(cellText) && cellText.contains("#")){
                    tableCell.removeParagraph(0);
                    XWPFParagraph newPara = tableCell.addParagraph();
                    newPara.createRun().setText(StrUtils.parseTplContent(cellText, paramMap));
                }
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(config.getDocOutPath())){
            document.write(outputStream);
        }
    }

    private void initParamMap(List<ExpenseBO> list) {
        // 合计
        BigDecimal sum = new BigDecimal("0");
        for (ExpenseBO bo : list) {
            sum = sum.add(new BigDecimal(bo.getMoney()));
        }
        this.paramMap.put("sum", sum.doubleValue());
        // 报销人
        this.paramMap.put("myName", config.getMyName());
        this.paramMap.put("today", DateFormatUtils.format(new Date(), "yyyy年MM月dd日"));

    }
}
