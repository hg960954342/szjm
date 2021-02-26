package com.prolog.eis.util.led;

import java.io.UnsupportedEncodingException;

//import java.util.Calendar;

public class ProtocolDemo {

    // ====引入动作方式列表(数值从0开始)====
    // 0 = '随机',
    // 1 = '立即显示',
    // 2 = '左滚显示',
    // 3 = '上滚显示',
    // 4 = '右滚显示',
    // 5 = '下滚显示',
    // 6 = '连续左滚显示',
    // 7 = '连续上滚显示',
    // 8 = '连续右滚',
    // 9 = '连续下滚',
    // 10 = '中间向上下展开',
    // 11 = '中间向两边展开',
    // 12 = '中间向四周展开',
    // 13 = '从右向左移入',
    // 14 = '从左向右移入',
    // 15 = '从左向右展开',
    // 16 = '从右向左展开',
    // 17 = '从右上角移入',
    // 18 = '从右下角移入',
    // 19 = '从左上角移入',
    // 20 = '从左下角移入',
    // 21 = '从上向下移入',
    // 22 = '从下向上移入',
    // 23 = '横向百叶窗',
    // 24 = '纵向百叶窗',
    // =====================================

    // ====引出动作方式列表(数值从0开始)====
    // 0 = '随机',
    // 1 = '不消失',
    // 2 = '立即消失',
    // 3 = '上下向中间合拢',
    // 4 = '两边向中间合拢',
    // 5 = '四周向中间合拢',
    // 6 = '从左向右移出',
    // 7 = '从右向左移出',
    // 8 = '从右向左合拢',
    // 9 = '从左向右合拢',
    // 10 = '从右上角移出',
    // 11 = '从右下角移出',
    // 12 = '从左上角移出',
    // 13 = '从左下角移出',
    // 14 = '从下向上移出',
    // 15 = '从上向下移出',
    // 16 = '横向百叶窗',
    // 17 = '纵向百叶窗'
    // =====================================

    // ====停留动作方式列表(数值从0开始)====
    // 0 = '静态显示',
    // 1 = '闪烁显示'
    // =====================================
    
    public static MyUdpSocket my_udp;
    public static LEDSender2010 ledsender = new LEDSender2010();
    //public static String ledhost = "127.0.0.1";
    public static String ledhost = "10.10.10.43";
    public static int ledport = 6666;
    public static int addr = 0;
    
    public static void mysleep(int msec){
        try{  
            Thread.sleep(msec);  
            }  
            catch (InterruptedException e){  
             e.printStackTrace();  
            }  
    }
    
    //发送UDP包，buffer为发送数据，size为发送数据长度
    public static void udp_send(byte[] buffer, int size){
        byte[] packet = new byte[size];
        ledsender.blockCopy(packet, 0, buffer, 0, size);
        ledsender.print_stream(packet, size);
        try {    
            my_udp.send(ledhost, ledport, packet, size);
        } catch (Exception ex) {    
            ex.printStackTrace();    
        }
    }
    
    //接收UDP包，buffer为接收数据，size为接收数据长度
    public static int udp_receive(byte[] buffer){
        int size=0;
        size=my_udp.receive();
        if(size>0){ 
        	my_udp.get_receive_packet(buffer, size);
        }
        return size;
    }

    public static int do_command(byte[] buffer, int size){
        byte[] packet = new byte[1280];
        int tx, tx_repeat=5;
        int isize=0;
        int i;

        for (tx=0; tx<tx_repeat; tx++){
            udp_send(buffer, size);
            for (i=0; i<50; i++){
            	isize=udp_receive(packet);
                if (isize>0 && ledsender.parse_cmd_respond(packet, isize)==1) 
                {
                	return 1;
                }
            }
        }
        System.out.println("超时");
        return 0;
    }
    
    public static void demo_reset_display(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_reset_display(packet, (byte)addr, 1);
        System.out.println("复位节目显示...");
        do_command(packet, size);
    }
    
    public static void demo_power_on(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_set_power(packet, (byte)addr, 1);
        System.out.println("打开电源...");
        do_command(packet, size);
    }
    
    public static void demo_power_off(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_set_power(packet, (byte)addr, 0);
        System.out.println("关闭电源...");
        do_command(packet, size);
    }
    
    public static void demo_get_power(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_get_power(packet, (byte)addr);
        System.out.println("读取电源状态...");
        do_command(packet, size);
    }
    
    public static void demo_set_bright(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_set_bright(packet, (byte)addr, 7);
        System.out.println("设置亮度...");
        do_command(packet, size);
    }

    public static void demo_get_bright(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_get_bright(packet, (byte)addr);
        System.out.println("读取亮度...");
        do_command(packet, size);
    }
    
    public static void demo_adjust_time(){
        int size;
        byte[] packet = new byte[1280];
        //Calendar calendar = Calendar.getInstance();
        //指定时间校时，12小时制
        //size=ledsender.pkg_adjust_time_ex(packet, (byte)addr, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_WEEK)-1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        //指定时间校时，24小时制
        //size=ledsender.pkg_adjust_time_ex(packet, (byte)addr, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_WEEK)-1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        //以当前系统时间校时
        size=ledsender.pkg_adjust_time(packet, (byte)addr);
        System.out.println("校正时间...");
        do_command(packet, size);
    }

    public static void demo_switch_on(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_set_switch(packet, (byte)addr, 1);
        System.out.println("开启5V输出...");
        do_command(packet, size);
    }

    public static void demo_switch_off(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_set_switch(packet, (byte)addr, 0);
        System.out.println("关闭5V输出...");
        do_command(packet, size);
    }

    public static void demo_get_boardparam(){
        int size;
        byte[] packet = new byte[1280];
        size=ledsender.pkg_get_boardparam(packet, (byte)addr);
        System.out.println("读取控制卡参数...");
        do_command(packet, size);
    }
    
    public static void demo_set_boardparam_ip(String ip){
        int size;
        byte[] packet = new byte[1280];
        byte[] ips = new byte[4];
        int i, k, k1, x;
        size=ledsender.pkg_get_boardparam(packet, (byte)addr);
        System.out.println("读取控制卡参数...");
        if (do_command(packet, size) == 1){
            k=0;
            x=0;
            ip=ip+'.';
            for (i=0; i<4; i++){
                k1 = ip.indexOf('.', k);
                if (k1>k){
                	ips[i] = (byte)Integer.parseInt(ip.substring(k, k1));
                	k = k1+1;
                	x++;
                }
            }
            if (x==4){
            	size=ledsender.pkg_set_boardparam_ip(packet, (byte)addr, ips);
                if (size>0){
                    do_command(packet, size);
                }
            }
        }
    }

    public static void demo_power_schedule(){
        int size;
        byte[] packet = new byte[1280];
        //按一周7天每日定时开关屏，每日可以定义3个时段
        size=ledsender.pkg_power_schedule_weekday(packet, (byte)addr, true);
        //按指定的起止日期时间播放，一共可以定义21个时段
        //size=ledsender.pkg_power_schedule_period(packet, (byte)addr, true);
        System.out.println("设置定时开关屏计划...");
        do_command(packet, size);
    }
    
    //dib_buffer为点阵图片的RGB数组，数组为整形数组，数组的每个单元代表一个像素点的RGB值
    //  像素点(X,Y)的RGB值按照一列一列顺序排列，例如宽64，高32的屏
    //    (0,0),(0,1),(0,2),...(0,31)  (1,0),(1,1),(1,2),...(1,31)  (2,0),(2,1),(2,2),...(2,31)  ... (63,0),(63,1),(63,2),...(63,31)
    //    像素点的RGB值用整数表示，0-7位表示红色，8-15位表示绿色，16-23表示蓝色，24-31位保留置0
    //    即：dib_buffer[n]&0xFF 表示红色
    //        (dib_buffer[n]>>8)&0xFF 表示绿色
    //        (dib_buffer[n]>>16)&0xFF 表示蓝色
    public static int dib_width = 64;
    public static int dib_height = 64;
    public static int dib_buffer_size = dib_width*dib_height; 
    public static int[] dib_buffer = new int[dib_buffer_size];
    public static void draw_dib(){
        int x, y, z=0;
        int seek=0;
        int color=0xFF;
        for (x=0; x<dib_width; x++){
            for (y=0; y<dib_height; y++){
                dib_buffer[seek++]=color;
            }
            z++;
            if (z>=4) {
                switch(color){
                case 0xFF:
                    color=0xFF00;
                    break;
                case 0xFF00:
                    color=0xFFFF;
                    break;
                case 0xFFFF:
                    color=0xFF;
                    break;
                default:
                    color=0xFF;
                    break;
                }
                z=0;
            }
        }
    }
    
    public static void send_data(){
    	int size=0;
        byte[] packet = new byte[1280];
        int i, j, k, x, r;
        int tx, tx_repeat=5;
        boolean ok;
        //下面为通讯代码，包括数据的拆分打包、发送
        //    由于是UDP通讯，增加
        
        //如果数据量大于
        if (ledsender.GetDataSize()>(1<<21)){
            System.out.println("LED节目数据长度超出显示屏存储空间");
            return;
        }
        
        System.out.println("LED节目压缩前数据长度"+ledsender.GetDataSize()+"，数据包数量"+ledsender.get_pkg_count(512));
        ledsender.Compress();
        System.out.println("LED节目压缩后数据长度"+ledsender.GetDataSize()+"，数据包数量"+ledsender.get_pkg_count(512));
        
        //起始包
        ok=false;
        System.out.println("发送起始包...，序列号=0");
        for (tx=0; tx<tx_repeat; tx++){
            size=ledsender.pkg_begin(packet, addr);
            udp_send(packet, size);
            for (x=0; x<50; x++){
            	size=udp_receive(packet);
            	if (size>0 && ledsender.parse_trans_respond(packet, size, LEDSender2010.PKC_BEGIN, 0)>0){
                    System.out.println("起始包发送完成，序列号=0");
                    ok=true;
                    break;
            	}
            }
            if (ok) break;
        }
        if (!ok){
            System.out.println("超时");
            return;
        }

        //数据包
        k=ledsender.get_pkg_count(512);
        i=1;
        while (i<=k){
            ok=false;
            System.out.println("发送数据包...，序列号="+i);
            for (tx=0; tx<tx_repeat; tx++){
            	j=i;
                size=ledsender.pkg_data(packet, addr, i, 512);
                udp_send(packet, size);
                for (x=0; x<50; x++){
                	size=udp_receive(packet);
                	if (size>0){
                		r=ledsender.parse_trans_respond(packet, size, LEDSender2010.PKC_DATA, i);
                		switch(r){
                		case 0x1:
                        	System.out.println("数据包发送完成，序列号="+i);
                            ok=true;
                            j=i+1;
                			break;
                		case 0x2:
                        	System.out.println("数据包序列号校对错误，应收="+i+"，实收="+ledsender.fix_serialno);
                			j=ledsender.fix_serialno;
                			break;
                		}
                        //ok=true;
                        //break;
                	}
                    if (ok) break;
                }
                i=j;
                if (ok) break;
            }
            if (!ok){
                System.out.println("超时");
                return;
            }
        }

        //结束包
        ok=false;
        System.out.println("发送结束包...，序列号="+i);
        for (tx=0; tx<tx_repeat; tx++){
            size=ledsender.pkg_end(packet, addr, i);
            udp_send(packet, size);
            for (x=0; x<50; x++){
            	size=udp_receive(packet);
            	if (size>0 && ledsender.parse_trans_respond(packet, size, LEDSender2010.PKC_END, i)>0){
                    System.out.println("结束包发送完成，序列号="+i);
                    ok=true;
                    break;
            	}
            }
            if (ok) break;
        }
        if (!ok){
            System.out.println("超时");
            return;
        }

        /*
        ok=false;
        for (x=0; x<100; x++){
        	size=udp_receive(packet);
        	switch (ledsender.parse_notify(packet, size)){
        	case LEDSender2010.NOTIFY_ROOT_PLAY:
                System.out.println("发送节目数据校验成功");
                ok=true;
                break;
        	case LEDSender2010.NOTIFY_ROOT_DOWNLOAD:
                System.out.println("下载节目数据校验成功");
        		ok=true;
                break;
        	}
        	if (ok) break;
        }
        if (!ok) System.out.println("节目数据校验错误，发送/下载失败");
        */
    }
    
    public static void demo_upgrade(){
    	ledsender.Upgrade("d:\\vsd.bin");
    	send_data();
    }
    
    public static void demo_play(){
        //String[] formats={"#y", "年", "#m", "月", "#d", "日", " ", "#h", ":", "#n", ":", "#s"};
        //String[] formats={"#h", ":", "#n", ":", "#s"};

        //生成节目数据
        ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);
    	//ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_THREE);
    	//ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_FULL);
        ledsender.AddChapter(1, 1000);
        ledsender.AddRegion(0, 0, 256, 64);
        
        /*
        //点阵图片
        ledsender.AddLeaf(1, 2000);
        //draw_dib();
        //ledsender.AddDib(0, 0, 64, 64, dib_buffer, dib_buffer_size, dib_width, dib_height, 1, 5, 2, 5, 0, 0, 1000);
        BufferedImage bi;
        try {
            bi = LEDSender2010.createImageEx("中华人民共和国", new Font("宋体", Font.BOLD, 24), Color.BLACK, Color.CYAN, 0, false, 64);
            System.out.println("width:"+bi.getWidth()+"height:"+bi.getHeight());
            ledsender.AddWindow(0, 0, 64, 64, LEDSender2010.getBufferedImageRGB(bi), bi.getWidth() * bi.getHeight(), bi.getWidth(), bi.getHeight(), 2, 0, 2, 0, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        
        //点阵文字（单行，支持对齐方式）
        ledsender.AddLeaf(1, 2000);
        //ledsender.AddTextJuhao(0, 0, 64, 32, "欢迎光临。谢谢惠顾。", "宋体", 24, Color.BLACK, Color.RED, 0, 2, true, 2, 0, 2, 0, 0, 0, 0);
        ledsender.AddTextEx(0, 0, 128, 32, "123567354732684238", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 16, Color.BLACK, new Color(255,0,0), 0, 1, 0, 2, 0, 0, 0, 0);
        //ledsender.AddTextEx(128, 0, 160, 32, "谢谢惠顾", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 16, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);

        //ledsender.AddLeaf(1, 2000);
        //ledsender.AddTextEx(0, 0, 128, 32, "同一个中国", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 16, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);
        //ledsender.AddTextEx(128, 0, 160, 32, "同一个梦想", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 16, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);

        /*
        //24点阵内码文字
        ledsender.AddLeaf(1, 2000);
        ledsender.AddString(0, 0, 64, 32, "欢迎光临abc", 2, 0, 2, 0, 0, 0, 0, 0xFF, 1);
        //24点阵日期时间，格式yyyy年mm月dd日
        ledsender.AddLeaf(1, 2000);
        ledsender.AddStringDateTime(0, 0, 256, 32, 0xFFFF, 1, formats, 6);
        */

        send_data();
    }

    public static void demo_play2(){

    	//生成节目数据
        ledsender.MakeRoot(LEDSender2010.ROOT_DOWNLOAD, LEDSender2010.COLOR_TYPE_DOUBLE);
    	//ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_THREE);
        ledsender.AddChapter(1, 1000000);
        ledsender.AddRegion(0, 0, 256, 64);
        
        //点阵文字（单行，支持对齐方式）
        ledsender.AddLeaf(1, 200000);
        ledsender.AddWindows(0, 0,64, 32);
        ledsender.AddChildText("欢迎光临", "宋体", 16, Color.BLACK, Color.RED, 0, 0, false, 6, 0, 2, 0, 0, 0, 0);
        ledsender.AddChildText("谢谢惠顾", "宋体", 16, Color.BLACK, Color.RED, 0, 0, false, 6, 0, 2, 0, 0, 0, 0);

        send_data();
    }

    public static void demo_play_chapterschedule(){
        //String[] formats={"#y", "年", "#m", "月", "#d", "日", " ", "#h", ":", "#n", ":", "#s"};
        //String[] formats={"#h", ":", "#n", ":", "#s"};

        //生成节目数据
        ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);

        ledsender.AddChapterEx(1, 1000, 1, 1, 0x7F, 2016, 8, 17, 0, 0, 0, 2016, 12, 27, 23, 59, 59);
        ledsender.AddRegion(0, 0, 64, 64);
        
        //点阵文字（自动换行）
        ledsender.AddLeaf(1, 2000);
        ledsender.AddText(0, 0, 64, 32, "欢迎光临abcd", "宋体", 14, Color.BLACK, Color.RED, 0, 2, true, 3, 0, 2, 0, 0, 0, 0);

        ledsender.AddChapterEx(1, 1000, 1, 1, 0x7F, 2016, 4, 28, 10, 10, 0, 2016, 12, 27, 23, 15, 0);
        ledsender.AddRegion(0, 0, 64, 64);
        
        //点阵文字（单行，支持对齐方式）
        ledsender.AddLeaf(1, 2000);
        ledsender.AddTextEx(0, 0, 64, 32, "谢谢惠顾abc", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);

        send_data();
    }

    public static void demo_play_picture(){
        //生成节目数据
        ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);
        ledsender.AddChapter(1, 1000);
        ledsender.AddRegion(0, 0, 256, 32);
        ledsender.AddLeaf(1, 2000);
        ledsender.AddPicture(0, 0, 256, 32, "E:\\prolog\\项目\\群创\\LED屏幕\\VS2010版二次开发包SDK_V160816\\JavaDemo\\Demo.bmp", 1, 0, 2, 0, 0, 0, 0);
        send_data();
    }

    public static void demo_play_voice(){
        //生成节目数据
        ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);
        ledsender.AddChapter(1, 1000);
        ledsender.AddRegion(0, 0, 64, 64);
        ledsender.AddLeaf(1, 2000);
        ledsender.AddTextEx(0, 0, 64, 32, "欢迎光临", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 16, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);
        ledsender.AddVoice("欢迎光临", 5000, 10, 0, 5, 5, 1);
        send_data();
    }

    public static void demo_datetime_play(){
        String[] formats_datetime={"#y", "年", "#m", "月", "#d", "日", " ", "#h", ":", "#n", ":", "#s"};
        String[] formats_date={"#y", "年", "#m", "月", "#d", "日"};
        String[] formats_time={"#h", ":", "#n", ":", "#s"};
        String[] formats_week={"星期", "#w"};

        //生成节目数据
        ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);
        //ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_THREE);
        ledsender.AddChapter(1, 10000);
        ledsender.AddRegion(0, 0, 256, 64);

        ledsender.AddLeaf(1, 2000);
        ledsender.AddDateTime(0, 16, 256, 32, formats_datetime, 12, "宋体", 14, Color.BLACK, Color.YELLOW, 0);
//        ledsender.AddLeaf(1, 2000);
        ledsender.AddDateTime(0, 0, 128, 32, formats_date, 6, "宋体", 14, Color.BLACK, Color.RED, 0);
//        ledsender.AddLeaf(1, 2000);
//        ledsender.AddDateTime(0, 0, 128, 32, formats_time, 5, "宋体", 14, Color.BLACK, Color.GREEN, 0);
//        ledsender.AddLeaf(1, 2000);
//        ledsender.AddDateTime(0, 0, 128, 32, formats_week, 2, "宋体", 14, Color.BLACK, Color.RED, 0);

        send_data();
    }

    public static void demo_schedule_child_play(){
        //生成节目数据
        //String[] formats_datetime={"#y", "年", "#m", "月", "#d", "日", " ", "#h", ":", "#n", ":", "#s"};

        ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);
        ledsender.AddChapter(1, 10000);
        ledsender.AddRegion(0, 0, 64, 64);

        ledsender.AddLeaf(1, 2000);
        ledsender.AddWindows(0, 0, 64, 32);
//        ledsender.AddChildText("abc", "宋体", 14, Color.BLACK, Color.RED, 0, false, 1, 0, 2, 0, 0, 0, 0);
        ledsender.AddChildScheduleText("欢迎光临", "宋体", 14, Color.BLACK, Color.RED, 0, 0, false, 1, 0, 2, 0, 0, 0, 2000, 2, 0x0, 2018, 2, 5, 7, 10, 0, 2018, 2, 6, 23, 15, 0);
        ledsender.AddChildScheduleText("谢谢惠顾", "宋体", 14, Color.BLACK, Color.RED, 0, 0, false, 1, 0, 2, 0, 0, 0, 2000, 2, 0x0, 2018, 2, 5, 7, 10, 0, 2018, 2, 6, 23, 15, 0);
        ledsender.AddChildScheduleText("同一个世界", "宋体", 14, Color.BLACK, Color.RED, 0, 0, false, 1, 0, 2, 0, 0, 0, 2000, 2, 0x0, 2018, 2, 8, 7, 10, 0, 2018, 2, 9, 23, 15, 0);
        ledsender.AddChildScheduleText("同一个梦想", "宋体", 14, Color.BLACK, Color.RED, 0, 0, false, 1, 0, 2, 0, 0, 0, 2000, 2, 0x0, 2018, 2, 8, 7, 10, 0, 2018, 2, 9, 23, 15, 0);

        send_data();
    }
    
    public static void demo_fontset_import(MyUdpSocket udp, String ip, int num, String text){
    	byte[] packet = new byte[1024];
    	byte[] s; 
    	int i, size;
    	int c;

    	size=0;
		try {
	    	s=text.getBytes("GB2312");
	    	size=0;
			packet[size++]=0x55;
	    	packet[size++]=0x25;
	    	packet[size++]=0x3E;
	    	packet[size++]=(byte)(((num/100)%10)+0x30);
	    	packet[size++]=(byte)(((num/10)%10)+0x30);
	    	packet[size++]=(byte)((num%10)+0x30);
			for (i=0; i<s.length; i++){
				if (s[i]>=0) c=s[i]; else c=(256+s[i]);
				switch(c>>4){
				case 0: packet[size++]=0x30; break;
				case 1: packet[size++]=0x31; break;
				case 2: packet[size++]=0x32; break;
				case 3: packet[size++]=0x33; break;
				case 4: packet[size++]=0x34; break;
				case 5: packet[size++]=0x35; break;
				case 6: packet[size++]=0x36; break;
				case 7: packet[size++]=0x37; break;
				case 8: packet[size++]=0x38; break;
				case 9: packet[size++]=0x39; break;
				case 10: packet[size++]=0x61; break;
				case 11: packet[size++]=0x62; break;
				case 12: packet[size++]=0x63; break;
				case 13: packet[size++]=0x64; break;
				case 14: packet[size++]=0x65; break;
				case 15: packet[size++]=0x66; break;
				default: packet[size++]=0x30; break;
				}
				switch(c&0xF){
				case 0: packet[size++]=0x30; break;
				case 1: packet[size++]=0x31; break;
				case 2: packet[size++]=0x32; break;
				case 3: packet[size++]=0x33; break;
				case 4: packet[size++]=0x34; break;
				case 5: packet[size++]=0x35; break;
				case 6: packet[size++]=0x36; break;
				case 7: packet[size++]=0x37; break;
				case 8: packet[size++]=0x38; break;
				case 9: packet[size++]=0x39; break;
				case 10: packet[size++]=0x61; break;
				case 11: packet[size++]=0x62; break;
				case 12: packet[size++]=0x63; break;
				case 13: packet[size++]=0x64; break;
				case 14: packet[size++]=0x65; break;
				case 15: packet[size++]=0x66; break;
				default: packet[size++]=0x30; break;
				}
			}
			packet[size++] = 0x23;
			packet[size++] = (byte)0xAA;
		} catch (Exception e) {
		}

		if (size>0)	{
	        try {    
	            udp = new MyUdpSocket(8868);
	            udp.send(ip, 6666, packet, size);
	        } catch (Exception ex) {    
	            ex.printStackTrace();    
	        }
		}
    }

    //只更新一个节目
    public static void demo_play_sub_chapter(){
    	ledsender.MakeChapter(LEDSender2010.ROOT_PLAY_CHAPTER, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 1, 1000);
        ledsender.AddRegion(0, 0, 64, 64);
        ledsender.AddLeaf(1, 2000);
        ledsender.AddTextEx(0, 0, 64, 32, "abc", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);
        send_data();
    }

    public static void demo_play_sub_chapter_ex(){
        ledsender.MakeChapterEx(LEDSender2010.ROOT_PLAY_CHAPTER, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 1, 1000, 1, 1, 0x7F, 2016, 8, 17, 0, 0, 0, 2016, 12, 27, 23, 59, 59);
        ledsender.AddRegion(0, 0, 64, 64);
        ledsender.AddLeaf(1, 2000);
        ledsender.AddTextEx(0, 0, 64, 32, "abc", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);
        send_data();
    }

    //只更新一个分区
    public static void demo_play_sub_region(){
    	ledsender.MakeRegion(LEDSender2010.ROOT_PLAY_REGION, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 1, 0, 16, 128, 16);
        ledsender.AddLeaf(1, 2000);
        ledsender.AddTextEx(0, 0, 64, 32, "abc", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);
        send_data();
    }

    //只更新一个页面
    public static void demo_play_sub_leaf(){
        String[] formats_datetime={"#h", ":", "#n", ":", "#s"};
    	ledsender.MakeLeaf(LEDSender2010.ROOT_PLAY_LEAF, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 1, 1, 1000);
        ledsender.AddTextEx(0, 0, 64, 16, "abc", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);
        ledsender.AddDateTime(0, 16, 64, 16, formats_datetime, 12, "宋体", 14, Color.BLACK, Color.YELLOW, 0);
        send_data();
    }

    //只更新一个对象
    public static void demo_play_sub_object(){
    	ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 1);
    	//此处只能添加一个对象
        ledsender.AddTextEx(0, 0, 64, 32, "abc", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);
        send_data();
    }

    public static void main(String[] args) {

        // TODO Auto-generated method stub
    	
        try {    
            my_udp = new MyUdpSocket(8818);
        } catch (Exception ex) {    
            ex.printStackTrace();    
        }

//        demo_play2();
//        demo_play_voice();
//    	demo_fontset_import(my_udp, "10.10.10.43", 0, "你好朋友123");
        //demo_reset_display();
//        demo_play_chapterschedule();
        //demo_upgrade();
//        demo_datetime_play();
//        demo_play_picture();
//        demo_schedule_child_play();
//        demo_get_boardparam();
//        demo_play_sub_chapter();
        //demo_set_boardparam_ip("192.168.1.99");
//        demo_power_off();
//        demo_power_on();
//        demo_get_power();
//        demo_set_bright();
//        demo_get_bright();
//        demo_adjust_time();
//        demo_switch_on();
//        demo_switch_off();
//        demo_power_schedule();
        
        mydemo();
//        refresh();
//        try {
//			test();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        System.out.println("运行结束.");
    }
    
    public static void mydemo() {

        //String[] formats={"#y", "年", "#m", "月", "#d", "日", " ", "#h", ":", "#n", ":", "#s"};
        //String[] formats={"#h", ":", "#n", ":", "#s"};

//        //生成节目数据
//        ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);
//
//        ledsender.AddChapterEx(1, 1000, 1, 1, 0x7F, 2016, 8, 17, 0, 0, 0, 2016, 12, 27, 23, 59, 59);
//        ledsender.AddRegion(0, 0, 256, 32);
//        
//        //点阵文字（自动换行）
//        ledsender.AddLeaf(1, 2000);
//        ledsender.AddTextEx(40, 16, 256, 16, "ruku", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
//        ledsender.AddTextEx(130, 16, 126, 16, "零一二三四五六七八九", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 1000);
//        ledsender.AddTextEx(0, 0, 256, 16, "佛山群志原物料自动仓储系统", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.YELLOW, 0, 1, 0, 1, 0, 0, 0, 0);
//        ledsender.AddTextEx(0, 16, 64, 16, "状态：", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
//        ledsender.AddTextEx(90, 16, 64, 16, "信息：", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
//        
////        ledsender.AddRegion(0, 0, 256, 32);
//        
//        
////        ledsender.AddText(0, 0, 256, 32, "佛山群志原物料自动仓储系统", "宋体", 14, Color.BLACK, Color.RED, 0, 2, true, 3, 0, 2, 0, 0, 0, 0);
//
////        ledsender.AddChapterEx(1, 1000, 1, 1, 0x7F, 2016, 4, 28, 10, 10, 0, 2016, 12, 27, 23, 15, 0);
////        ledsender.AddRegion(0, 0, 64, 64);
////        
////        //点阵文字（单行，支持对齐方式）
////        ledsender.AddLeaf(1, 2000);
////        ledsender.AddTextEx(0, 0, 128, 32, "谢谢惠顾abc", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.RED, 0, 1, 0, 2, 0, 0, 0, 0);
//
//        send_data();
        
        
        
        
        
        
        
        
        
        
        
        
        

		// led屏幕规格 32x192
		
		// 指定ledip
		ledhost = "10.10.10.73";
		
		//生成节目数据
		ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);
		
		// chapter 0
		ledsender.AddChapter(1, 1000);
		// region 0
		ledsender.AddRegion(0, 0, 192, 32);
		
		//点阵文字（自动换行）
		// leaf 0
		ledsender.AddLeaf(1, 2000);
		// object 0
		ledsender.AddTextEx(40, 16, 192, 16, "state", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
		// object 1
		ledsender.AddTextEx(130, 16, 126, 16, "01234567890123456789", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
		// object 2
		ledsender.AddTextEx(0, 0, 192, 16, "佛山群志原物料自動倉儲系統", LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.YELLOW, 0, 1, 0, 1, 0, 0, 0, 0);
		// object 3
		ledsender.AddTextEx(0, 16, 64, 16, "狀態：", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
		// object 4
		ledsender.AddTextEx(90, 16, 64, 16, "信息：", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
		
		send_data();
	
    
    }
    
    public static void refresh() {
    	if(false) {
    		ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 0);
    		//此处只能添加一个对象
    		ledsender.AddTextEx(40, 16, 256, 16, "入库", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
    		send_data();
    	}else {
    		ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 1);
    		//此处只能添加一个对象
//        ledsender.AddTextEx(130, 16, 126, 16, "这是绿色的字", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 2, 1, 2, 0, 1, 2, 2000);
    		ledsender.AddTextEx(130, 16, 126, 16, "这是红色的字", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.RED, 0, 2, 1, 2, 0, 1, 2, 2000);
//        ledsender.AddTextEx(130, 16, 126, 16, "这是黄色的字", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.YELLOW, 0, 2, 1, 2, 0, 1, 2, 2000);
    		send_data();
    	}
    }
    
    public static void test() throws UnsupportedEncodingException {
    	String stateStr = "入库";
    	int messageType = 0;
    	String message = "正常";
        // 更新stateStr
        ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 0);
        ledsender.AddTextEx(40, 16, 256, 16, stateStr, LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
		send_data();
		
		// 更新message
        ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 1);
        Color fontcolor = Color.GREEN;// 字体颜色，默认绿色
        int inmethod = 1;// 引入方式，默认直接显示
        // 信息类型 0正常 10报警 20异常
        if(messageType == 0) {
        	fontcolor = Color.GREEN;
        }else if(messageType == 10) {
        	fontcolor = Color.YELLOW;
        }else if(messageType == 20) {
        	fontcolor = Color.RED;
        }
        
        if(message.getBytes("gbk").length <= 18) {
        	// 不需要滚动
            inmethod = 1;// 引入方式，直接显示
        }else {
        	// 滚动
        	inmethod = 2;// 引入方式，左滚显示
        }
        ledsender.AddTextEx(130, 16, 126, 16, message, LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, fontcolor, 0, inmethod, 0, 2, 0, 0, 0, 2000);
		send_data();
	
    }

}
