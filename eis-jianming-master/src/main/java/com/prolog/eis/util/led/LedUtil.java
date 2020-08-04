package com.prolog.eis.util.led;

import com.prolog.eis.dao.led.LedPortParamMapper;
import com.prolog.eis.dto.eis.led.LedPortParamDto;
import com.prolog.eis.util.SpringContextUtils;
import com.prolog.framework.utils.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class LedUtil {
	
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
    public static StringBuffer ledhost = new StringBuffer("10.10.10.43");
    public static int ledport = 6666;
    public static int addr = 0;
    
    static {
    	String title = "佛山群志原物料自動倉儲系統";
    	String stateTitle = "狀態：";
    	String messageTitle = "信息：";
    	
    	LedPortParamMapper ledPortParamMapper = (LedPortParamMapper) SpringContextUtils.getBean("ledPortParamMapper");
    	List<LedPortParamDto> list = ledPortParamMapper.getLedPortParam();
    	try {
    		my_udp = new MyUdpSocket(8818);
    		for(LedPortParamDto dto : list) {
    			initLed(dto.getLedIp(), dto.getLedTitle(), stateTitle, messageTitle, dto.getUda0());
    		}
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }
    
    /**
     * 发送UDP包，buffer为发送数据，size为发送数据长度
     * @param buffer
     * @param size
     */
    private static void udp_send(byte[] buffer, int size){
        byte[] packet = new byte[size];
        ledsender.blockCopy(packet, 0, buffer, 0, size);
        ledsender.print_stream(packet, size);
        try {    
            my_udp.send(ledhost.toString(), ledport, packet, size);
        } catch (Exception ex) {    
            ex.printStackTrace();    
        }
    }
    
    /**
     * 接收UDP包，buffer为接收数据，size为接收数据长度
     * @param buffer
     * @return
     */
    private static int udp_receive(byte[] buffer){
        int size=0;
        size=my_udp.receive();
        if(size>0){ 
        	my_udp.get_receive_packet(buffer, size);
        }
        return size;
    }
    
    private static void send_data(){
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
    
    /**
     * 初始化led屏
     * @param ip
     * @param title
     * @param stateTitle
     * @param messageTitle
     */
    public static void initLed(String ip, String title, String stateTitle, String messageTitle, String ledSize) {
    	if("32x256".equals(ledSize)) {
    		// led屏幕规格 32x256
    		
    		// 指定ledip
    		ledhost = new StringBuffer(ip);
    		
    		//生成节目数据
    		ledsender.MakeRoot(LEDSender2010.ROOT_PLAY, LEDSender2010.COLOR_TYPE_DOUBLE);
    		
    		// chapter 0
    		ledsender.AddChapter(1, 1000);
    		// region 0
    		ledsender.AddRegion(0, 0, 256, 32);
    		
    		//点阵文字（自动换行）
    		// leaf 0
    		ledsender.AddLeaf(1, 2000);
    		// object 0
    		ledsender.AddTextEx(40, 16, 256, 16, "state", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
    		// object 1
    		ledsender.AddTextEx(130, 16, 126, 16, "message", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
    		// object 2
    		ledsender.AddTextEx(0, 0, 256, 16, title, LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.YELLOW, 0, 1, 0, 1, 0, 0, 0, 0);
    		// object 3
    		ledsender.AddTextEx(0, 16, 64, 16, stateTitle, LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
    		// object 4
    		ledsender.AddTextEx(90, 16, 64, 16, messageTitle, LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
    		
    		send_data();
    	}else if("32x192".equals(ledSize)){
    		// led屏幕规格 32x192
    		
    		// 指定ledip
    		ledhost = new StringBuffer(ip);
    		
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
    		ledsender.AddTextEx(130, 16, 126, 16, "message", LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
    		// object 2
    		ledsender.AddTextEx(0, 0, 192, 16, title, LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.YELLOW, 0, 1, 0, 1, 0, 0, 0, 0);
    		// object 3
    		ledsender.AddTextEx(0, 16, 64, 16, stateTitle, LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
    		// object 4
    		ledsender.AddTextEx(90, 16, 64, 16, messageTitle, LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
    		
    		send_data();
    	}
    }
	
    /**
     * 发送消息到指定led屏
     * @param ip
     * @param stateStr
     * @param messageType
     * @param message
     * @param title 
     * @throws UnsupportedEncodingException
     */
	public static void sendMsg(String ip, String stateStr, int messageType, String message, String ledSize, LedPortParamDto title) throws UnsupportedEncodingException {
		
		if(StringUtils.isEmpty(ledSize) || "32x256".equals(ledSize)) {
			// 指定ledip
			ledhost = new StringBuffer(ip);
			
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
			
			// 更新title
			ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 2);
    		ledsender.AddTextEx(0, 0, 256, 16, title.getLedTitle(), LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.YELLOW, 0, 1, 0, 1, 0, 0, 0, 0);
			send_data();
			
			// 更新stateTitle
			ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 3);
    		ledsender.AddTextEx(0, 16, 64, 16, title.getUda1(), LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
			send_data();
			
			// 更新messageTitle
			ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 4);
    		ledsender.AddTextEx(90, 16, 64, 16, title.getUda2(), LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
			send_data();
			
		}else if("32x192".equals(ledSize)) {
			// 指定ledip
			ledhost = new StringBuffer(ip);
			
			// 更新stateStr
			ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 0);
			ledsender.AddTextEx(40, 16, 192, 16, stateStr, LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 2, 0, 0, 0, 0);
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
			
			if(message.getBytes("gbk").length <= 9) {
				// 不需要滚动
				inmethod = 1;// 引入方式，直接显示
			}else {
				// 滚动
				inmethod = 2;// 引入方式，左滚显示
			}
			ledsender.AddTextEx(130, 16, 126, 16, message, LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, fontcolor, 0, inmethod, 0, 2, 0, 0, 0, 2000);
			send_data();
			
			// 更新title
			ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 2);
    		ledsender.AddTextEx(0, 0, 192, 16, title.getLedTitle(), LEDSender2010.ALIGN_CENTER, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.YELLOW, 0, 1, 0, 1, 0, 0, 0, 0);
			send_data();
			
			// 更新stateTitle
			ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 3);
    		ledsender.AddTextEx(0, 16, 64, 16, title.getUda1(), LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
			send_data();
			
			// 更新messageTitle
			ledsender.MakeObject(LEDSender2010.ROOT_PLAY_OBJECT, LEDSender2010.COLOR_TYPE_DOUBLE, 1, 0, 0, 0, 4);
    		ledsender.AddTextEx(90, 16, 64, 16, title.getUda2(), LEDSender2010.ALIGN_LEFT, LEDSender2010.ALIGN_TOP, "宋体", 14, Color.BLACK, Color.GREEN, 0, 1, 0, 1, 0, 0, 0, 0);
			send_data();
		}
    	
	}
}
