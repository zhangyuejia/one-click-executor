package com.zhangyj.oneclick.component.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.zhangyj.oneclick.component.common.config.CmdMovePointerKeyboardConfig;
import com.zhangyj.oneclick.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalTime;

/**
 * @author zhang.yuejia1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdMovePointerKeyboardServiceImpl extends AbstractCmdService<CmdMovePointerKeyboardConfig> {

    @Override
    public void exec(CmdMovePointerKeyboardConfig config) throws Exception {
        // 停止时间 20:20
        LocalTime stopTime = LocalTime.of(Integer.parseInt(config.getStopTime().substring(0, 2)),
                Integer.parseInt(config.getStopTime().substring(3, 5)),
                Integer.parseInt(config.getStopTime().substring(6, 8)));
        // 禁用无头模式，避免创建Robot抛出异常
        System.setProperty("java.awt.headless", "false");
        Robot robot = new Robot();
        while (true) {
            // 比较当前时间是否在20:20之后
            if (LocalTime.now().isAfter(stopTime)) {
                return;
            }
            // 模拟鼠标移动（偏移1像素再移回）
            PointerInfo pointer = MouseInfo.getPointerInfo();
            Point point = pointer.getLocation();
            robot.mouseMove(point.x + 1, point.y);
            robot.mouseMove(point.x, point.y);

            // 或模拟键盘操作（如按下/释放Alt键）
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            // 每1分钟操作一次（小于15分钟阈值）
            ThreadUtil.sleep(config.getSleepInterval() * 1000);
        }
    }

    @Override
    public String getDesc() {
        return "模拟键盘鼠标移动";
    }
}