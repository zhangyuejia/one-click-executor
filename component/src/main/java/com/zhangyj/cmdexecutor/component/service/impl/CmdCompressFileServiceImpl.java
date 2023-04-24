package com.zhangyj.cmdexecutor.component.service.impl;

import com.zhangyj.cmdexecutor.component.common.config.CmdCompressFileConfig;
import com.zhangyj.cmdexecutor.core.service.AbstractCmdService;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 压缩文件功能
 * @author zhangyj
 */
@Slf4j
@Service
public class CmdCompressFileServiceImpl extends AbstractCmdService<CmdCompressFileConfig> {

    @Override
    public void exec() throws Exception {
        File file = new File(config.getCompressDir());
        compressFile(file, config.getCompressPassword());
    }

    private static void compressFile(File file, String password) throws Exception {
        String zipPath  = file.getParentFile().getCanonicalPath() + File.separator + file.getName() + ".zip";
        net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(zipPath);

        ZipParameters parameters =new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if(StringUtils.isNotBlank(password)){
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(password);
        }
        if(file.isFile()){
            zipFile.addFile(file, parameters);
        }else {
            zipFile.addFolder(file, parameters);
        }
    }
}
