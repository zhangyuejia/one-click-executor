package com.zhangyj.oneclick.component.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.excel.util.StringUtils;
import com.google.common.collect.Lists;
import com.zhangyj.oneclick.component.business.readpdf.BasePdfRule;
import com.zhangyj.oneclick.component.common.config.CmdReadPdfConfig;
import com.zhangyj.oneclick.component.entity.bo.ExpenseBO;
import com.zhangyj.oneclick.core.common.util.FileUtils;
import com.zhangyj.oneclick.core.common.util.PdfUtils;
import com.zhangyj.oneclick.core.common.util.StrUtils;
import com.zhangyj.oneclick.core.service.AbstractCmdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        File pdfFile = new File(config.getPdfDir());
        if(!pdfFile.exists()){
            log.error("文件路径不存在{}", config.getPdfDir());
            return;
        }
        File[] files = pdfFile.isFile()? new File[]{ pdfFile }: pdfFile.listFiles();
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
        initParamMap(list);

        XWPFDocument document = new XWPFDocument(Files.newInputStream(Paths.get(FileUtils.getResourcePath() + "\\component\\file\\交通明细模板.docx")));
        XWPFTable table = document.getTables().get(0);

        int tmplRowNum = 5;
        XWPFTableRow tmplRow = table.getRow(tmplRowNum);
        for (ExpenseBO expenseBO : list) {
            XWPFTableRow xwpfTableRow = table.insertNewTableRow(tmplRowNum++);
            for (XWPFTableCell tableCell : tmplRow.getTableCells()) {
                XWPFTableCell cell = xwpfTableRow.createCell();
                String s = StrUtils.parseTplContent(tableCell.getText(), expenseBO, this.paramMap);
                cell.setText(s);
                cell.getCTTc().setTcPr(tableCell.getCTTc().getTcPr());
            }
        }
        table.removeRow(tmplRowNum);
        

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
        this.paramMap.put("today", DateFormatUtils.format(new Date(), "yyyy年MM月dd日"));
        this.paramMap.putAll(config.getParams());
    }

    @Override
    public String getDesc() {
        return "读取pdf功能";
    }
}
