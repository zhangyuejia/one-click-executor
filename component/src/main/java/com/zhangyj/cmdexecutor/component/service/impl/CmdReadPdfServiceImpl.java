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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public void exec() throws Exception {
        List<ExpenseBO> data = new ArrayList<>();
        for (String pdfPath : config.getPdfPaths()) {
            String pdfContent = PdfUtils.getPdfContentUseIText(pdfPath);
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
        try (FileOutputStream outputStream = new FileOutputStream(config.getDocOutPath())){
            document.write(outputStream);
        }
    }
}
