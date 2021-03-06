package com.dingding.clockin.hint;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @description: 监听键盘操作
 * @author: Wayne
 * @date: 2021/1/11
 */
public class ListeningKeyboard {

    private static final int GLOBAL_HOT_KEY_WIN_L = 129;

    public static void main(String[] args) {
        //定时提示, 周一到周五18:00提示下班打卡
        timingHint();
        //监听键盘
        JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_WIN_L,
                JIntellitype.MOD_CONTROL, (int) 'L');
        JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
            @Override
            public void onHotKey(int markCode) {
                //判断当前时间是否在6:00之后, 第二天凌晨2:00前
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 2 , 00);
                LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 18 , 00);
                int result = -2;
                if (!(now.isAfter(beginTime) && now.isBefore(endTime))){
                    switch (markCode) {
                        case GLOBAL_HOT_KEY_WIN_L:
                            String [] options = {"只是离开","好的, 已打卡"};
                            //弹框,选择对话框
                            result = JOptionPane.showOptionDialog(null, "下班啦, 请记得打卡下班哟!!：", "钉钉打卡提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            break;
                        default:
                            return;
                    }
                }
                if (result != -1){
                    try {
                        //锁屏操作
                        Runtime.getRuntime().exec("RunDll32.exe user32.dll,LockWorkStation");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static void timingHint() {
        new Thread(()->{
            cronTiming();
        }).run();
    }

    public static void cronTiming(){
        try    {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            // 定义一次任务
            JobDetail job = JobBuilder.newJob(TimingHint.class)
                    .withIdentity("jobName1", "groupName1").build();
            // 定义执行时间,2秒1次
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("triggerName1", "groupName1")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0 18 ? * 2-6"))
                    .build();
            scheduler.scheduleJob(job, trigger);

        }   catch  (Exception e)   {
            e.printStackTrace();
        }
    }
}
