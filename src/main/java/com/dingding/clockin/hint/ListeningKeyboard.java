package com.dingding.clockin.hint;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

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
                            System.out.println(result);
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
}
