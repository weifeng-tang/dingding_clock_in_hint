package com.dingding.clockin.hint;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.swing.*;

public class TimingHint implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //弹框,选择对话框
        JOptionPane.showMessageDialog(null, "到点下班啦, 请记得打卡下班哟");

    }
}
