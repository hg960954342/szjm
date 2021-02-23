package com.prolog.eis.controller.led;



import cn.hutool.core.util.StrUtil;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.Bx5GScreenProfile;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.TextBinary;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class PrologLedViewService {

    private static DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);


    public void reStore(String ip, int port, String itemName, double weight, String lotId, String state) throws Exception {
        //Bx5GEnv.initial("log.properties");

        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");


        if (!screen.connect(ip, port)) {
            //System.out.println("connect failed");
            return;
        }

        Bx5GScreenProfile profile = screen.getProfile();

        // 获取显示特技方式列表，此变量会下面用到
        //DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

        //创建节目
        ProgramBxFile p000 = new ProgramBxFile("P000", profile);
        //创建第一块区域
        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea);

        int size = 8;
        String padChar = "    ";
        String s1 = StrUtil.center(itemName, size, padChar);

        //创建数据页
        TextBxPage page = new TextBxPage("  品名：" + s1);
        PrologLedViewService.setPageStyle(page);

        //创建第二块区域
        TextCaptionBxArea tArea2 = new TextCaptionBxArea(0, 24, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea2);

        String padChar1 = "   ";
        String newWeight = weight + "公斤";
        String s2 = StrUtil.center(newWeight, size, padChar1);

        //创建数据页
        TextBxPage page2 = new TextBxPage("  重量：" + s2);
        PrologLedViewService.setPageStyle(page2);

        //创建第三块区域
        TextCaptionBxArea tArea3 = new TextCaptionBxArea(0, 48, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea3);

        String padChar2 = "    ";
        String s3 = StrUtil.center(lotId, size, padChar2);

        //创建数据页
        TextBxPage page3 = new TextBxPage("  批号：" + s3);
        PrologLedViewService.setPageStyle(page3);

        //创建第四块区域
        TextCaptionBxArea tArea4 = new TextCaptionBxArea(0, 72, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea4);

        String padChar3 = "     ";
        String s4 = StrUtil.center(state, size, padChar3);

        //创建数据页
        TextBxPage page4 = new TextBxPage("  状态：" + s4);
        PrologLedViewService.setPageStyle(page4);

        tArea.addPage(page);
        tArea2.addPage(page2);
        tArea3.addPage(page3);
        tArea4.addPage(page4);

        // 将 area 添加到节目中
        p000.addArea(tArea);
        p000.addArea(tArea2);
        p000.addArea(tArea3);
        p000.addArea(tArea4);

        if (p000.validate() != null) {
            return;
        }
        // 创建一个 list
        ArrayList<ProgramBxFile> plist = new ArrayList<ProgramBxFile>();
        plist.add(p000);

        //删除之前写入的程序
        screen.deletePrograms();

        // 1. writeProgramsAsync - 异步方式，即SDK会自己起线程来发送
        screen.writeProgramsAsync(plist, new WriteProgramTextCaptionWithStyle());

        //断开连接
        //screen.disconnect();

    }

    public void outStore(String ip, int port, String itemName, BigDecimal weight, String lotId, String pickStation) throws Exception {
        //Bx5GEnv.initial("log.properties");

        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");


        if (!screen.connect(ip, port)) {
            //System.out.println("connect failed");
            return;
        }

        Bx5GScreenProfile profile = screen.getProfile();

        // 获取显示特技方式列表，此变量会下面用到
        //DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

        //创建节目
        ProgramBxFile p000 = new ProgramBxFile("P000", profile);
        //创建第一块区域
        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea);

        int size = 8;
        String padChar = "    ";
        String s1 = StrUtil.center(itemName, size, padChar);

        //创建数据页
        TextBxPage page = new TextBxPage("  品名：" + s1);
        PrologLedViewService.setPageStyle(page);

        //创建第二块区域
        TextCaptionBxArea tArea2 = new TextCaptionBxArea(0, 24, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea2);

        String padChar1 = "   ";
        String newWeight = weight + "公斤";
        String s2 = StrUtil.center(newWeight, size, padChar1);

        //创建数据页
        TextBxPage page2 = new TextBxPage("  重量：" + s2);
        PrologLedViewService.setPageStyle(page2);

        //创建第三块区域
        TextCaptionBxArea tArea3 = new TextCaptionBxArea(0, 48, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea3);

        String padChar2 = "    ";
        String s3 = StrUtil.center(lotId, size, padChar2);

        //创建数据页
        TextBxPage page3 = new TextBxPage("  批号：" + s3);
        PrologLedViewService.setPageStyle(page3);

        //创建第四块区域
        TextCaptionBxArea tArea4 = new TextCaptionBxArea(0, 72, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea4);

        String padChar3 = "     ";
        String s4 = StrUtil.center(pickStation, size, padChar3);

        //创建数据页
        TextBxPage page4 = new TextBxPage("  站点：" + s4);
        PrologLedViewService.setPageStyle(page4);

        tArea.addPage(page);
        tArea2.addPage(page2);
        tArea3.addPage(page3);
        tArea4.addPage(page4);

        // 将 area 添加到节目中
        p000.addArea(tArea);
        p000.addArea(tArea2);
        p000.addArea(tArea3);
        p000.addArea(tArea4);

        if (p000.validate() != null) {
            return;
        }
        // 创建一个 list
        ArrayList<ProgramBxFile> plist = new ArrayList<ProgramBxFile>();
        plist.add(p000);

        //删除之前写入的程序
        screen.deletePrograms();

        // 1. writeProgramsAsync - 异步方式，即SDK会自己起线程来发送
        screen.writeProgramsAsync(plist, new WriteProgramTextCaptionWithStyle());

        //断开连接
        //screen.disconnect();

    }

    public void pick(String ip, int port, String itemName, BigDecimal pickWeight, String lotId, BigDecimal reWeight, String pickStation) throws Exception {
        //Bx5GEnv.initial("log.properties");

        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");


        if (!screen.connect(ip, port)) {
            //System.out.println("connect failed");
            return;
        }

        Bx5GScreenProfile profile = screen.getProfile();

        // 获取显示特技方式列表，此变量会下面用到
        //DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

        //创建节目
        ProgramBxFile p000 = new ProgramBxFile("P000", profile);
        //创建第一块区域
        TextCaptionBxArea tArea = new TextCaptionBxArea(0, 0, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea);

        int size = 8;
        String padChar = "    ";
        String ps = "  " + pickStation;
        String s1 = StrUtil.center(itemName, size, padChar);

        //创建数据页
        TextBxPage page = new TextBxPage(ps + "：" + s1);
        PrologLedViewService.setPageStyle(page);

        //创建第二块区域
        TextCaptionBxArea tArea2 = new TextCaptionBxArea(0, 24, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea2);

        String padChar1 = "   ";
        String pWeight = pickWeight + "公斤";
        String s2 = StrUtil.center(pWeight, size, padChar1);

        //创建数据页
        TextBxPage page2 = new TextBxPage("  拣重：" + s2);
        PrologLedViewService.setPageStyle(page2);

        //创建第三块区域
        TextCaptionBxArea tArea3 = new TextCaptionBxArea(0, 48, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea3);

        String padChar2 = "    ";
        String s3 = StrUtil.center(lotId, size, padChar2);

        //创建数据页
        TextBxPage page3 = new TextBxPage("  批号：" + s3);
        PrologLedViewService.setPageStyle(page3);

        //创建第四块区域
        TextCaptionBxArea tArea4 = new TextCaptionBxArea(0, 72, 192, 24, screen.getProfile());
        PrologLedViewService.setAreaStyle(tArea4);

        String rWeight = reWeight + "公斤";
        String s4 = StrUtil.center(rWeight, size, padChar1);

        //创建数据页
        TextBxPage page4 = new TextBxPage("  回重：" + s4);
        PrologLedViewService.setPageStyle(page4);

        tArea.addPage(page);
        tArea2.addPage(page2);
        tArea3.addPage(page3);
        tArea4.addPage(page4);

        // 将 area 添加到节目中
        p000.addArea(tArea);
        p000.addArea(tArea2);
        p000.addArea(tArea3);
        p000.addArea(tArea4);

        if (p000.validate() != null) {
            return;
        }
        // 创建一个 list
        ArrayList<ProgramBxFile> plist = new ArrayList<ProgramBxFile>();
        plist.add(p000);

        //删除之前写入的程序
        screen.deletePrograms();

        // 1. writeProgramsAsync - 异步方式，即SDK会自己起线程来发送
        screen.writeProgramsAsync(plist, new WriteProgramTextCaptionWithStyle());

        //断开连接
        //screen.disconnect();

    }

    public static void setAreaStyle(TextCaptionBxArea tarea) throws IOException {
        // 使能区域边框
        tarea.setFrameShow(true);
        tarea.setFrameStyle(7);
        // 使用内置边框1
        tarea.loadFrameImage(1);
    }

    public static void setPageStyle(TextBxPage page) {
        // 设置文本水平对齐方式
        page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
        // 设置文本垂直居中方式
        page.setVerticalAlignment(TextBinary.Alignment.CENTER);
        // 设置文本字体
        page.setFont(new Font("宋体", Font.PLAIN, 15));         // 字体
        // 设置文本颜色
        page.setForeground(Color.red);
        // 设置区域背景色，默认为黑色
        page.setBackground(Color.darkGray);
        // 调整特技方式
        page.setDisplayStyle(styles[1]);
        // 调整特技速度
        page.setSpeed(10);
    }
}
