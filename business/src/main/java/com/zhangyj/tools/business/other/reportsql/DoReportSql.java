package com.zhangyj.tools.business.other.reportsql;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zhangyj.tools.business.other.reportsql.enums.ChannelEnum;
import com.zhangyj.tools.business.other.reportsql.pojo.ExcelRecord;
import com.zhangyj.tools.business.other.reportsql.pojo.ExcelRecordVO;
import com.zhangyj.tools.business.other.reportsql.pojo.RecordDeleteVO;
import com.zhangyj.tools.common.base.IExec;
import com.zhangyj.tools.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangyj
 */
@Slf4j
public class DoReportSql implements IExec {

    private final static List<String> IGNORE_WORD = Lists.newArrayList("小计", "黄色");

    private final static String TABLE_SQL = "#{[channelName]}_MT_REPORT#{[year]}";

    private final static String SQL = "INSERT INTO " + TABLE_SQL + " (ECID, USERID, TASKID, SPGATE, IYMD, IHOUR, PTCODE, IMONTH, ICOUNT,  RSUCC, RFAIL1, RFAIL2, RNRET, RELEASEFLAG, Y, SPISUNCM, SPID, SVRTYPE, P1, P2, P3, P4, SENDTYPE, MOBILEAREA, BATCHID, AREACODE) \n" +
            "SELECT 100001, 'SCZH01', 0, '#{[spGate]}', #{[iYmd]}, 9, '', #{[iMonth]}, #{[iCount]},  #{[rSucc]}, #{[rFail]}, 0, #{[rNet]}, 1, #{[year]}, 0, '#{[spId]}', 'M00000', '0', '0000000000000000', ' ', ' ', 1, 0, 0, 86\n" +
            "FROM DUAL WHERE NOT EXISTS(SELECT 1 FROM SMS_MT_REPORT#{[year]} WHERE IYMD = #{[iYmd]} AND SPID = '#{[spId]}');\n" +
            "UPDATE " + TABLE_SQL + " SET ICOUNT=0, RSUCC=0, RFAIL1=0, RFAIL2=0, RNRET = 0 WHERE IYMD = #{[iYmd]} AND SPID = '#{[spId]}';\n" +
            "UPDATE " + TABLE_SQL + " SET ICOUNT=#{[iCount]}, RSUCC=#{[rSucc]}, RFAIL1=#{[rFail]}, RFAIL2=0, RNRET = #{[rNet]}\n" +
            "WHERE ID = (SELECT T.ID FROM (SELECT ID FROM " + TABLE_SQL + " WHERE IYMD = #{[iYmd]} AND SPID = '#{[spId]}' LIMIT 1) T);";

    private final ThreadLocal<SimpleDateFormat> dateLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    private final List<String> sqlList = new ArrayList<>();

    private final Set<RecordDeleteVO> deleteVOSet = new HashSet<>();

    @Override
    public void exec() throws IOException {
        String excelPath = "G:\\Users\\ZYJ\\Desktop\\杭州天翼智慧城市科技有限公司2021-12月-22年6月数据0915.xls";
        String sqlPath = "G:\\Users\\ZYJ\\Desktop\\杭州天翼报表调整.sql";
        String sheetName = "调整后数据-日明细";
        EasyExcel.read(excelPath, ExcelRecord.class, new ReadListener<ExcelRecord>(){
            @Override
            public void invoke(ExcelRecord record, AnalysisContext analysisContext) {
                log.debug("读取到数据:{}", JSON.toJSONString(record));
                for (String ignoreWord : IGNORE_WORD) {
                    if(record.getDate().contains(ignoreWord)){
                        log.info("跳过忽略关键词:{}", ignoreWord);
                        return;
                    }
                }

                ExcelRecordVO recordVO = getExcelRecordVO(record);
                sqlList.add(StringUtil.parseTplContent(SQL, recordVO));
                RecordDeleteVO deleteVO = new RecordDeleteVO();
                BeanUtils.copyProperties(recordVO, deleteVO);
                deleteVOSet.add(deleteVO);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {

            }
        }).sheet(sheetName).doRead();
        putDeleteSql();
        FileUtils.writeLines(new File(sqlPath), this.sqlList);
    }

    private void putDeleteSql() {
        Map<String, List<RecordDeleteVO>> listMap = deleteVOSet.stream().collect(Collectors.groupingBy(v -> v.getYear() + "_" +v.getChannelName()));
        for (List<RecordDeleteVO> list : listMap.values()) {
            StringBuilder builder = new StringBuilder();
            builder.append(StringUtil.parseTplContent("DELETE FROM " + TABLE_SQL +" WHERE NOT EXISTS(SELECT 1 FROM DUAL WHERE \n", list.get(0)));
            for (int i = 0; i < list.size(); i++) {
                builder.append("        ");
                if(i != 0){
                    builder.append("OR ");
                }
                builder.append(StringUtil.parseTplContent("(IYMD = #{[iYmd]}         AND SPID = '#{[spId]}') \n", list.get(i)));
            }
            builder.append(");");
            this.sqlList.add(builder.toString());
        }
    }

    private ExcelRecordVO getExcelRecordVO(ExcelRecord record) {
        ExcelRecordVO recordVO = new ExcelRecordVO();
        BeanUtils.copyProperties(record, recordVO);
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.parse(record.getDate()));
        recordVO.setYear(String.valueOf(cal.get(Calendar.YEAR)));
        recordVO.setIYmd(dateLocal.get().format(cal.getTime()));
        recordVO.setIMonth(String.valueOf(cal.get(Calendar.MONTH) + 1));
        ChannelEnum channelEnum = ChannelEnum.getByValue(record.getChannelTypeZh());
        if (channelEnum == null) {
            throw new RuntimeException("数据错误：" + record.getChannelTypeZh());
        }
        recordVO.setChannelName(channelEnum.name());
        recordVO.setSpGate(channelEnum.getSpGate());
        return recordVO;
    }
}
