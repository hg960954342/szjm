package com.prolog.eis.util.led;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

public class LEDSender2010 {

	// ====引入动作方式列表(数值从0开始)====
	// 0 = '随机',
	// 1 = '立即显示',
	// 2 = '左滚显示',
	// 3 = '上滚显示',
	// 4 = '右滚显示',
	// 5 = '下滚显示',
	// 6 = '连续左滚显示',
	// 7 = '连续上滚显示',
	// 8 = '连续右滚显示',
	// 9 = '连续下滚显示',
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
	
	public static final int PKP_PREFIX = 0x55;
	public static final int PKP_SUFFIX = 0xAA;
	public static final int PKP_FILLCH = 0xBB;

	public static final int ALIGN_LEFT = 0x0;
	public static final int ALIGN_TOP = 0x0;
	public static final int ALIGN_CENTER = 0x1;
	public static final int ALIGN_RIGHT = 0x2;
	public static final int ALIGN_BOTTOM = 0x2;

	public static final int ROOT_PLAY = 0x21;				//节目数据，保存到RAM，掉电丢失
	public static final int ROOT_DOWNLOAD = 0x22;			//节目数据，保存到Flash
	public static final int ROOT_PLAY_CHAPTER = 0x23;		//插入或者更新某一幕
	public static final int ROOT_DOWNLOAD_CHAPTER = 0x24;
	public static final int ROOT_PLAY_REGION = 0x25;		//插入或者更新某一分区 
	public static final int ROOT_DOWNLOAD_REGION = 0x26;
	public static final int ROOT_PLAY_LEAF = 0x27;			//插入或者更新某一页面
	public static final int ROOT_DOWNLOAD_LEAF = 0x28;
	public static final int ROOT_PLAY_OBJECT = 0x29;		//插入或者更新某一对象
	public static final int ROOT_DOWNLOAD_OBJECT = 0x2a;

	public static final int COLOR_TYPE_DOUBLE = 0x2;
	public static final int COLOR_TYPE_THREE = 0x3;
	public static final int COLOR_TYPE_FULL = 0x4;

	public static final int BITMAP_RED = 0x1;
	public static final int BITMAP_GREEN = 0x2;
	public static final int BITMAP_YELLOW = 0x3;
	public static final int BITMAP_COLOR = 0x4;
	public static final int BITMAP_RGB = 0x3;
	public static final int BITMAP_FULLCOLOR = 0x5;

	public static final int PKC_BEGIN = 0;
	public static final int PKC_END = 1;
	public static final int PKC_DATA = 2;
	public static final int PKC_RESPOND = 3;
	public static final int PKC_NOTIFY = 100;

	public static final int PKC_ADJUST_TIME = 6;
	public static final int PKC_GET_PARAM = 7;
	public static final int PKC_SET_PARAM = 8;
	public static final int PKC_GET_POWER = 9;
	public static final int PKC_SET_POWER = 10;
	public static final int PKC_GET_BRIGHT = 11;
	public static final int PKC_SET_BRIGHT = 12;
	public static final int PKC_COM_TRANSFER = 21;
	public static final int PKC_SET_SWITCH = 27;
	public static final int PKC_GET_POWER_SCHEDULE = 60;
	public static final int PKC_SET_POWER_SCHEDULE = 61;
	public static final int PKC_RESET_DISPLAY = 99;
	
	public static final int OBJECT_CHAPTER = 0x3F;
	public static final int OBJECT_REGION = 0x3E;
	public static final int OBJECT_LEAF = 0x30;
	public static final int OBJECT_WINDOW = 0x31;
	public static final int OBJECT_DCLOCK = 0x32;
	public static final int OBJECT_STRING = 0x33;
	public static final int OBJECT_STRING_DCLOCK = 0x35;
	public static final int OBJECT_PIXELS = 0x36;
	public static final int OBJECT_PIXELSET = 0x37;
	public static final int OBJECT_WINDOWS = 0x3B;
	public static final int OBJECT_STRINGS = 0x3C;
	public static final int OBJECT_SCHEDULE_WINDOW = 0x3E;
	public static final int OBJECT_VOICE = 0x5F;
	
	public static final int DF_YEAR = 1;
	public static final int DF_MONTH = 2;
	public static final int DF_DAY = 3;
	public static final int DF_WEEK = 4;
	public static final int DF_HOUR = 5;
	public static final int DF_MINUTE = 6;
	public static final int DF_SECOND = 7;
	public static final int DF_USER = 8;

	public static final int NOTIFY_ROOT_DOWNLOAD = 0x00010003;  //更新Flash播放节目
	public static final int NOTIFY_ROOT_PLAY = 0x00020003;  //更新Flash播放节目

	/*
	typedef struct PKG_HEADER{
			WORD   Command;
			BYTE   srcAddr;
			BYTE   dstAddr;
			DWORD  SerialNo;
	}TPKG_Header, *PPKG_Header;
	*/
	static final int PKG_HEADER_STRUCT_SIZE = 8; //2+1+1+4
	static final int PKG_HEADER_COMMAND_OFFSET = 0;
	static final int PKG_HEADER_SRCADDR_OFFSET = 2;
	static final int PKG_HEADER_DSTADDR_OFFSET = 3;
	static final int PKG_HEADER_SERIALNO_OFFSET = 4;
	
	/*
	typedef struct _SYSTEMTIME {
	    WORD wYear;
	    WORD wMonth;
	    WORD wDayOfWeek;
	    WORD wDay;
	    WORD wHour;
	    WORD wMinute;
	    WORD wSecond;
	    WORD wMilliseconds;
	} SYSTEMTIME, *PSYSTEMTIME, *LPSYSTEMTIME;
	*/
	static final int SYSTEMTIME_SIZE = 16;
	static final int SYSTEMTIME_YEAR_OFFSET = 0;
	static final int SYSTEMTIME_MONTH_OFFSET = 2;
	static final int SYSTEMTIME_WEEK_OFFSET = 4;
	static final int SYSTEMTIME_DAY_OFFSET = 6;
	static final int SYSTEMTIME_HOUR_OFFSET = 8;
	static final int SYSTEMTIME_MINUTE_OFFSET = 10;
	static final int SYSTEMTIME_SECOND_OFFSET = 12;
	static final int SYSTEMTIME_MSECOND_OFFSET = 14;
	
	/*
	TPowerSchedule = packed record
	    Enabled:    DWord;
	    Mode:       DWord;
	    OpenTime:   array [0..20] of TTimeStamp;
	    CloseTime:  array [0..20] of TTimeStamp;
	    Checksum:   DWord;
	end;
	PPowerSchedule = ^TPowerSchedule;
	*/
	static final int PWOERSCHEDULE_SIZE = 348;
	static final int PWOERSCHEDULE_ENABLED_OFFSET = 0;
	static final int PWOERSCHEDULE_MODE_OFFSET = 4;
	static final int PWOERSCHEDULE_OPENTIME_OFFSET = 8;
	static final int PWOERSCHEDULE_CLOSETIME_OFFSET = 176;
	static final int PWOERSCHEDULE_SUNDAY_OPENTIME_OFFSET = 8;
	static final int PWOERSCHEDULE_SUNDAY_CLOSETIME_OFFSET = 176;
	static final int PWOERSCHEDULE_MONDAY_OPENTIME_OFFSET = 32;
	static final int PWOERSCHEDULE_MONDAY_CLOSETIME_OFFSET = 200;
	static final int PWOERSCHEDULE_TUESDAY_OPENTIME_OFFSET = 56;
	static final int PWOERSCHEDULE_TUESDAY_CLOSETIME_OFFSET = 224;
	static final int PWOERSCHEDULE_WEDNESDAY_OPENTIME_OFFSET = 80;
	static final int PWOERSCHEDULE_WEDNESDAY_CLOSETIME_OFFSET = 248;
	static final int PWOERSCHEDULE_THURSDAY_OPENTIME_OFFSET = 104;
	static final int PWOERSCHEDULE_THURSDAY_CLOSETIME_OFFSET = 272;
	static final int PWOERSCHEDULE_FRIDAY_OPENTIME_OFFSET = 128;
	static final int PWOERSCHEDULE_FRIDAY_CLOSETIME_OFFSET = 296;
	static final int PWOERSCHEDULE_SATURDAY_OPENTIME_OFFSET = 152;
	static final int PWOERSCHEDULE_SATURDAY_CLOSETIME_OFFSET = 320;

	/*
	//数据流头部
	typedef struct ROOT{
		  WORD  id;              
		  WORD  color;           
		  DWORD size;            
		  DWORD count;           
		  long  survive;         
		  WORD  flag;            
		  WORD  Reserved;
		}TRoot, *PRoot;
	*/
	static final int ROOT_STRUCT_SIZE = 20; //2+2+4+4+4+2+2
	static final int ROOT_COLORTYPE_OFFSET = 2;
	static final int ROOT_SIZE_OFFSET = 4;
	static final int ROOT_COUNT_OFFSET = 8;
	static final int ROOT_SURVIVE_OFFSET = 12;
	static final int ROOT_FLAG_OFFSET = 16;
	
	/*
	//局部刷新操作数据结构声明
	typedef struct CUSTOMACTION{
		TRoot header;  //数据流头部
		WORD actionmode;       //操作模式(插入/替换)
		WORD chapterindex;     //在哪个章节插入或者替换
		WORD regionindex;      //在哪个区域窗插入或者替换
		WORD leafindex;        //在哪个页面插入或者替换
		WORD objectindex;      //在哪个位置插入或者替换控件
		WORD reserved;
	}TCustomAction, *PCustomAction;
	*/
	static final int ROOT_SUB_STRUCT_SIZE = ROOT_STRUCT_SIZE + 12; //2+2+4+4+4+2+2
	static final int ROOT_SUB_ACTION_MODE = 0;
	static final int ROOT_SUB_CHAPTER_INDEX = 2;
	static final int ROOT_SUB_REGION_INDEX = 4;
	static final int ROOT_SUB_LEAF_INDEX = 6;
	static final int ROOT_SUB_OBJECT_INDEX = 8;
	
	/*
	typedef struct PLAYSCHEDULE{
	  WORD  kind;           //播放计划类型0=始终播放；1=按每日起止时间播放；3=按指定的起止日期时间播放
	  WORD  week;           //星期日到六哪天有效
	  TTimeStamp fromtime;  //开始时间
	  TTimeStamp totime;    //结束时间
	}TPlaySchedule, *PPlaySchedule;
	*/
	static final int PLAY_SCHEDULE_SIZE = 20;
	static final int PLAY_SCHEDULE_KIND_OFFSET = 0;
	static final int PLAY_SCHEDULE_WEEK_OFFSET = 2;
	static final int PLAY_SCHEDULE_FROMTIME_OFFSET = 4;
	static final int PLAY_SCHEDULE_FROMDATE_OFFSET = 8;
	static final int PLAY_SCHEDULE_TOTIME_OFFSET = 12;
	static final int PLAY_SCHEDULE_TODATE_OFFSET = 16;
	/*
	//章节数据结构体
	typedef struct CHAPTER{
	  WORD  id;             //标识，用于数据校验
	  WORD  no;             //节目编号，置0
	  WORD  wait;           //是否等待播放完成(=1等待; =0到时间就切)
	  WORD  repeat;         //节目播放次数，预留未实现，置0
	  WORD  count;          //逻辑窗口数量 
	  BYTE  compress;       //是否压缩，预留未实现，置0
	  BYTE  priority;       //优先级，预留未实现，置0
	  DWORD time;           //节目显示时长，单位毫秒
	  DWORD size;           //标识该节目字节数(包含数据字节数和该元素本身字节数之和)
	  TPlaySchedule schedule; //播放计划 
	}TChapter, *PChapter;
	*/
	static final int CHAPTER_STRUCT_SIZE = 20 + PLAY_SCHEDULE_SIZE; //2+2+2+2+2+1+1+4+4+PLAY_SCHEDULE_SIZE
	static final int CHAPTER_SIZE_OFFSET = 16;
	static final int CHAPTER_COUNT_OFFSET = 8;
	static final int CHAPTER_WAIT_OFFSET = 4;
	static final int CHAPTER_PRIORITY_OFFSET = 11;
	static final int CHAPTER_TIME_OFFSET = 12;
	static final int CHAPTER_PLAY_SCHEDULE_KIND_OFFSET = 20 + PLAY_SCHEDULE_KIND_OFFSET;
	static final int CHAPTER_PLAY_SCHEDULE_WEEK_OFFSET = 20 + PLAY_SCHEDULE_WEEK_OFFSET;
	static final int CHAPTER_PLAY_SCHEDULE_FROMTIME_OFFSET = 20 + PLAY_SCHEDULE_FROMTIME_OFFSET;
	static final int CHAPTER_PLAY_SCHEDULE_FROMDATE_OFFSET = 20 + PLAY_SCHEDULE_FROMDATE_OFFSET;
	static final int CHAPTER_PLAY_SCHEDULE_TOTIME_OFFSET = 20 + PLAY_SCHEDULE_TOTIME_OFFSET;
	static final int CHAPTER_PLAY_SCHEDULE_TODATE_OFFSET = 20 + PLAY_SCHEDULE_TODATE_OFFSET;

	/*
	//区域窗数据结构体
	typedef struct REGION{
	  WORD  id;             //标识，用于数据校验
	  WORD  no;             //逻辑窗口编号，置0
	  WORD  over;           //节目是否播放完成(当mode为等待对象播放完成时，此字段有效。对计算机此字段无效)
	  WORD  count;          //页面数目
	  DWORD size;           //标识该逻辑窗口字节数(包含数据字节数和该元素本身字节数之和)
	  TRect rect;           //逻辑窗口位置大小
	  BYTE  border; 
	  BYTE  Reserved1;
	  WORD  Reserved2;
	}TRegion, *PRegion;
	*/
	static final int REGION_STRUCT_SIZE = 32; //2+2+2+2+4+16+1+1+2
	static final int REGION_SIZE_OFFSET = 8;
	static final int REGION_COUNT_OFFSET = 6;
	static final int REGION_LEFT_OFFSET = 12;
	static final int REGION_TOP_OFFSET = 16;
	static final int REGION_RIGHT_OFFSET = 20;
	static final int REGION_BOTTOM_OFFSET = 24;
	
	/*
	//页面数据结构体
	typedef struct LEAF{
	  WORD  id;             //页面编号(在页面替换时，根据此编号替换)
	  WORD  no;
	  WORD  wait;           //是否等待播放完成(=1等待; =0到时间就切)
	  WORD  serial;         //是否串行方式播放，置0
	  WORD  count;          //对象数目
	  WORD  Reserved;
	  DWORD time;           //页面显示时间
	  DWORD size;           //标识该页面字节数(包含数据字节数和该元素本身字节数之和)
	  WORD  Reserved1;
	  WORD  Reserved2;
	}TLeaf, *PLeaf;
	*/
	static final int LEAF_STRUCT_SIZE = 24; //2+2+2+2+2+2+4+4+2+2
	static final int LEAF_SIZE_OFFSET = 16;
	static final int LEAF_COUNT_OFFSET = 8;
	static final int LEAF_WAIT_OFFSET = 4;
	static final int LEAF_TIME_OFFSET = 12;
	
	/*
	//对象通用属性结构体
	typedef struct OBJECT{
	  WORD  id;             //对象标识
	  BYTE  transparent;    //是否透明
	  BYTE  flag;           //一些控制位 
	  DWORD size;           //对象字节数
	  TRect rect;           //对象显示区域
	}TObject, *PObject;
	*/
	static final int OBJECT_STRUCT_SIZE = 24;  //2+1+1+4+16
	static final int OBJECT_TRANSPARENT_OFFSET = 2;
	static final int OBJECT_SIZE_OFFSET = 4;
	static final int OBJECT_LEFT_OFFSET = 8;
	static final int OBJECT_TOP_OFFSET = 12;
	static final int OBJECT_RIGHT_OFFSET = 16;
	static final int OBJECT_BOTTOM_OFFSET = 20;
	
	/*
	//多播放对象数据结构，后面跟多个TWindow对象或者多个TString对象
	typedef struct WINDOWS{
	  TObject     object;     //对象的一般定义
	  BYTE        method;     //动作方式
	  BYTE        speed;      //动作速度
	  WORD        count;      //包含的的Window数量
	}TWindows, *PWindows;
	*/
	static final int STRINGS_STRUCT_SIZE = OBJECT_STRUCT_SIZE + 4;  //1+1+2
	static final int STRINGS_COUNT_OFFSET = OBJECT_STRUCT_SIZE + 2;
	static final int WINDOWS_STRUCT_SIZE = OBJECT_STRUCT_SIZE + 4;  //1+1+2
	static final int WINDOWS_COUNT_OFFSET = OBJECT_STRUCT_SIZE + 2;
	
	/*
	//内码文字对象
	typedef struct STRING{
	  WORD        id;
	  BYTE        inMethod;   //引入方式
	  BYTE        outMethod;  //引出方式
	  BYTE        stopMethod; //停留方式
	  BYTE        alignment;  //对齐方式 
	  WORD        inSpeed;    //引入速度
	  WORD        outSpeed;   //引出速度
	  WORD        stopSpeed;  //停留速度(例如为闪烁时，表示闪烁速度)
	  DWORD       stoptime;   //停留时间
	  long        stopx;      //停留位置
	  DWORD       size;       //数据长度
	  DWORD       color;      //字符颜色
	  WORD        fontset;    //字符集(一般情况 0=16点阵 1=24点阵)
	  WORD        Reserved2;
	}TString, *PString;
	*/
	static final int STRING_STRUCT_SIZE = 32;  //2+1+1+1+1+2+2+2+4+4+4+4+2+2
	static final int STRING_IN_METHOD_OFFSET = 2;
	static final int STRING_OUT_METHOD_OFFSET = 3;
	static final int STRING_STOP_METHOD_OFFSET = 4;
	static final int STRING_IN_SPEED_OFFSET = 6;
	static final int STRING_OUT_SPEED_OFFSET = 8;
	static final int STRING_STOP_SPEED_OFFSET = 10;
	static final int STRING_STOP_TIME_OFFSET = 12;
	static final int STRING_SIZE_OFFSET = 20;
	static final int STRING_COLOR_OFFSET = 24;
	static final int STRING_FONT_SET_OFFSET = 28;
	
	/*
	//内码日期时间对象
	typedef struct STRINGDCLOCK{
	  TObject     object;       //对象的一般定义
	  BYTE        type;         //类型：天文时间、作战时间
	  BYTE        direct;       //方向，=0正常，=1逆时针旋转90度，=2顺时针旋转90度 
	  WORD        step;         //步长 
	  TTimeStamp  basetime;     //基准时间(作战时间使用)
	  TTimeStamp  fromtime;     //开始时间(作战时间使用)
	  TTimeStamp  totime;       //结束时间(作战时间使用)
	  long        year_offset;  //年偏移量
	  long        month_offset; //月偏移量
	  long        day_offset;   //天偏移量
	  long        time_offset;  //时间偏移量(毫秒)
	  WORD        formats[MAX_DATETIME_FORMAT];  //显示格式[32]
	  DWORD       color;
	  BYTE        fontset;
	  BYTE        reserved;
	  WORD        reserved2;
	}TStringDClock, *PStringDClock;
	*/
	static final int SDCLOCK_STRUCT_SIZE = OBJECT_STRUCT_SIZE+116;  //2+1+1+1+1+2+2+2+4+4+4+4+2+2
	static final int SDCLOCK_FORMAT_OFFSET = 44;
	static final int SDCLOCK_COLOR_OFFSET = 108;
	static final int SDCLOCK_FONTSET_OFFSET = 112;

	/*
	//日期时间对象
	typedef struct DClock{
	  TObject     object;       //对象的一般定义
	  BYTE        type;         //类型：天文时间、作战时间
	  BYTE        direct;       //方向，=0正常，=1逆时针旋转90度，=2顺时针旋转90度 
	  WORD        step;         //步长 
	  TTimeStamp  basetime;     //基准时间(作战时间使用)
	  TTimeStamp  fromtime;     //开始时间(作战时间使用)
	  TTimeStamp  totime;       //结束时间(作战时间使用)
	  long        year_offset;  //年偏移量
	  long        month_offset; //月偏移量
	  long        day_offset;   //天偏移量
	  long        time_offset;  //时间偏移量(毫秒)
	  WORD        formats[MAX_DATETIME_FORMAT];  //显示格式
	}TDClock, *PDClock;
	*/
	static final int DCLOCK_STRUCT_SIZE = OBJECT_STRUCT_SIZE+108;  //2+1+1+1+1+2+2+2+4+4+4+4+2+2
	static final int DCLOCK_FORMAT_OFFSET = 44;

	/*
	//正计时、倒计时对象
	typedef struct COUNTER{
	  TObject    object;
	  TTimeStamp basetime;
	  WORD       type;
	  WORD       format;
	  WORD       Reserved1;
	  WORD       Reserved2;
	}TCounter, *PCounter;
	*/
	static final int COUNTER_STRUCT_SIZE = OBJECT_STRUCT_SIZE+24;  //2+1+1+1+1+2+2+2+4+4+4+4+2+2
	static final int COUNTER_BASETIME_OFFSET = 0;
	static final int COUNTER_TYPE_OFFSET = 8;
	static final int COUNTER_FORMAT_OFFSET = 10;

	/*
	//语音播放对象
	typedef struct VOICE{
	  TObject     object;     //对象的一般定义
	  DWORD       interval;
	  DWORD       times;
	  BYTE        voice_speaker;
	  BYTE        voice_volumn;
	  BYTE        voice_tone;
	  BYTE        voice_speed;
	  DWORD       reversed[4];
	}TVoice, *PVoice;
	*/
	static final int VOICE_STRUCT_SIZE = OBJECT_STRUCT_SIZE + 16;
	static final int VOICE_INTERVAL_OFFSET = OBJECT_STRUCT_SIZE;
	static final int VOICE_TIMES_OFFSET = OBJECT_STRUCT_SIZE + 4;
	static final int VOICE_SPEAKER_OFFSET = OBJECT_STRUCT_SIZE + 8;
	static final int VOICE_VOLUMN_OFFSET = OBJECT_STRUCT_SIZE + 9;
	static final int VOICE_TONE_OFFSET = OBJECT_STRUCT_SIZE + 10;
	static final int VOICE_SPEED_OFFSET = OBJECT_STRUCT_SIZE + 11;

	/*
	//图片对象，后面跟着一个点阵TPixels
	typedef struct WINDOW{
	  WORD        id;
	  BYTE        inMethod;   //引入方式
	  BYTE        outMethod;  //引出方式
	  BYTE        stopMethod; //停留方式
	  BYTE        flag;       //一些控制标志 
	  WORD        inSpeed;    //引入速度
	  WORD        outSpeed;   //引出速度
	  WORD        stopSpeed;  //停留速度(例如为闪烁时，表示闪烁速度)
	  DWORD       stoptime;   //停留时间
	  long        stopx;      //停留位置
	  DWORD       size;       //数据长度
	}TWindow, *PWindow;
	*/
	static final int WINDOW_STRUCT_SIZE = 24;  //2+1+1+1+1+2+2+2+4+4+4
	static final int WINDOW_IN_METHOD_OFFSET = 2;
	static final int WINDOW_OUT_METHOD_OFFSET = 3;
	static final int WINDOW_STOP_METHOD_OFFSET = 4;
	static final int WINDOW_IN_SPEED_OFFSET = 6;
	static final int WINDOW_OUT_SPEED_OFFSET = 8;
	static final int WINDOW_STOP_SPEED_OFFSET = 10;
	static final int WINDOW_STOP_TIME_OFFSET = 12;
	static final int WINDOW_SIZE_OFFSET = 20;

	/*
	//图片对象，后面跟着一个点阵TPixels
	typedef struct SCHEDULEWINDOW{
	  WORD        id;
	  BYTE        inMethod;   //引入方式
	  BYTE        outMethod;  //引出方式
	  BYTE        stopMethod; //停留方式
	  BYTE        flag;       //一些控制标志 
	  WORD        inSpeed;    //引入速度
	  WORD        outSpeed;   //引出速度
	  WORD        stopSpeed;  //停留速度(例如为闪烁时，表示闪烁速度)
	  DWORD       stoptime;   //停留时间
	  long        stopx;      //停留位置
	  DWORD       size;       //数据长度
	  TPlaySchedule schedule; //播放计划 
	}TScheduleWindow, *PScheduleWindow;
	*/
	static final int SCHEDULE_WINDOW_STRUCT_SIZE = WINDOW_STRUCT_SIZE + PLAY_SCHEDULE_SIZE;  //2+1+1+1+1+2+2+2+4+4+4+20

	/*
	//点阵集数据结构定义（点阵集包含多个点阵）
	typedef struct PIXELSET{
	  WORD   id;            //对象标识
	  WORD   count;         //TPixels对象数目
	  DWORD  size;          //对象字节数
	}TPixelSet, *PPixelSet;
	*/
	static final int PIXELSET_STRUCT_SIZE = 8;  //2+2+4
	static final int PIXELSET_COUNT_OFFSET = 2;
	static final int PIXELSET_SIZE_OFFSET = 4;

	/*
	//点阵数据结构定义
	typedef struct PIXELS{
	  WORD   id;            //对象标识
	  WORD   bits;          //每象素位数
	  long   width;         //点阵宽度
	  long   height;        //点阵高度
	  DWORD  size;          //对象大小
	}TPixels, *PPixels;
	*/
	static final int PIXELS_STRUCT_SIZE = 16;  //2+2+4+4+4
	static final int PIXELS_BITS_OFFSET = 2;
	static final int PIXELS_WIDTH_OFFSET = 4;
	static final int PIXELS_HEIGHT_OFFSET = 8;
	static final int PIXELS_SIZE_OFFSET = 12;

	/*
	typedef struct PKG_RESPOND{
		TPKG_Header Header;
		WORD Command;
		WORD Result;
	}TPKG_Respond, *PPKG_Respond;
	*/
	static final int RESPOND_STRUCT_SIZE = 12;
	static final int RESPOND_COMMAND_OFFSET = 0;
	static final int RESPOND_RESULT_OFFSET = 2;
	
	/*
	typedef struct BOARDPARAM{
		  WORD   width;
		  WORD   height;
		  WORD   type;
		  WORD   frequency;
		  DWORD  flag;
		  DWORD  uart;								//+2+2+2+2+4+4=16
		  BYTE   mac[ETHER_ADDRESS_LENGTH];
		  BYTE   ip[IP_ADDRESS_LENGTH];
		  BYTE   gateMAC[ETHER_ADDRESS_LENGTH];
		  BYTE   serverIP[IP_ADDRESS_LENGTH];		//+6+4+6+4=36
		  BYTE   brightness;
		  BYTE   importflag;
		  BYTE   rootcount;
		  BYTE   disconnect_sec;
		  DWORD  rom_size;
		  long   left;
		  long   top;								//+4+4+4+4=52
		  WORD   scan_mode;
		  WORD   server_port;
		  WORD   line_order;
		  WORD   oe_time;
		  WORD   shift_freq;
		  WORD   refresh_freq;						//+2+2+2+2+2+2=64;
		  BYTE   GateIP[IP_ADDRESS_LENGTH];
		  BYTE   ipMask[IP_ADDRESS_LENGTH];			//+4+4=72
		  BYTE   name[32];
		  DWORD  ident;
		  DWORD  address;							//+32+4+4=112
		  DWORD  pkp_rx_timeo;
		  DWORD  pkp_tx_timeo;
		  DWORD  pkp_tx_repeat;
		  BYTE   dnsIP[IP_ADDRESS_LENGTH];
		  BYTE   dnsMAC[ETHER_ADDRESS_LENGTH];
		  WORD   ReportTime;						//+12+4+6+2=136
		  //如果server_host=0，则server_domain有效
		  BYTE   serverDomain[32];  
		  //显示屏的密钥，用于屏蔽密钥不符的数据包，阻止非法用户向显示屏上发送信息
		  DWORD  key;	                            
		  DWORD  pkp_length;                        //+32+4+4=176
		  //对应的bin文件名
		  char   bin_name[32];                      //+32=208
		  //////////////////////////////////////////////////////
		  //光感相关参数 
		  BYTE   light_sense;
		  BYTE   light_sense_auto;
		  WORD   light_sense_reserved;
		  WORD   light_sense_split_val[8];
		  BYTE   light_sense_bright_val[8];         //+28=236
		  //////////////////////////////////////////////////////
		  //双卡时钟同步受控端IP地址
		  BYTE   TimeSyncIP[IP_ADDRESS_LENGTH];		//+4=240
		  //////////////////////////////////////////////////////
		  //自动重启参数
		  BYTE   auto_reset_enabled;
		  BYTE   auto_reset_type;
		  BYTE   auto_reset_hour;
		  BYTE   auto_reset_minute;
		}TBoardParam, *PBoardParam;
	*/
	static final int BOARDPARAM_STRUCT_SIZE = 512;
    static final int BOARDPARAM_WIDTH_OFFSET = 0;
    static final int BOARDPARAM_HEIGHT_OFFSET = 2;
    static final int BOARDPARAM_COLORMODE_OFFSET = 4;
    static final int BOARDPARAM_IP_OFFSET = 22;

	static int root_seek = -1;
	static int root_size = 0;
	static int root_count = 0;
	static int chapter_seek = -1;
	static int chapter_size = 0;
	static int chapter_count = 0;
	static int region_seek = -1;
	static int region_size = 0;
	static int region_count = 0;
	static int leaf_seek = -1;
	static int leaf_size = 0;
	static int leaf_count = 0;
	static int strings_seek = -1;
	static int strings_size = 0;
	static int strings_count = 0;
	static int windows_seek = -1;
	static int windows_size = 0;
	static int windows_count = 0;
	static int data_seek = 0;

	public static byte[] data_stream;
	public static byte[] cmd_stream;
	public static byte[] respond_stream;
	public static byte[] packet_stream;
	public static int packet_size;
	
	public static int get_boardparam_respond_size = 0;
	
	public static final int RGB_SPLIT = 64;
	
	public int fix_serialno=0;

	static private int current_color_type=0;

	public void print_stream(byte[] stream, int size){
		int i;
		short x;
		String s;
	    System.out.print("size="+size+"; "+"data=");
		for (i=0; i<size; i++){
			if (stream[i]<0) x=(short)(256+stream[i]); else x=stream[i];
			s=Integer.toHexString(x);
			if (s.length()==1) s="0x0"+s; else s="0x"+s;
	        System.out.print(s);    
	        System.out.print(' ');    
		}
	    System.out.println();    
	}

	public void print_stream_ex(byte[] stream, int start, int size){
		int i;
		short x;
	    System.out.print("size="+size+"; "+"data=");
		for (i=start; i<start+size; i++){
			if (stream[i]<0) x=(short)(256+stream[i]); else x=stream[i];
	        System.out.print(x);    
	        System.out.print(' ');    
		}
	    System.out.println();    
	}

	static byte lo_of_short(short value){
		return (byte)(value&0xFF);
	}

	static byte hi_of_short(short value){
		return (byte)((value>>8)&0xFF);
	}

	static short lo_of_int(int value){
		return (short)(value&0xFFFF);
	}
	
	static short hi_of_int(int value){
		return (short)((value>>16)&0xFFFF);
	}
	
	static void byteFill(byte[] buffer, int index, byte value){
		buffer[index]=value;
	}
	
	static int asByte(byte[] buffer, int index){
		return buffer[index];
	}

	static void shortFill(byte[] buffer, int index, short value){
		buffer[index]=lo_of_short(value);
		buffer[index+1]=hi_of_short(value);
	}

	static int asShort(byte[] buffer, int index){
		int value;
		short result=0;
		if (buffer[index]>=0) value=buffer[index]; else value=(256+buffer[index]);
		result+=value;
		if (buffer[index+1]>=0) value=buffer[index+1]; else value=(256+buffer[index+1]);
		result+=(value<<8);
		return (short)result;
	}

	static String asIP(byte[] buffer, int index){
		int[] ip=new int[4];
		int i;
		for(i=0; i<4; i++){
			if (buffer[index+i]>=0) ip[i]=buffer[index+i]; else ip[i]=(256+buffer[index+i]);
		}
		return ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3];
	}

	static String asMAC(byte[] buffer, int index){
		int[] mac=new int[6];
		int i;
		for(i=0; i<6; i++){
			if (buffer[index+i]>=0) mac[i]=buffer[index+i]; else mac[i]=(256+buffer[index+i]);
		}
		return mac[0]+"-"+mac[1]+"-"+mac[2]+"-"+mac[3]+"-"+mac[4]+"-"+mac[5];
	}

	static void intFill(byte[] buffer, int index, int value){
		buffer[index]=lo_of_short(lo_of_int(value));
		buffer[index+1]=hi_of_short(lo_of_int(value));
		buffer[index+2]=lo_of_short(hi_of_int(value));
		buffer[index+3]=hi_of_short(hi_of_int(value));
	}
	
	static void timeFill(byte[] buffer, int index, int hour, int minute, int second){
		int time;
		time=((hour*60+minute)*60+second)*1000;
		intFill(buffer, index, time);
	}

	static void dateFill(byte[] buffer, int index, int year, int month, int day){
		int date;
		int MonthDays[][]={{31,28,31,30,31,30,31,31,30,31,30,31}, {31,29,31,30,31,30,31,31,30,31,30,31}};
		int i,y,m,d;

		y=year-1;
		for (m=0,d=year; d>100; d-=100, m++) ;
		if ((year&3)==0 && ((m&3)==0 || d!=0)) d=1; else d=0;
		  
		date=day;
		for (i=1; i<=month-1; i++) date+=MonthDays[d][i-1];
		date+=y*365+(y>>2)-(y/100)+(y/400);
		  
		intFill(buffer, index, date);
	}

	static void timestampFill(byte[] buffer, int index, int year, int month, int day, int hour, int minute, int second){
		timeFill(buffer, index, hour, minute, second);
		dateFill(buffer, index+4, year, month, day);
	}

	static int asInt(byte[] buffer, int index){
		int value;
		int result=0;
		if (buffer[index]>=0) value=buffer[index]; else value=(256+buffer[index]);
		result+=value;
		if (buffer[index+1]>=0) value=buffer[index+1]; else value=(256+buffer[index+1]);
		result+=(value<<8);
		if (buffer[index+2]>=0) value=buffer[index+2]; else value=(256+buffer[index+2]);
		result+=(value<<16);
		if (buffer[index+3]>=0) value=buffer[index+3]; else value=(256+buffer[index+3]);
		result+=(value<<24);
		return result;
	}

	static void blockFill(byte[] buffer, int index, int count, byte value){
		int i;
		for (i=index; i<index+count; i++){
			buffer[i]=value;
		}
	}

	public void blockCopy(byte[] obuffer, int ostart, byte[] ibuffer, int istart, int count){
		int x, y, i;
		x=ostart;
		y=istart;
		for (i=0; i<count; i++){
			obuffer[x++]=ibuffer[y++];
		}
	}

	public void stringCopy(byte[] obuffer, int ostart, String str, int len){
		int x, i;
		x=ostart;
		for (i=0; i<len; i++){
			obuffer[x++]=str.getBytes()[i];
		}
	}
	
	static short CRCTableHi[]={
			0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40};

	static short CRCTableLo[]={
			0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06, 0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04,
            0xCC, 0x0C, 0x0D, 0xCD, 0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09, 0x08, 0xC8,
            0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A, 0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC,
            0x14, 0xD4, 0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3, 0x11, 0xD1, 0xD0, 0x10,
            0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3, 0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4,
            0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A, 0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38,
            0x28, 0xE8, 0xE9, 0x29, 0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED, 0xEC, 0x2C,
            0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26, 0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0,
            0xA0, 0x60, 0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67, 0xA5, 0x65, 0x64, 0xA4,
            0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F, 0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68,
            0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E, 0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C,
            0xB4, 0x74, 0x75, 0xB5, 0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71, 0x70, 0xB0,
            0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92, 0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54,
            0x9C, 0x5C, 0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B, 0x99, 0x59, 0x58, 0x98,
            0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B, 0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
            0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42, 0x43, 0x83, 0x41, 0x81, 0x80, 0x40};

	static int CreateCRC(byte[] buffer, int size){
		short CRCHi=0xFF;
		short CRCLo=0xFF;
		int r;
		int i;
		
		for (i=0; i<size; i++)
		{
			r=(buffer[i]^CRCHi)&0xFF;
		    CRCHi=(short)(CRCLo^CRCTableHi[r]);
		    CRCLo=CRCTableLo[r];
		}
		return ((CRCHi&0xFF)<<8)+(CRCLo&0xFF);
	}

	static int Pack(byte[] opacket, byte[] ipacket, int isize){
		int x, y;
		int crc;
		x=0;
		y=0;
		crc=CreateCRC(ipacket, isize);
		ipacket[isize++]=(byte)(crc&0xFF);
		ipacket[isize++]=(byte)((crc>>8)&0xFF);
		opacket[y++]=(byte)PKP_PREFIX;
		for (x=0; x<isize; x++){
			switch(ipacket[x]){
			case (byte)PKP_PREFIX:
				opacket[y++]=(byte)PKP_FILLCH;
				opacket[y++]=(byte)(PKP_PREFIX+1);
				break;
			case (byte)PKP_SUFFIX:
				opacket[y++]=(byte)PKP_FILLCH;
				opacket[y++]=(byte)(PKP_SUFFIX+1);
				break;
			case (byte)PKP_FILLCH:
				opacket[y++]=(byte)PKP_FILLCH;
				opacket[y++]=(byte)(PKP_FILLCH+1);
				break;
			default:
				opacket[y++]=ipacket[x];
				break;
			}
		}
		opacket[y++]=(byte)PKP_SUFFIX;
		return y;
	}

	static int Depack(byte[] opacket, byte[] ipacket, int isize){
		int x, y;
		x=1;
		y=0;
		while (x<isize-1){
			switch(ipacket[x]){
			case (byte)PKP_FILLCH:
				opacket[y++]=(byte)(ipacket[x+1]-1);
				x+=2;
				break;
			default:
				opacket[y++]=ipacket[x];
				x++;
				break;
			}
		}
		return y-2;
	}

	//初始化节目数据
	//roottype: 节目类型  ROOT_PLAY为RAM节目，掉电丢失；ROOT_DOWNLOAD为FLASH节目，掉电不丢失。
	//colortype: 基色类型 COLOR_TYPE_DOUBLE为双基色，COLOR_TYPE_THREE为无灰度全彩
	public void MakeRoot(int roottype, int colortype) {
		root_seek=0;
		root_count=0;
		root_size=ROOT_STRUCT_SIZE;
		current_color_type=colortype;
		blockFill(data_stream, root_seek, root_size, (byte)0);
		shortFill(data_stream, root_seek, (short)roottype);
		shortFill(data_stream, root_seek+ROOT_COLORTYPE_OFFSET, (short)colortype);
		intFill(data_stream, root_seek+ROOT_SURVIVE_OFFSET, -1);
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		data_seek=root_seek+root_size;
		//System.out.println("root="+data_seek);
	}

	//初始化播放数据
	//roottype: 节目类型  ROOT_PLAY为RAM节目，掉电丢失；ROOT_DOWNLOAD为FLASH节目，掉电不丢失。
	//colortype: 基色类型 COLOR_TYPE_DOUBLE为双基色，COLOR_TYPE_THREE为无灰度全彩
	//survive: 节目生命时长（秒），roottype为ROOT_PLAY时有效。当发送的节目播放达到这个时长后，显示内置节目。
	public void MakeRootEx(int roottype, int colortype, int survive) {
		root_seek=0;
		root_count=0;
		root_size=ROOT_STRUCT_SIZE;
		blockFill(data_stream, root_seek, root_size, (byte)0);
		shortFill(data_stream, root_seek, (short)roottype);
		shortFill(data_stream, root_seek+ROOT_COLORTYPE_OFFSET, (short)colortype);
		intFill(data_stream, root_seek+ROOT_SURVIVE_OFFSET, survive);
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		data_seek=root_seek+root_size;
		//System.out.println("root="+data_seek);
	}

	//初始化局部刷新（只更新一个节目）节目数据
	//roottype: 节目类型  ROOT_PLAY_CHAPTER
	//colortype: 基色类型 COLOR_TYPE_DOUBLE为双基色，COLOR_TYPE_THREE为无灰度全彩
	//mode: 刷新模式，0插入，1替换，目前只支持1替换方式
	//chapter_index: 替换节目编号
	public void MakeChapter(int roottype, int colortype, int mode, int chapter_index, 
			int wait, int time) {
		root_seek=0;
		chapter_seek=0;
		region_seek=0;
		leaf_seek=0;

		root_count=0;
		root_size=ROOT_SUB_STRUCT_SIZE;
		current_color_type=colortype;
		blockFill(data_stream, root_seek, root_size, (byte)0);
		shortFill(data_stream, root_seek, (short)roottype);
		shortFill(data_stream, root_seek+ROOT_COLORTYPE_OFFSET, (short)colortype);
		intFill(data_stream, root_seek+ROOT_SURVIVE_OFFSET, -1);
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_ACTION_MODE, (short)mode);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_CHAPTER_INDEX, (short)chapter_index);
		
		data_seek=root_seek+root_size;

		chapter_seek=data_seek;
		chapter_count=0;
		chapter_size=CHAPTER_STRUCT_SIZE;
		blockFill(data_stream, chapter_seek, chapter_size, (byte)0);
		shortFill(data_stream, chapter_seek, (short)OBJECT_CHAPTER);
		shortFill(data_stream, chapter_seek+CHAPTER_WAIT_OFFSET, (short)wait);
		intFill(data_stream, chapter_seek+CHAPTER_TIME_OFFSET, time);
		intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		shortFill(data_stream, chapter_seek+CHAPTER_COUNT_OFFSET, (short)chapter_count);
		root_size+=chapter_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=chapter_seek+chapter_size;
	}

	public void MakeChapterEx(int roottype, int colortype, int mode, int chapter_index,
			int wait, int time, int priority, int kind, int week, 
			int from_year, int from_month, int from_day, int from_hour, int from_minute, int from_second, 
			int to_year, int to_month, int to_day, int to_hour, int to_minute, int to_second) {
		root_seek=0;
		chapter_seek=0;
		region_seek=0;
		leaf_seek=0;

		root_count=0;
		root_size=ROOT_SUB_STRUCT_SIZE;
		current_color_type=colortype;
		blockFill(data_stream, root_seek, root_size, (byte)0);
		shortFill(data_stream, root_seek, (short)roottype);
		shortFill(data_stream, root_seek+ROOT_COLORTYPE_OFFSET, (short)colortype);
		intFill(data_stream, root_seek+ROOT_SURVIVE_OFFSET, -1);
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_ACTION_MODE, (short)mode);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_CHAPTER_INDEX, (short)chapter_index);
		
		data_seek=root_seek+root_size;

		chapter_seek=data_seek;
		chapter_count=0;
		chapter_size=CHAPTER_STRUCT_SIZE;
		blockFill(data_stream, chapter_seek, chapter_size, (byte)0);
		shortFill(data_stream, chapter_seek, (short)OBJECT_CHAPTER);
		
		shortFill(data_stream, chapter_seek+CHAPTER_WAIT_OFFSET, (short)wait);
		byteFill(data_stream, chapter_seek+CHAPTER_PRIORITY_OFFSET, (byte)priority);
		intFill(data_stream, chapter_seek+CHAPTER_TIME_OFFSET, time);
		intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		shortFill(data_stream, chapter_seek+CHAPTER_COUNT_OFFSET, (short)chapter_count);
		shortFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_KIND_OFFSET, (short)kind);
		shortFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_WEEK_OFFSET, (short)week);
		intFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_FROMTIME_OFFSET, SystemTimeToTime(from_hour, from_minute, from_second));
		intFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_FROMDATE_OFFSET, SystemTimeToDate(from_year, from_month, from_day));
		intFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_TOTIME_OFFSET, SystemTimeToTime(to_hour, to_minute, to_second));
		intFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_TODATE_OFFSET, SystemTimeToDate(to_year, to_month, to_day));
		
		root_size+=chapter_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=chapter_seek+chapter_size;
	}

	//初始化局部刷新（只更新一个分区）节目数据
	//roottype: 节目类型  ROOT_PLAY_REGION
	//colortype: 基色类型 COLOR_TYPE_DOUBLE为双基色，COLOR_TYPE_THREE为无灰度全彩
	//mode: 刷新模式，0插入，1替换，目前只支持1替换方式
	//chapter_index: 替换节目编号
	//region_index: 替换分区编号
	public void MakeRegion(int roottype, int colortype, int mode, int chapter_index, int region_index, 
			int left, int top, int width, int height) {
		root_seek=0;
		chapter_seek=0;
		region_seek=0;
		leaf_seek=0;

		root_count=0;
		root_size=ROOT_SUB_STRUCT_SIZE;
		current_color_type=colortype;
		blockFill(data_stream, root_seek, root_size, (byte)0);
		shortFill(data_stream, root_seek, (short)roottype);
		shortFill(data_stream, root_seek+ROOT_COLORTYPE_OFFSET, (short)colortype);
		intFill(data_stream, root_seek+ROOT_SURVIVE_OFFSET, -1);
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_ACTION_MODE, (short)mode);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_CHAPTER_INDEX, (short)chapter_index);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_REGION_INDEX, (short)region_index);
		
		data_seek=root_seek+root_size;

		region_seek=data_seek;
		region_count=0;
		region_size=REGION_STRUCT_SIZE;
		blockFill(data_stream, region_seek, region_size, (byte)0);
		shortFill(data_stream, region_seek, (short)OBJECT_REGION);
		intFill(data_stream, region_seek+REGION_LEFT_OFFSET, left);
		intFill(data_stream, region_seek+REGION_TOP_OFFSET, top);
		intFill(data_stream, region_seek+REGION_RIGHT_OFFSET, left+width);
		intFill(data_stream, region_seek+REGION_BOTTOM_OFFSET, top+height);
		intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		shortFill(data_stream, region_seek+REGION_COUNT_OFFSET, (short)region_count);
		root_size+=region_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=region_seek+region_size;
	}

	//初始化局部刷新（只更新一个页面）节目数据
	//roottype: 节目类型  ROOT_PLAY_LEAF
	//colortype: 基色类型 COLOR_TYPE_DOUBLE为双基色，COLOR_TYPE_THREE为无灰度全彩
	//mode: 刷新模式，0插入，1替换，目前只支持1替换方式
	//chapter_index: 替换节目编号
	//region_index: 替换分区编号
	//leaf_index: 替换页面编号
	public void MakeLeaf(int roottype, int colortype, int mode, int chapter_index, int region_index, int leaf_index, 
			int wait, int time) {
		root_seek=0;
		chapter_seek=0;
		region_seek=0;
		leaf_seek=0;

		root_count=0;
		root_size=ROOT_SUB_STRUCT_SIZE;
		current_color_type=colortype;
		blockFill(data_stream, root_seek, root_size, (byte)0);
		shortFill(data_stream, root_seek, (short)roottype);
		shortFill(data_stream, root_seek+ROOT_COLORTYPE_OFFSET, (short)colortype);
		intFill(data_stream, root_seek+ROOT_SURVIVE_OFFSET, -1);
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_ACTION_MODE, (short)mode);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_CHAPTER_INDEX, (short)chapter_index);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_REGION_INDEX, (short)region_index);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_LEAF_INDEX, (short)leaf_index);
		
		data_seek=root_seek+root_size;

		leaf_seek=data_seek;
		leaf_count=0;
		leaf_size=LEAF_STRUCT_SIZE;
		blockFill(data_stream, leaf_seek, leaf_size, (byte)0);
		shortFill(data_stream, leaf_seek, (short)OBJECT_LEAF);
		shortFill(data_stream, leaf_seek+LEAF_WAIT_OFFSET, (short)wait);
		intFill(data_stream, leaf_seek+LEAF_TIME_OFFSET, time);
		intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		shortFill(data_stream, leaf_seek+LEAF_COUNT_OFFSET, (short)leaf_count);
		root_size+=leaf_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=leaf_seek+leaf_size;
		
	}

	//初始化局部刷新（只更新一个对象）节目数据
	//roottype: 节目类型  ROOT_PLAY_LEAF
	//colortype: 基色类型 COLOR_TYPE_DOUBLE为双基色，COLOR_TYPE_THREE为无灰度全彩
	//mode: 刷新模式，0插入，1替换，目前只支持1替换方式
	//chapter_index: 替换节目编号
	//region_index: 替换分区编号
	//leaf_index: 替换页面编号
	//object_index: 替换对象编号
	public void MakeObject(int roottype, int colortype, int mode, int chapter_index, int region_index, int leaf_index, int object_index) {
		root_seek=0;
		chapter_seek=0;
		region_seek=0;
		leaf_seek=0;

		root_count=0;
		root_size=ROOT_SUB_STRUCT_SIZE;
		current_color_type=colortype;
		blockFill(data_stream, root_seek, root_size, (byte)0);
		shortFill(data_stream, root_seek, (short)roottype);
		shortFill(data_stream, root_seek+ROOT_COLORTYPE_OFFSET, (short)colortype);
		intFill(data_stream, root_seek+ROOT_SURVIVE_OFFSET, -1);
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_ACTION_MODE, (short)mode);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_CHAPTER_INDEX, (short)chapter_index);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_REGION_INDEX, (short)region_index);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_LEAF_INDEX, (short)leaf_index);
		shortFill(data_stream, root_seek+ROOT_STRUCT_SIZE+ROOT_SUB_OBJECT_INDEX, (short)object_index);
		
		data_seek=root_seek+root_size;
	}

	//添加节目
	//wait 1=等待节目内部内容播放完成  0=按照节目时间长度播放
	//time 节目播放时长，单位毫秒
	public void AddChapter(int wait, int time) {
		chapter_seek=data_seek;
		chapter_count=0;
		chapter_size=CHAPTER_STRUCT_SIZE;
		blockFill(data_stream, chapter_seek, chapter_size, (byte)0);
		shortFill(data_stream, chapter_seek, (short)OBJECT_CHAPTER);
		shortFill(data_stream, chapter_seek+CHAPTER_WAIT_OFFSET, (short)wait);
		intFill(data_stream, chapter_seek+CHAPTER_TIME_OFFSET, time);
		intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		shortFill(data_stream, chapter_seek+CHAPTER_COUNT_OFFSET, (short)chapter_count);
		root_count++;
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		root_size+=chapter_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=chapter_seek+chapter_size;
		//System.out.println("chapter="+data_seek);
	}

	int SystemTimeToDate(int year, int month, int day)
	{
	  long MonthDays[][]={{31,28,31,30,31,30,31,31,30,31,30,31},{31,29,31,30,31,30,31,31,30,31,30,31}};
	  int i, y, m, d;
	  int date;

	  y=year-1;
	  for (m=0,d=year; d>100; d-=100, m++) ;

	  if (((year & 3)==0) && ((m & 3)==0 || (d!=0))) d=1;
	  else d=0;
	  
	  date=day;
	  for (i=1; i<=month-1; i++) date+=MonthDays[d][i-1];
	  date+=y*365+(y>>2)-(y/100)+(y/400);
	
	  return date;
	}

	int SystemTimeToTime(int hour, int minute, int second)
	{
	  return hour*60*60000+minute*60000+second*1000;
	}

	//添加节目
	//wait 1=等待节目内部内容播放完成  0=按照节目时间长度播放
	//time 节目播放时长，单位毫秒
	//priority 优先级，显示屏上只播放优先级最大的节目，如果有多个节目有最大优先级的值，则循环切换播放
	//kind 计划模式，=0始终播放，=1按一周每日播放，=2按起止时间播放，=3不播放
	//week 一周定义，当kind=1时有效，从bit0-bit6表示周日到周六
	//              周日0x1     周一0x2    周二0x4     周三0x8     周四0x10     周五0x20     周六0x30
	//              这个值可以相加组合，例如只播放周一、周二、周三这3天，则week赋值0x2+0x4+0x8即可
	public void AddChapterEx(int wait, int time, int priority, int kind, int week, 
			int from_year, int from_month, int from_day, int from_hour, int from_minute, int from_second, 
			int to_year, int to_month, int to_day, int to_hour, int to_minute, int to_second) {
		chapter_seek=data_seek;
		chapter_count=0;
		chapter_size=CHAPTER_STRUCT_SIZE;
		blockFill(data_stream, chapter_seek, chapter_size, (byte)0);
		shortFill(data_stream, chapter_seek, (short)OBJECT_CHAPTER);
		shortFill(data_stream, chapter_seek+CHAPTER_WAIT_OFFSET, (short)wait);
		byteFill(data_stream, chapter_seek+CHAPTER_PRIORITY_OFFSET, (byte)priority);
		intFill(data_stream, chapter_seek+CHAPTER_TIME_OFFSET, time);
		intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		shortFill(data_stream, chapter_seek+CHAPTER_COUNT_OFFSET, (short)chapter_count);
		shortFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_KIND_OFFSET, (short)kind);
		shortFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_WEEK_OFFSET, (short)week);
		intFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_FROMTIME_OFFSET, SystemTimeToTime(from_hour, from_minute, from_second));
		intFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_FROMDATE_OFFSET, SystemTimeToDate(from_year, from_month, from_day));
		intFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_TOTIME_OFFSET, SystemTimeToTime(to_hour, to_minute, to_second));
		intFill(data_stream, chapter_seek+CHAPTER_PLAY_SCHEDULE_TODATE_OFFSET, SystemTimeToDate(to_year, to_month, to_day));
		root_count++;
		intFill(data_stream, root_seek+ROOT_COUNT_OFFSET, root_count);
		root_size+=chapter_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=chapter_seek+chapter_size;
		//System.out.println("chapter="+data_seek);
	}

	//添加分区
	//left,top,width,height: 左上宽高
	public void AddRegion(int left, int top, int width, int height) {
		region_seek=data_seek;
		region_count=0;
		region_size=REGION_STRUCT_SIZE;
		blockFill(data_stream, region_seek, region_size, (byte)0);
		shortFill(data_stream, region_seek, (short)OBJECT_REGION);
		intFill(data_stream, region_seek+REGION_LEFT_OFFSET, left);
		intFill(data_stream, region_seek+REGION_TOP_OFFSET, top);
		intFill(data_stream, region_seek+REGION_RIGHT_OFFSET, left+width);
		intFill(data_stream, region_seek+REGION_BOTTOM_OFFSET, top+height);
		intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		shortFill(data_stream, region_seek+REGION_COUNT_OFFSET, (short)region_count);
		if (chapter_seek>0) {
			chapter_count++;
			shortFill(data_stream, chapter_seek+CHAPTER_COUNT_OFFSET, (short)chapter_count);
			chapter_size+=region_size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=region_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=region_seek+region_size;
	}

	//添加页面
	//wait 1=等待页面内部内容播放完成  0=按照页面时间长度播放
	//time 页面播放时长，单位毫秒
	public void AddLeaf(int wait, int time) {
		leaf_seek=data_seek;
		leaf_count=0;
		leaf_size=LEAF_STRUCT_SIZE;
		blockFill(data_stream, leaf_seek, leaf_size, (byte)0);
		shortFill(data_stream, leaf_seek, (short)OBJECT_LEAF);
		shortFill(data_stream, leaf_seek+LEAF_WAIT_OFFSET, (short)wait);
		intFill(data_stream, leaf_seek+LEAF_TIME_OFFSET, time);
		intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		shortFill(data_stream, leaf_seek+LEAF_COUNT_OFFSET, (short)leaf_count);
		if (region_seek>0) {
			region_count++;
			shortFill(data_stream, region_seek+REGION_COUNT_OFFSET, (short)region_count);
			region_size+=leaf_size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=leaf_size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=leaf_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=leaf_seek+leaf_size;
	}

	public void AddStrings(int left, int top, int width, int height) {
		strings_seek=data_seek;
		strings_count=0;
		strings_size=STRINGS_STRUCT_SIZE;
		blockFill(data_stream, strings_seek, strings_size, (byte)0);
		shortFill(data_stream, strings_seek, (short)OBJECT_STRINGS);
		blockFill(data_stream, strings_seek+OBJECT_TRANSPARENT_OFFSET, 1, (byte)1);
		intFill(data_stream, strings_seek+OBJECT_LEFT_OFFSET, left);
		intFill(data_stream, strings_seek+OBJECT_TOP_OFFSET, top);
		intFill(data_stream, strings_seek+OBJECT_RIGHT_OFFSET, left+width);
		intFill(data_stream, strings_seek+OBJECT_BOTTOM_OFFSET, top+height);
		shortFill(data_stream, strings_seek+STRINGS_COUNT_OFFSET, (short)strings_count);
		intFill(data_stream, strings_seek+OBJECT_SIZE_OFFSET, strings_size);
		if (leaf_seek>0) {
			leaf_count++;
			shortFill(data_stream, leaf_seek+LEAF_COUNT_OFFSET, (short)leaf_count);
			leaf_size+=strings_size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=strings_size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=strings_size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=strings_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=strings_seek+strings_size;
		//System.out.println("strings="+data_seek);
	}

	//inmethod 引入方式，在此java文件最上方有定义
	//outmethod 引出方式，在此java文件最上方有定义
	//inspeed, outspeed 引入引出速度，0到5
	//stopmethod 停留方式，请置0
	//stopspeed 停留速度，请置0
	//color 0xFF=红，0xFF00=绿，0xFFFF=黄
	//fontset 0=16点阵，1=24点阵
	public void AddChildString(String text, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime, int color, int fontset) {
		int seek, size;
		int len=0;
		byte[] gb2312;
		try {
			byte[] s=text.getBytes("GB2312");
			print_stream(s, s.length);
			len=s.length+1;
			if ((len&0x3)>0) len=((len+0x3)&(~0x3));
			gb2312=new byte[len];
			blockFill(gb2312, 0, len, (byte)0);
			blockCopy(gb2312, 0, s, 0, s.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gb2312=new byte[0];
		}

		seek=data_seek;
		size=STRING_STRUCT_SIZE+len;
		size=((size+0x3)&(~0x3));
		blockFill(data_stream, seek, size, (byte)0);
		shortFill(data_stream, seek, (short)OBJECT_STRING);
		byteFill(data_stream, seek+STRING_IN_METHOD_OFFSET, (byte)inmethod);
		shortFill(data_stream, seek+STRING_IN_SPEED_OFFSET, (short)inspeed);
		byteFill(data_stream, seek+STRING_OUT_METHOD_OFFSET, (byte)outmethod);
		shortFill(data_stream, seek+STRING_OUT_SPEED_OFFSET, (short)outspeed);
		byteFill(data_stream, seek+STRING_STOP_METHOD_OFFSET, (byte)stopmethod);
		shortFill(data_stream, seek+STRING_STOP_SPEED_OFFSET, (short)stopspeed);
		intFill(data_stream, seek+STRING_STOP_TIME_OFFSET, stoptime);
		intFill(data_stream, seek+STRING_COLOR_OFFSET, color);
		shortFill(data_stream, seek+STRING_FONT_SET_OFFSET, (short)fontset);
		intFill(data_stream, seek+STRING_SIZE_OFFSET, size);
		//stringCopy(data_stream, seek+STRING_STRUCT_SIZE, text, len);
		blockCopy(data_stream, seek+STRING_STRUCT_SIZE, gb2312, 0, len);
		strings_count++;
		shortFill(data_stream, strings_seek+STRINGS_COUNT_OFFSET, (short)strings_count);
		strings_size+=size;
		intFill(data_stream, strings_seek+OBJECT_SIZE_OFFSET, strings_size);
		if (leaf_seek>0) {
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("childstring="+data_seek);
	}
	
	public void AddWindows(int left, int top, int width, int height) {
		windows_seek=data_seek;
		windows_count=0;
		windows_size=WINDOWS_STRUCT_SIZE;
		blockFill(data_stream, windows_seek, windows_size, (byte)0);
		shortFill(data_stream, windows_seek, (short)OBJECT_WINDOWS);
		blockFill(data_stream, windows_seek+OBJECT_TRANSPARENT_OFFSET, 1, (byte)1);
		intFill(data_stream, windows_seek+OBJECT_LEFT_OFFSET, left);
		intFill(data_stream, windows_seek+OBJECT_TOP_OFFSET, top);
		intFill(data_stream, windows_seek+OBJECT_RIGHT_OFFSET, left+width);
		intFill(data_stream, windows_seek+OBJECT_BOTTOM_OFFSET, top+height);
		shortFill(data_stream, windows_seek+WINDOWS_COUNT_OFFSET, (short)windows_count);
		intFill(data_stream, windows_seek+OBJECT_SIZE_OFFSET, windows_size);
		if (leaf_seek>0) {
			leaf_count++;
			shortFill(data_stream, leaf_seek+LEAF_COUNT_OFFSET, (short)leaf_count);
			leaf_size+=windows_size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=windows_size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=windows_size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=windows_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=windows_seek+windows_size;
		//System.out.println("windows="+data_seek);
	}

	//dib_buffer 点阵图片RGB数组
	//dib_buffer_size 数组长度
	//dib_width 点阵宽度
	//dib_height 点阵高度
	static int AddPixelsRG(int[] dib_buffer, int dib_buffer_size, int dib_width, int dib_height) {
		int pixels_seek, pixels_size, pixels_height;
		int x, y;
		int shift[] = new int[4];
		int shift_value[] = new int[4];
		int shift_mask[] = new int[4];
		int bits_value[] = new int[4];
		int bits_seek, bits_size;
		int buffer_seek=0;
		int byte_seek=3;

		pixels_height=((dib_height+0xF)&(~0xF));
		bits_size=(pixels_height>>2)*dib_width;
		pixels_seek=data_seek;
		pixels_size=PIXELS_STRUCT_SIZE+bits_size;
		blockFill(data_stream, pixels_seek, pixels_size, (byte)0);

		shortFill(data_stream, pixels_seek, (short)OBJECT_PIXELS);
		shortFill(data_stream, pixels_seek+PIXELS_BITS_OFFSET, (short)BITMAP_YELLOW);
		intFill(data_stream, pixels_seek+PIXELS_WIDTH_OFFSET, (int)dib_width);
		intFill(data_stream, pixels_seek+PIXELS_HEIGHT_OFFSET, (int)pixels_height);
		intFill(data_stream, pixels_seek+PIXELS_SIZE_OFFSET, (int)pixels_size);
		
		bits_seek=pixels_seek+PIXELS_STRUCT_SIZE;
		for (x=0; x<dib_width; x++){
			shift[0]=0x0; shift[1]=0x0; shift[2]=0x0; shift[3]=0x80;
			shift_mask[0]=0x0; shift_mask[1]=0x0; shift_mask[2]=0x0; shift_mask[3]=0xC0;
			bits_value[0]=0x0; bits_value[1]=0x0; bits_value[2]=0x0; bits_value[3]=0x0;
			byte_seek=3;
			for (y=0; y<dib_height; y++){
				shift_value[0]=0; shift_value[1]=0; shift_value[2]=0; shift_value[3]=0;
				if (((dib_buffer[buffer_seek]>>8)&0xFF)>RGB_SPLIT) shift_value[byte_seek]|=shift[byte_seek];
				shift[byte_seek]>>=1;
				if ((dib_buffer[buffer_seek]&0xFF)>RGB_SPLIT) shift_value[byte_seek]|=shift[byte_seek];
				shift[byte_seek]>>=1;
				buffer_seek++;
				if (shift_value[byte_seek]>0){
					bits_value[byte_seek]&=(~shift_mask[byte_seek]);
					bits_value[byte_seek]|=shift_value[byte_seek];
				}
				shift_mask[byte_seek]>>=2;
				if (byte_seek==0 && shift[byte_seek]==0){
					data_stream[bits_seek]=(byte)bits_value[0];
					data_stream[bits_seek+1]=(byte)bits_value[1];
					data_stream[bits_seek+2]=(byte)bits_value[2];
					data_stream[bits_seek+3]=(byte)bits_value[3];
					bits_seek+=4;
					shift[0]=0x0; shift[1]=0x0; shift[2]=0x0; shift[3]=0x80;
					shift_mask[0]=0x0; shift_mask[1]=0x0; shift_mask[2]=0x0; shift_mask[3]=0xC0;
					bits_value[0]=0x0; bits_value[1]=0x0; bits_value[2]=0x0; bits_value[3]=0x0;
					byte_seek=3;
				}
				if (shift[byte_seek]==0){
					byte_seek--;
					shift[byte_seek]=0x80;
					shift_mask[byte_seek]=0xC0;
					bits_value[byte_seek]=0;
				}
			}
			if (shift[3]!=0x80){
				data_stream[bits_seek]=(byte)bits_value[0];
				data_stream[bits_seek+1]=(byte)bits_value[1];
				data_stream[bits_seek+2]=(byte)bits_value[2];
				data_stream[bits_seek+3]=(byte)bits_value[3];
				bits_seek+=4;
			}
		}
		
		return pixels_size;
	}

	//dib_buffer 点阵图片RGB数组
	//dib_buffer_size 数组长度
	//dib_width 点阵宽度
	//dib_height 点阵高度
	static int AddPixelsRGB(int[] dib_buffer, int dib_buffer_size, int dib_width, int dib_height) {
		int pixels_seek, pixels_size, pixels_height;
		int x, y, i;
		int shift[] = new int[4];
		int shift_value_r[] = new int[4];
		int shift_value_g[] = new int[4];
		int shift_value_b[] = new int[4];
		int bits_seek, bits_size;
		int buffer_seek=0;
		int byte_seek=3;

		pixels_height=((dib_height+0x1F)&(~0x1F));
		bits_size=(pixels_height>>3)*dib_width*3;
		pixels_seek=data_seek;
		pixels_size=PIXELS_STRUCT_SIZE+bits_size;
		blockFill(data_stream, pixels_seek, pixels_size, (byte)0);

		shortFill(data_stream, pixels_seek, (short)OBJECT_PIXELS);
		shortFill(data_stream, pixels_seek+PIXELS_BITS_OFFSET, (short)BITMAP_RGB);
		intFill(data_stream, pixels_seek+PIXELS_WIDTH_OFFSET, (int)dib_width);
		intFill(data_stream, pixels_seek+PIXELS_HEIGHT_OFFSET, (int)pixels_height);
		intFill(data_stream, pixels_seek+PIXELS_SIZE_OFFSET, (int)pixels_size);
		
		bits_seek=pixels_seek+PIXELS_STRUCT_SIZE;
		for (x=0; x<dib_width; x++){
			shift[0]=0x0; shift[1]=0x0; shift[2]=0x0; shift[3]=0x80;
			byte_seek=3;
			for (i=0; i<4; i++) shift_value_r[i]=0;
			for (i=0; i<4; i++) shift_value_g[i]=0;
			for (i=0; i<4; i++) shift_value_b[i]=0;
			for (y=0; y<dib_height; y++){
				if ((dib_buffer[buffer_seek]&0xFF)>RGB_SPLIT) {
					for (i=0; i<4; i++) shift_value_r[i]|=shift[i];
				}
				if (((dib_buffer[buffer_seek]>>8)&0xFF)>RGB_SPLIT) {
					for (i=0; i<4; i++) shift_value_g[i]|=shift[i];
				}
				if (((dib_buffer[buffer_seek]>>16)&0xFF)>RGB_SPLIT) {
					for (i=0; i<4; i++) shift_value_b[i]|=shift[i];
				}
				shift[byte_seek]>>=1;
				buffer_seek++;
				if (byte_seek==0 && shift[byte_seek]==0){
					for (i=0; i<4; i++) data_stream[bits_seek+i]=(byte)shift_value_r[i];
					bits_seek+=4;
					for (i=0; i<4; i++) data_stream[bits_seek+i]=(byte)shift_value_g[i];
					bits_seek+=4;
					for (i=0; i<4; i++) data_stream[bits_seek+i]=(byte)shift_value_b[i];
					bits_seek+=4;
					shift[0]=0x0; shift[1]=0x0; shift[2]=0x0; shift[3]=0x80;
					byte_seek=3;
					for (i=0; i<4; i++) shift_value_r[i]=0;
					for (i=0; i<4; i++) shift_value_g[i]=0;
					for (i=0; i<4; i++) shift_value_b[i]=0;
				}
				if (shift[byte_seek]==0){
					byte_seek--;
					shift[byte_seek]=0x80;
				}
			}
			if (shift[3]!=0x80){
				for (i=0; i<4; i++) data_stream[bits_seek+i]=(byte)shift_value_r[i];
				bits_seek+=4;
				for (i=0; i<4; i++) data_stream[bits_seek+i]=(byte)shift_value_g[i];
				bits_seek+=4;
				for (i=0; i<4; i++) data_stream[bits_seek+i]=(byte)shift_value_b[i];
				bits_seek+=4;
			}
		}
		
		return pixels_size;
	}

	//dib_buffer 点阵图片RGB数组
	//dib_buffer_size 数组长度
	//dib_width 点阵宽度
	//dib_height 点阵高度
	static int AddPixelsFull(int[] dib_buffer, int dib_buffer_size, int dib_width, int dib_height) {
		int pixels_seek, pixels_size, pixels_height;
		int x, y, z, i;
		int shift[] = new int[4];
		int data_value[] = new int[4];
		int r_value, g_value, b_value;
		int bits_seek, bits_size;
		int buffer_seek=0;
		int byte_seek=3;

		pixels_height=dib_height;
		bits_size=pixels_height*dib_width*4;
		pixels_seek=data_seek;
		pixels_size=PIXELS_STRUCT_SIZE+bits_size;
		blockFill(data_stream, pixels_seek, pixels_size, (byte)0);

		shortFill(data_stream, pixels_seek, (short)OBJECT_PIXELS);
		shortFill(data_stream, pixels_seek+PIXELS_BITS_OFFSET, (short)BITMAP_RGB);
		intFill(data_stream, pixels_seek+PIXELS_WIDTH_OFFSET, (int)dib_width);
		intFill(data_stream, pixels_seek+PIXELS_HEIGHT_OFFSET, (int)pixels_height);
		intFill(data_stream, pixels_seek+PIXELS_SIZE_OFFSET, (int)pixels_size);
		
		bits_seek=pixels_seek+PIXELS_STRUCT_SIZE;
		for (x=0; x<dib_width; x++){
			for (y=0; y<dib_height; y++){
				byte_seek=3;
				shift[0]=0x0; shift[1]=0x0; shift[2]=0x0; shift[3]=0x80;
				for (i=0; i<4; i++) data_value[i]=0;
				r_value=(dib_buffer[buffer_seek]&0xFF);
				g_value=((dib_buffer[buffer_seek]>>8)&0xFF);
				b_value=((dib_buffer[buffer_seek]>>16)&0xFF);
				if (r_value>0 || g_value>0 || b_value>0){
					for (z=0; z<8; z++){
						data_value[byte_seek]<<=1;
						if ((r_value & 0x80) > 0) data_value[byte_seek]|=1;
						data_value[byte_seek]<<=1;
						if ((g_value & 0x80) > 0) data_value[byte_seek]|=1;
						data_value[byte_seek]<<=1;
						if ((b_value & 0x80) > 0) data_value[byte_seek]|=1;
						data_value[byte_seek]<<=1;
						r_value<<=1;
						g_value<<=1;
						b_value<<=1;
						shift[byte_seek]>>=4;
						if (shift[byte_seek]==0){
							if (byte_seek==0) byte_seek=3; else	byte_seek--;
							shift[byte_seek]=0x80;
						}
					}
				}
				for (i=0; i<4; i++) data_stream[bits_seek+i]=(byte)data_value[i];
				bits_seek+=4;
				buffer_seek++;
			}
		}
		
		return pixels_size;
	}

	static int AddPixels(int[] dib_buffer, int dib_buffer_size, int dib_width, int dib_height) {
		switch(current_color_type) {
		case COLOR_TYPE_THREE:
			return AddPixelsRGB(dib_buffer, dib_buffer_size, dib_width, dib_height);
		case COLOR_TYPE_FULL:
			return AddPixelsFull(dib_buffer, dib_buffer_size, dib_width, dib_height);
		default:
			return AddPixelsRG(dib_buffer, dib_buffer_size, dib_width, dib_height);
		}
	}
	
	//buffer 点阵图片RGB数组
	//buffer_size 数组长度
	//width 点阵宽度
	//height 点阵高度
	//inmethod 引入方式，在此java文件最上方有定义
	//outmethod 引出方式，在此java文件最上方有定义
	//inspeed, outspeed 引入引出速度，0到5
	//stopmethod 停留方式，请置0
	//stopspeed 停留速度，请置0
	public void AddChildWindow(int[] buffer, int buffer_size, int width, int height, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		int seek, size;
		int pixels_size;

		seek=data_seek;
		size=WINDOW_STRUCT_SIZE;
		blockFill(data_stream, seek, size, (byte)0);

		shortFill(data_stream, seek, (short)OBJECT_WINDOW);
		byteFill(data_stream, seek+WINDOW_IN_METHOD_OFFSET, (byte)inmethod);
		shortFill(data_stream, seek+WINDOW_IN_SPEED_OFFSET, (short)inspeed);
		byteFill(data_stream, seek+WINDOW_OUT_METHOD_OFFSET, (byte)outmethod);
		shortFill(data_stream, seek+WINDOW_OUT_SPEED_OFFSET, (short)outspeed);
		byteFill(data_stream, seek+WINDOW_STOP_METHOD_OFFSET, (byte)stopmethod);
		shortFill(data_stream, seek+WINDOW_STOP_SPEED_OFFSET, (short)stopspeed);
		intFill(data_stream, seek+WINDOW_STOP_TIME_OFFSET, stoptime);
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		
		data_seek+=WINDOW_STRUCT_SIZE;
		pixels_size=AddPixels(buffer, buffer_size, width, height);
		
		size+=pixels_size;
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		windows_count++;
		shortFill(data_stream, windows_seek+WINDOWS_COUNT_OFFSET, (short)windows_count);
		windows_size+=size;
		intFill(data_stream, windows_seek+OBJECT_SIZE_OFFSET, windows_size);
		if (leaf_seek>0) {
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("childwindow="+data_seek);
	}
	
	//buffer 点阵图片RGB数组
	//buffer_size 数组长度
	//width 点阵宽度
	//height 点阵高度
	//inmethod 引入方式，在此java文件最上方有定义
	//outmethod 引出方式，在此java文件最上方有定义
	//inspeed, outspeed 引入引出速度，0到5
	//stopmethod 停留方式，请置0
	//stopspeed 停留速度，请置0
	//kind 计划模式，=0始终播放，=1按一周每日播放，=2按起止时间播放，=3不播放
	//week 一周定义，当kind=1时有效，从bit0-bit6表示周日到周六
	//              周日0x1     周一0x2    周二0x4     周三0x8     周四0x10     周五0x20     周六0x30
	//              这个值可以相加组合，例如只播放周一、周二、周三这3天，则week赋值0x2+0x4+0x8即可
	public void AddChildScheduleWindow(int[] buffer, int buffer_size, int width, int height, 
			int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime,
			int kind, int week, 
			int from_year, int from_month, int from_day, int from_hour, int from_minute, int from_second, 
			int to_year, int to_month, int to_day, int to_hour, int to_minute, int to_second) {
		int seek, size;
		int pixels_size;

		seek=data_seek;
		size=SCHEDULE_WINDOW_STRUCT_SIZE;
		blockFill(data_stream, seek, size, (byte)0);

		shortFill(data_stream, seek, (short)OBJECT_SCHEDULE_WINDOW);
		byteFill(data_stream, seek+WINDOW_IN_METHOD_OFFSET, (byte)inmethod);
		shortFill(data_stream, seek+WINDOW_IN_SPEED_OFFSET, (short)inspeed);
		byteFill(data_stream, seek+WINDOW_OUT_METHOD_OFFSET, (byte)outmethod);
		shortFill(data_stream, seek+WINDOW_OUT_SPEED_OFFSET, (short)outspeed);
		byteFill(data_stream, seek+WINDOW_STOP_METHOD_OFFSET, (byte)stopmethod);
		shortFill(data_stream, seek+WINDOW_STOP_SPEED_OFFSET, (short)stopspeed);
		intFill(data_stream, seek+WINDOW_STOP_TIME_OFFSET, stoptime);
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		shortFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_KIND_OFFSET, (short)kind);
		shortFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_WEEK_OFFSET, (short)week);
		intFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_FROMTIME_OFFSET, SystemTimeToTime(from_hour, from_minute, from_second));
		intFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_FROMDATE_OFFSET, SystemTimeToDate(from_year, from_month, from_day));
		intFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_TOTIME_OFFSET, SystemTimeToTime(to_hour, to_minute, to_second));
		intFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_TODATE_OFFSET, SystemTimeToDate(to_year, to_month, to_day));
		
		data_seek+=SCHEDULE_WINDOW_STRUCT_SIZE;
		pixels_size=AddPixels(buffer, buffer_size, width, height);
		
		size+=pixels_size;
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		windows_count++;
		shortFill(data_stream, windows_seek+WINDOWS_COUNT_OFFSET, (short)windows_count);
		windows_size+=size;
		intFill(data_stream, windows_seek+OBJECT_SIZE_OFFSET, windows_size);
		if (leaf_seek>0) {
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("childwindow="+data_seek);
	}

    static int AddPicturePixels(String filename) {
    	int pixels_size=0;
        BufferedImage bi;
        try {
        	bi = ImageIO.read(new FileInputStream(filename));
    		pixels_size=AddPixels(getBufferedImageRGB(bi), bi.getWidth() * bi.getHeight(), bi.getWidth(), bi.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return pixels_size;
    }
    
	//filename 图片文件名
	//inmethod 引入方式，在此java文件最上方有定义
	//outmethod 引出方式，在此java文件最上方有定义
	//inspeed, outspeed 引入引出速度，0到5
	//stopmethod 停留方式，请置0
	//stopspeed 停留速度，请置0
	public void AddChildPicture(String filename, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		int seek, size;
		int pixels_size;

		seek=data_seek;
		size=WINDOW_STRUCT_SIZE;
		blockFill(data_stream, seek, size, (byte)0);

		shortFill(data_stream, seek, (short)OBJECT_WINDOW);
		byteFill(data_stream, seek+WINDOW_IN_METHOD_OFFSET, (byte)inmethod);
		shortFill(data_stream, seek+WINDOW_IN_SPEED_OFFSET, (short)inspeed);
		byteFill(data_stream, seek+WINDOW_OUT_METHOD_OFFSET, (byte)outmethod);
		shortFill(data_stream, seek+WINDOW_OUT_SPEED_OFFSET, (short)outspeed);
		byteFill(data_stream, seek+WINDOW_STOP_METHOD_OFFSET, (byte)stopmethod);
		shortFill(data_stream, seek+WINDOW_STOP_SPEED_OFFSET, (short)stopspeed);
		intFill(data_stream, seek+WINDOW_STOP_TIME_OFFSET, stoptime);
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		
		data_seek+=WINDOW_STRUCT_SIZE;
		pixels_size=AddPicturePixels(filename);
		
		size+=pixels_size;
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		windows_count++;
		shortFill(data_stream, windows_seek+WINDOWS_COUNT_OFFSET, (short)windows_count);
		windows_size+=size;
		intFill(data_stream, windows_seek+OBJECT_SIZE_OFFSET, windows_size);
		if (leaf_seek>0) {
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("childwindow="+data_seek);
	}

    static int GetTextPixelsWidth(String text, String fontname, int fontsize, int fontstyle) {
    	int value=0;
        BufferedImage bi;
        try {
            bi = createImage(text, new Font(fontname, fontstyle, fontsize), Color.BLACK, Color.YELLOW);
            value = bi.getWidth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return value;
    }

    static int GetTextPixelsHeight(String text, String fontname, int fontsize, int fontstyle) {
    	int value=0;
        BufferedImage bi;
        try {
            bi = createImage(text, new Font(fontname, fontstyle, fontsize), Color.BLACK, Color.YELLOW);
            value = bi.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return value;
    }

	/*
     * 根据str,font的样式返回图像缓存
     */
    static BufferedImage createImage(String str, Font font, Color bkcolor, Color color) throws Exception {
        // 获取font的样式应用在str上的整个矩形
        Rectangle2D r = font.getStringBounds(str, new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
        int unitHeight = (int) Math.floor(r.getHeight());// 获取单个字符的高度
        // 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 1;
        int height = unitHeight + 3;// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        // 创建图片
    	//logger.error(host+"<<createImage--BufferedImage>> <width>="+width+"<height>="+height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setColor(bkcolor);
        g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
        g.setColor(color);// 再换成红色
        g.setFont(font);// 设置画笔字体
        g.drawString(str, 0, font.getSize());// 画出字符串
        g.dispose();
        return image;
    }
    
    static int GetJuhaoOffsetX(int fontsize){
    	return -4;
    }

    static int GetJuhaoOffsetY(int fontsize){
    	return 8;
    }

    /*
     * 根据str,font的样式返回图像缓存
     */
    static BufferedImage createImageJuhao(String str, Font font, Color bkcolor, Color color, int fontsize) throws Exception {
        // 获取font的样式应用在str上的整个矩形
    	FontRenderContext rc = new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false);
        Rectangle2D r = font.getStringBounds(str, rc);
        String sTemp;
        int unitHeight = (int) Math.floor(r.getHeight());// 获取单个字符的高度
        // 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 1;
        int height = unitHeight + 3;// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        int[] iWidths;
        String[] sItems;
        int i, k, x, y;
        
        x = 0;
        sTemp = str;
        k = sTemp.indexOf("。");
        while (k >= 0){
        	x+=2;
        	sTemp = sTemp.substring(k+1);
            k = sTemp.indexOf("。");
        }
        if (!sTemp.isEmpty()) {
        	x++;
        }
        sItems = new String[x];
        iWidths = new int[x];

        x = 0;
        sTemp = str;
        k = sTemp.indexOf("。");
        while (k >= 0){
        	sItems[x++] = sTemp.substring(0, k);
        	sItems[x++] = "。";
        	sTemp = sTemp.substring(k+1);
            k = sTemp.indexOf("。");
        }
        if (!sTemp.isEmpty()) {
        	sItems[x++] = sTemp;
        }
        
        k = x;
        width = 0;
        for (i=0; i<k; i++){
            r = font.getStringBounds(sItems[i], rc);
            iWidths[i] = (int) Math.round(r.getWidth()) + 1;
            width += iWidths[i];
        }
        
        // 创建图片
    	//logger.error(host+"<<createImage--BufferedImage>> <width>="+width+"<height>="+height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setColor(bkcolor);
        g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
        g.setColor(color);// 再换成红色
        g.setFont(font);// 设置画笔字体
        
        x = 0;
        y = font.getSize();
        for (i=0; i<k; i++){
        	if (sItems[i] == "。"){
                g.drawString(sItems[i], x + GetJuhaoOffsetX(fontsize), y + GetJuhaoOffsetY(fontsize));// 画出字符串
        	}else{
                g.drawString(sItems[i], x, y);// 画出字符串
        	}
            x += iWidths[i];
        }

        //g.drawString(str, 0, font.getSize());// 画出字符串
        g.dispose();
        return image;
    }

    /*
     * 根据str,font的样式返回图像缓存
     */
    @SuppressWarnings("unchecked")
	static BufferedImage createImageEx(String str, Font font, Color bkcolor, Color color, int halign, boolean wordwrap, int matrixwidth) throws Exception {
    	if (!wordwrap)
    	{
    		return createImage(str, font, bkcolor, color);
    	}
    	else
    	{
	        // 获取font的样式应用在str上的整个矩形
        	int textwidth;
    		int i, width=0, height=0, cwidth=0, cx=0, cy=0, x, y;
    		Rectangle2D r;
    		char ch;
    		boolean ch_return=false;
    		ArrayList strlist = new ArrayList();
    		ArrayList widthlist = new ArrayList();
    		String szChar="";
    		String szLine="";
    		i=0;
    		while (i<str.length()){
    			ch=str.charAt(i);
    			szChar=String.valueOf(ch);
    			if (szChar.compareTo("\r")==0)
    			{
        			r=font.getStringBounds("V", new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
        			// 获取单个字符的高度， 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        			cy = (int) Math.floor(r.getHeight()) + 3;
        			if (cwidth>width) width=cwidth;
    				strlist.add(szLine);
    				widthlist.add(cwidth);
    				cwidth=0;
    				height+=cy;
    				szLine="";
    				ch_return=true;
        			i++;
    			}
    			else if (szChar.compareTo("\n")==0)
    			{
    				if (!ch_return)
    				{
            			r=font.getStringBounds("V", new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
            			// 获取单个字符的高度， 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
            			cy = (int) Math.floor(r.getHeight()) + 3;
            			if (cwidth>width) width=cwidth;
        				strlist.add(szLine);
        				widthlist.add(cwidth);
        				cwidth=0;
        				height+=cy;
        				szLine="";
    				}
    				ch_return=false;
        			i++;
    			}
    			else
    			{
        			r=font.getStringBounds(szChar, new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
        			// 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        			cx = (int) Math.round(r.getWidth()) + 1;
        			// 获取单个字符的高度， 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        			cy = (int) Math.floor(r.getHeight()) + 3;
        			if (cwidth>0 && cwidth+cx>matrixwidth){
        				if (cwidth>width) width=cwidth;
        				strlist.add(szLine);
        				widthlist.add(cwidth);
        				cwidth=cx;
        				height+=cy;
        				szLine=szChar;
        			}
        			else{
        				cwidth+=cx; 
        				szLine=szLine+szChar;
        			}
        			ch_return=false;
        			i++;
    			}
				System.out.print(ch);
    		}
    		if (cwidth>0){
				if (cwidth>width) width=cwidth;
				strlist.add(szLine);
				widthlist.add(cwidth);
				height+=cy;
    		}
    		if (width==0 || height==0){
    			width=10; height=10;
    		}
	        // 创建图片
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
	        Graphics g = image.getGraphics();
	        g.setColor(bkcolor);
	        g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
	        g.setColor(color);// 再换成红色
	        g.setFont(font);// 设置画笔字体
	        y=font.getSize();
	        for (i=0; i<strlist.size(); i++){
	        	szLine=strlist.get(i).toString();
	        	textwidth=Integer.parseInt(widthlist.get(i).toString());
	    		x=0;
    			switch(halign){
                case ALIGN_CENTER:
                	x=(matrixwidth-textwidth)/2;
	    			break;
	    		case ALIGN_RIGHT:
	    			x=matrixwidth-textwidth;
	    			break;
	    		}
	        	g.drawString(szLine, x, y);// 画出字符串
	        	y+=cy;
	        }
	        g.dispose();
	        strlist.clear();
	        widthlist.clear();
	        return image;
    	}
    }

    /*
     * 提取缓存图片的RGB一维数组
     */
    static int[] getBufferedImageRGB(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int red,green,blue,pix;
        int[] imageRGB = new int[width * height];
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pix = bufferedImage.getRGB(i, j);
                red = (pix & 0xff0000) >> 16;
                green = (pix & 0x00ff00) >> 8;
                blue = (pix & 0xff);
                imageRGB[i*height+j] = (int) blue << 16 | (int) green << 8 | (int) red;
            }
        }

        return imageRGB;
    }
    
    static int AddTextPixels(String text, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle, int halign, boolean wordwrap, int matrixwidth) {
    	int pixels_size=0;
        BufferedImage bi;
        try {
        	//此处会出现异常，进入exception，造成点阵数据没有添加上，节目校验就会失败
        	//请在此处打印日志，显示传入的所有参数
        	//logger.error(host+"<<AddTextPixels--createImageEx>> <text>="+text+"<fontname>="+fontname+"<fontstyle>="+fontstyle+"<fontsize>="+fontsize+"<bkcolor>="+bkcolor+"<fontcolor>="+fontcolor+"<halign>="+halign+"<wordwrap>="+wordwrap+"<matrixwidth>="+matrixwidth);
            bi = createImageEx(text, new Font(fontname, fontstyle, fontsize), bkcolor, fontcolor, halign, wordwrap, matrixwidth);
            //logger.error(host+"<<AddTextPixels--AddPixels>> <biWidth>="+bi.getWidth()+"<biHeight>="+bi.getHeight());
    		pixels_size=AddPixels(getBufferedImageRGB(bi), bi.getWidth() * bi.getHeight(), bi.getWidth(), bi.getHeight());
        } catch (Exception e) {
            //logger.error(host+"<<AddTextPixels--Exception>> <exception_msg>="+e.getMessage());
            e.printStackTrace();
        }
    	return pixels_size;
    }
    
    static int AddTextPixelsJuhao(String text, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle, int halign, boolean wordwrap, int matrixwidth) {
    	int pixels_size=0;
        BufferedImage bi;
        try {
        	//此处会出现异常，进入exception，造成点阵数据没有添加上，节目校验就会失败
        	//请在此处打印日志，显示传入的所有参数
        	//logger.error(host+"<<AddTextPixels--createImageEx>> <text>="+text+"<fontname>="+fontname+"<fontstyle>="+fontstyle+"<fontsize>="+fontsize+"<bkcolor>="+bkcolor+"<fontcolor>="+fontcolor+"<halign>="+halign+"<wordwrap>="+wordwrap+"<matrixwidth>="+matrixwidth);
            bi = createImageJuhao(text, new Font(fontname, fontstyle, fontsize), bkcolor, fontcolor, fontsize);
            //logger.error(host+"<<AddTextPixels--AddPixels>> <biWidth>="+bi.getWidth()+"<biHeight>="+bi.getHeight());
    		pixels_size=AddPixels(getBufferedImageRGB(bi), bi.getWidth() * bi.getHeight(), bi.getWidth(), bi.getHeight());
        } catch (Exception e) {
            //logger.error(host+"<<AddTextPixels--Exception>> <exception_msg>="+e.getMessage());
            e.printStackTrace();
        }
    	return pixels_size;
    }

    //buffer 点阵图片RGB数组
	//buffer_size 数组长度
	//width 点阵宽度
	//height 点阵高度
	//inmethod 引入方式，在此java文件最上方有定义
	//outmethod 引出方式，在此java文件最上方有定义
	//inspeed, outspeed 引入引出速度，0到5
	//stopmethod 停留方式，请置0
	//stopspeed 停留速度，请置0
	public void AddChildText(String text, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle, int halign, boolean wordwrap, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		int seek, size;
		int pixels_size;
		int matrix_width;

		seek=data_seek;
		size=WINDOW_STRUCT_SIZE;
		blockFill(data_stream, seek, size, (byte)0);

		shortFill(data_stream, seek, (short)OBJECT_WINDOW);
		byteFill(data_stream, seek+WINDOW_IN_METHOD_OFFSET, (byte)inmethod);
		shortFill(data_stream, seek+WINDOW_IN_SPEED_OFFSET, (short)inspeed);
		byteFill(data_stream, seek+WINDOW_OUT_METHOD_OFFSET, (byte)outmethod);
		shortFill(data_stream, seek+WINDOW_OUT_SPEED_OFFSET, (short)outspeed);
		byteFill(data_stream, seek+WINDOW_STOP_METHOD_OFFSET, (byte)stopmethod);
		shortFill(data_stream, seek+WINDOW_STOP_SPEED_OFFSET, (short)stopspeed);
		intFill(data_stream, seek+WINDOW_STOP_TIME_OFFSET, stoptime);
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		
		data_seek+=WINDOW_STRUCT_SIZE;
		matrix_width=asInt(data_stream, windows_seek+OBJECT_RIGHT_OFFSET)-asInt(data_stream, windows_seek+OBJECT_LEFT_OFFSET);
		pixels_size=AddTextPixels(text, fontname, fontsize, bkcolor, fontcolor, fontstyle, halign, wordwrap, matrix_width);
		
		size+=pixels_size;
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		windows_count++;
		shortFill(data_stream, windows_seek+WINDOWS_COUNT_OFFSET, (short)windows_count);
		windows_size+=size;
		intFill(data_stream, windows_seek+OBJECT_SIZE_OFFSET, windows_size);
		if (leaf_seek>0) {
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("childwindow="+data_seek);
	}
	
    //buffer 点阵图片RGB数组
	//buffer_size 数组长度
	//width 点阵宽度
	//height 点阵高度
	//inmethod 引入方式，在此java文件最上方有定义
	//outmethod 引出方式，在此java文件最上方有定义
	//inspeed, outspeed 引入引出速度，0到5
	//stopmethod 停留方式，请置0
	//stopspeed 停留速度，请置0
	public void AddChildTextJuhao(String text, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle, int halign, boolean wordwrap, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		int seek, size;
		int pixels_size;
		int matrix_width;

		seek=data_seek;
		size=WINDOW_STRUCT_SIZE;
		blockFill(data_stream, seek, size, (byte)0);

		shortFill(data_stream, seek, (short)OBJECT_WINDOW);
		byteFill(data_stream, seek+WINDOW_IN_METHOD_OFFSET, (byte)inmethod);
		shortFill(data_stream, seek+WINDOW_IN_SPEED_OFFSET, (short)inspeed);
		byteFill(data_stream, seek+WINDOW_OUT_METHOD_OFFSET, (byte)outmethod);
		shortFill(data_stream, seek+WINDOW_OUT_SPEED_OFFSET, (short)outspeed);
		byteFill(data_stream, seek+WINDOW_STOP_METHOD_OFFSET, (byte)stopmethod);
		shortFill(data_stream, seek+WINDOW_STOP_SPEED_OFFSET, (short)stopspeed);
		intFill(data_stream, seek+WINDOW_STOP_TIME_OFFSET, stoptime);
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		
		data_seek+=WINDOW_STRUCT_SIZE;
		matrix_width=asInt(data_stream, windows_seek+OBJECT_RIGHT_OFFSET)-asInt(data_stream, windows_seek+OBJECT_LEFT_OFFSET);
		pixels_size=AddTextPixelsJuhao(text, fontname, fontsize, bkcolor, fontcolor, fontstyle, halign, wordwrap, matrix_width);
		
		size+=pixels_size;
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		windows_count++;
		shortFill(data_stream, windows_seek+WINDOWS_COUNT_OFFSET, (short)windows_count);
		windows_size+=size;
		intFill(data_stream, windows_seek+OBJECT_SIZE_OFFSET, windows_size);
		if (leaf_seek>0) {
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("childwindow="+data_seek);
	}

	//buffer 点阵图片RGB数组
	//buffer_size 数组长度
	//width 点阵宽度
	//height 点阵高度
	//inmethod 引入方式，在此java文件最上方有定义
	//outmethod 引出方式，在此java文件最上方有定义
	//inspeed, outspeed 引入引出速度，0到5
	//stopmethod 停留方式，请置0
	//stopspeed 停留速度，请置0
	public void AddChildScheduleText(String text, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle, int halign, boolean wordwrap,  
			int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime,
			int kind, int week, 
			int from_year, int from_month, int from_day, int from_hour, int from_minute, int from_second, 
			int to_year, int to_month, int to_day, int to_hour, int to_minute, int to_second) {
		int seek, size;
		int pixels_size;
		int matrix_width;

		seek=data_seek;
		size=SCHEDULE_WINDOW_STRUCT_SIZE;
		blockFill(data_stream, seek, size, (byte)0);

		shortFill(data_stream, seek, (short)OBJECT_SCHEDULE_WINDOW);
		byteFill(data_stream, seek+WINDOW_IN_METHOD_OFFSET, (byte)inmethod);
		shortFill(data_stream, seek+WINDOW_IN_SPEED_OFFSET, (short)inspeed);
		byteFill(data_stream, seek+WINDOW_OUT_METHOD_OFFSET, (byte)outmethod);
		shortFill(data_stream, seek+WINDOW_OUT_SPEED_OFFSET, (short)outspeed);
		byteFill(data_stream, seek+WINDOW_STOP_METHOD_OFFSET, (byte)stopmethod);
		shortFill(data_stream, seek+WINDOW_STOP_SPEED_OFFSET, (short)stopspeed);
		intFill(data_stream, seek+WINDOW_STOP_TIME_OFFSET, stoptime);
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		shortFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_KIND_OFFSET, (short)kind);
		shortFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_WEEK_OFFSET, (short)week);
		intFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_FROMTIME_OFFSET, SystemTimeToTime(from_hour, from_minute, from_second));
		intFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_FROMDATE_OFFSET, SystemTimeToDate(from_year, from_month, from_day));
		intFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_TOTIME_OFFSET, SystemTimeToTime(to_hour, to_minute, to_second));
		intFill(data_stream, seek+WINDOW_STRUCT_SIZE+PLAY_SCHEDULE_TODATE_OFFSET, SystemTimeToDate(to_year, to_month, to_day));
		
		data_seek+=SCHEDULE_WINDOW_STRUCT_SIZE;
		matrix_width=asInt(data_stream, windows_seek+OBJECT_RIGHT_OFFSET)-asInt(data_stream, windows_seek+OBJECT_LEFT_OFFSET);
		pixels_size=AddTextPixels(text, fontname, fontsize, bkcolor, fontcolor, fontstyle, halign, wordwrap, matrix_width);
		
		size+=pixels_size;
		intFill(data_stream, seek+WINDOW_SIZE_OFFSET, size);
		windows_count++;
		shortFill(data_stream, windows_seek+WINDOWS_COUNT_OFFSET, (short)windows_count);
		windows_size+=size;
		intFill(data_stream, windows_seek+OBJECT_SIZE_OFFSET, windows_size);
		if (leaf_seek>0) {
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("childwindow="+data_seek);
	}
	
	//日期时间显示格式处理函数
	private void SDClockFormat(byte[] stream, int index, String format){
		if (format=="#y"){
			stream[index]=DF_YEAR;
			stream[index+1]=0;
		}else if (format=="#m"){
			stream[index]=DF_MONTH;
			stream[index+1]=0;
		}else if (format=="#w"){
			stream[index]=DF_WEEK;
			stream[index+1]=0;
		}else if (format=="#d"){
			stream[index]=DF_DAY;
			stream[index+1]=0;
		}else if (format=="#h"){
			stream[index]=DF_HOUR;
			stream[index+1]=0;
		}else if (format=="#n"){
			stream[index]=DF_MINUTE;
			stream[index+1]=0;
		}else if (format=="#s"){
			stream[index]=DF_SECOND;
			stream[index+1]=0;
		}else if (format=="年"){
			stream[index]=DF_USER;
			stream[index+1]=0;
		}else if (format=="月"){
			stream[index]=DF_USER;
			stream[index+1]=1;
		}else if (format=="日"){
			stream[index]=DF_USER;
			stream[index+1]=2;
		}else if (format=="时"){
			stream[index]=DF_USER;
			stream[index+1]=3;
		}else if (format=="分"){
			stream[index]=DF_USER;
			stream[index+1]=4;
		}else if (format=="秒"){
			stream[index]=DF_USER;
			stream[index+1]=5;
		}else if (format=="星"){
			stream[index]=DF_USER;
			stream[index+1]=6;
		}else if (format=="期"){
			stream[index]=DF_USER;
			stream[index+1]=7;
		}else if (format=="-"){
			stream[index]=DF_USER;
			stream[index+1]=8;
		}else if (format==":"){
			stream[index]=DF_USER;
			stream[index+1]=9;
		}else if (format==" "){
			stream[index]=DF_USER;
			stream[index+1]=10;
		}
	}
	
	public void AddStringDateTime(int left, int top, int width, int height, int color, int fontset, String[] formats, int formats_count) {
		int seek, size;
		int i;
		seek=data_seek;
		size=SDCLOCK_STRUCT_SIZE;
		blockFill(data_stream, seek, size, (byte)0);
		shortFill(data_stream, seek, (short)OBJECT_STRING_DCLOCK);
		blockFill(data_stream, seek+OBJECT_TRANSPARENT_OFFSET, 1, (byte)1);
		intFill(data_stream, seek+OBJECT_LEFT_OFFSET, left);
		intFill(data_stream, seek+OBJECT_TOP_OFFSET, top);
		intFill(data_stream, seek+OBJECT_RIGHT_OFFSET, left+width);
		intFill(data_stream, seek+OBJECT_BOTTOM_OFFSET, top+height);
		for (i=0; i<formats_count; i++){
			SDClockFormat(data_stream, seek+OBJECT_STRUCT_SIZE+SDCLOCK_FORMAT_OFFSET+(i<<1), formats[i]);
		}
		intFill(data_stream, seek+OBJECT_STRUCT_SIZE+SDCLOCK_COLOR_OFFSET, color);
		byteFill(data_stream, seek+OBJECT_STRUCT_SIZE+SDCLOCK_FONTSET_OFFSET, (byte)fontset);
		intFill(data_stream, seek+OBJECT_SIZE_OFFSET, size);
		if (leaf_seek>0) {
			leaf_count++;
			shortFill(data_stream, leaf_seek+LEAF_COUNT_OFFSET, (short)leaf_count);
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("strings="+data_seek);
	}

	//添加内码文字，需要控制卡下载字库
	//left,top,width,height: 左上宽高
	//text: 文字
	//inmethod: 引入方式
	//inspeed: 引入速度
	//outmethod: 引出方式
	//outspeed: 引出速度
	//stopmethod: 停留方式
	//stopspeed: 停留速度
	//stoptime: 停留时间（毫秒）
	public void AddString(int left, int top, int width, int height, String text, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime, int color, int fontset) {
		AddStrings(left, top, width, height);
		AddChildString(text, inmethod, inspeed, outmethod, outspeed, stopmethod, stopspeed, stoptime, color, fontset);
	}
	
	//添加点阵图片
	//left,top,width,height: 左上宽高
	//buffer: 图片点阵数据
	//buffer_size: 数据长度
	//inmethod: 引入方式
	//inspeed: 引入速度
	//outmethod: 引出方式
	//outspeed: 引出速度
	//stopmethod: 停留方式
	//stopspeed: 停留速度
	//stoptime: 停留时间（毫秒）
	public void AddWindow(int left, int top, int width, int height, int[] buffer, int buffer_size, int dib_width, int dib_height, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		AddWindows(left, top, width, height);
		AddChildWindow(buffer, buffer_size, dib_width, dib_height, inmethod, inspeed, outmethod, outspeed, stopmethod, stopspeed, stoptime);
	}
	
	//添加图片文件
	//left,top,width,height: 左上宽高
	//filename: 图片文件名
	//inmethod: 引入方式
	//inspeed: 引入速度
	//outmethod: 引出方式
	//outspeed: 引出速度
	//stopmethod: 停留方式
	//stopspeed: 停留速度
	//stoptime: 停留时间（毫秒）
	public void AddPicture(int left, int top, int width, int height, String filename, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		AddWindows(left, top, width, height);
		AddChildPicture(filename, inmethod, inspeed, outmethod, outspeed, stopmethod, stopspeed, stoptime);
	}
	
	//添加点阵文字
	//left,top,width,height: 左上宽高
	//text: 文字
	//fontname: 字体名称
	//fontsize: 字体大小
	//bkcolor: 背景色（无效，置0）
	//fontcolor: 字体颜色
	//fontstyle: 字体风格（无效，置0）
	//inmethod: 引入方式
	//inspeed: 引入速度
	//outmethod: 引出方式
	//outspeed: 引出速度
	//stopmethod: 停留方式
	//stopspeed: 停留速度
	//stoptime: 停留时间（毫秒）
	public void AddText(int left, int top, int width, int height, String text, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle, int halign, boolean wordwrap, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		AddWindows(left, top, width, height);
		AddChildText(text, fontname, fontsize, bkcolor, fontcolor, fontstyle, halign, wordwrap, inmethod, inspeed, outmethod, outspeed, stopmethod, stopspeed, stoptime);
	}
	
	//添加点阵文字
	//left,top,width,height: 左上宽高
	//text: 文字
	//halign: 横向对齐方式 0靠左，1居中，2靠右
	//valign: 纵向对齐方式 0靠上，1居中，2靠下
	//fontname: 字体名称
	//fontsize: 字体大小
	//bkcolor: 背景色（无效，置0）
	//fontcolor: 字体颜色
	//fontstyle: 字体风格（无效，置0）
	//inmethod: 引入方式
	//inspeed: 引入速度
	//outmethod: 引出方式
	//outspeed: 引出速度
	//stopmethod: 停留方式
	//stopspeed: 停留速度
	//stoptime: 停留时间（毫秒）
	public void AddTextEx(int left, int top, int width, int height, String text, int halign, int valign, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		int textwidth, textheight;
		AddWindows(left, top, width, height);
		AddChildText(text, fontname, fontsize, bkcolor, fontcolor, fontstyle, halign, false, inmethod, inspeed, outmethod, outspeed, stopmethod, stopspeed, stoptime);

		textwidth=GetTextPixelsWidth(text, fontname, fontsize, fontstyle);
		if (textwidth<width){
			switch(halign){
			case ALIGN_CENTER:
				intFill(data_stream, windows_seek+OBJECT_LEFT_OFFSET, left+((width-textwidth)>>1));
				break;
			case ALIGN_RIGHT:
				intFill(data_stream, windows_seek+OBJECT_LEFT_OFFSET, left+width-textwidth);
				break;
			}
		}
		textheight=GetTextPixelsHeight(text, fontname, fontsize, fontstyle);
		if (textheight<height){
			switch(valign){
			case ALIGN_CENTER:
				intFill(data_stream, windows_seek+OBJECT_TOP_OFFSET, top+((height-textheight)>>1));
				break;
			case ALIGN_BOTTOM:
				intFill(data_stream, windows_seek+OBJECT_TOP_OFFSET, top+height-textheight);
				break;
			}
		}
	}
	
	//添加点阵文字
	//left,top,width,height: 左上宽高
	//text: 文字
	//fontname: 字体名称
	//fontsize: 字体大小
	//bkcolor: 背景色（无效，置0）
	//fontcolor: 字体颜色
	//fontstyle: 字体风格（无效，置0）
	//inmethod: 引入方式
	//inspeed: 引入速度
	//outmethod: 引出方式
	//outspeed: 引出速度
	//stopmethod: 停留方式
	//stopspeed: 停留速度
	//stoptime: 停留时间（毫秒）
	public void AddTextJuhao(int left, int top, int width, int height, String text, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle, int halign, boolean wordwrap, int inmethod, int inspeed, int outmethod, int outspeed, int stopmethod, int stopspeed, int stoptime) {
		AddWindows(left, top, width, height);
		AddChildTextJuhao(text, fontname, fontsize, bkcolor, fontcolor, fontstyle, halign, wordwrap, inmethod, inspeed, outmethod, outspeed, stopmethod, stopspeed, stoptime);
	}

	//日期时间显示格式处理函数
	private int DClockFormat(byte[] stream, int index, String format){
		if (format=="#y"){
			stream[index]=DF_USER;
			stream[index+1]=2;
			stream[index+2]=DF_USER;
			stream[index+3]=0;
			stream[index+4]=DF_YEAR;
			stream[index+5]=0;
			stream[index+6]=DF_YEAR;
			stream[index+7]=1;
			return 8;
		}else if (format=="#m"){
			stream[index]=DF_MONTH;
			stream[index+1]=0;
			stream[index+2]=DF_MONTH;
			stream[index+3]=1;
			return 4;
		}else if (format=="#w"){
			stream[index]=DF_WEEK;
			stream[index+1]=10;
			return 2;
		}else if (format=="#d"){
			stream[index]=DF_DAY;
			stream[index+1]=0;
			stream[index+2]=DF_DAY;
			stream[index+3]=1;
			return 4;
		}else if (format=="#h"){
			stream[index]=DF_HOUR;
			stream[index+1]=0;
			stream[index+2]=DF_HOUR;
			stream[index+3]=1;
			return 4;
		}else if (format=="#n"){
			stream[index]=DF_MINUTE;
			stream[index+1]=0;
			stream[index+2]=DF_MINUTE;
			stream[index+3]=1;
			return 4;
		}else if (format=="#s"){
			stream[index]=DF_SECOND;
			stream[index+1]=0;
			stream[index+2]=DF_SECOND;
			stream[index+3]=1;
			return 4;
		}else if (format=="年"){
			stream[index]=DF_USER;
			stream[index+1]=17;
			return 2;
		}else if (format=="月"){
			stream[index]=DF_USER;
			stream[index+1]=18;
			return 2;
		}else if (format=="日"){
			stream[index]=DF_USER;
			stream[index+1]=19;
			return 2;
		}else if (format=="时"){
			stream[index]=DF_USER;
			stream[index+1]=20;
			return 2;
		}else if (format=="分"){
			stream[index]=DF_USER;
			stream[index+1]=21;
			return 2;
		}else if (format=="秒"){
			stream[index]=DF_USER;
			stream[index+1]=22;
			return 2;
		}else if (format=="星期"){
			stream[index]=DF_USER;
			stream[index+1]=23;
			return 2;
		}else if (format=="-"){
			stream[index]=DF_USER;
			stream[index+1]=24;
			return 2;
		}else if (format==":"){
			stream[index]=DF_USER;
			stream[index+1]=25;
			return 2;
		}else if (format==" "){
			stream[index]=DF_USER;
			stream[index+1]=26;
			return 2;
		}
		return 0;
	}
	
	public void AddDateTime(int left, int top, int width, int height, String[] formats, int formats_count, String fontname, int fontsize, Color bkcolor, Color fontcolor, int fontstyle)
	{
		int seek, format_seek, size;
		int i;
		int pixels_size;
		int pixelset_size;
		
		seek=data_seek;
		size=DCLOCK_STRUCT_SIZE;
		blockFill(data_stream, seek, size, (byte)0);
		shortFill(data_stream, seek, (short)OBJECT_DCLOCK);
		blockFill(data_stream, seek+OBJECT_TRANSPARENT_OFFSET, 1, (byte)1);
		intFill(data_stream, seek+OBJECT_LEFT_OFFSET, left);
		intFill(data_stream, seek+OBJECT_TOP_OFFSET, top);
		intFill(data_stream, seek+OBJECT_RIGHT_OFFSET, left+width);
		intFill(data_stream, seek+OBJECT_BOTTOM_OFFSET, top+height);
		format_seek=OBJECT_STRUCT_SIZE+DCLOCK_FORMAT_OFFSET;
		for (i=0; i<formats_count; i++){
			format_seek+=DClockFormat(data_stream, seek+format_seek, formats[i]);
		}
		
		pixelset_size=PIXELSET_STRUCT_SIZE;
		shortFill(data_stream, seek+DCLOCK_STRUCT_SIZE, (short)OBJECT_PIXELSET);
		shortFill(data_stream, seek+DCLOCK_STRUCT_SIZE+PIXELSET_COUNT_OFFSET, (short)27);
		
		data_seek+=(DCLOCK_STRUCT_SIZE+PIXELSET_STRUCT_SIZE);
		pixels_size=AddTextPixels("0", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("1", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("2", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("3", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("4", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("5", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("6", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("7", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("8", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("9", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("日", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("一", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("二", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("三", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("四", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("五", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("六", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("年", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("月", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("日", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("时", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("分", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("秒", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("星期", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels("-", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels(":", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		pixels_size=AddTextPixels(" ", fontname, fontsize, bkcolor, fontcolor, fontstyle, 0, false, 0);
		data_seek+=pixels_size;
		pixelset_size+=pixels_size;
		
		intFill(data_stream, seek+DCLOCK_STRUCT_SIZE+PIXELSET_SIZE_OFFSET, pixelset_size);

		size+=pixelset_size;

		intFill(data_stream, seek+OBJECT_SIZE_OFFSET, size);
		if (leaf_seek>0) {
			leaf_count++;
			shortFill(data_stream, leaf_seek+LEAF_COUNT_OFFSET, (short)leaf_count);
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
	}
	
	//interval 两次语音播放时间间隔
	//times 重复次数 =0时，无限循环重复播放
	//speeker 朗读者 0-5
	//volumn 音量 0-9
	//speed 语速 0-9
	//tone 语调 0-9
	public void AddVoice(String text, int interval, int times, int speaker, int volumn, int speed, int tone)
	{
		int seek, size;
		int len=0;
		byte[] gb2312;
		try {
			byte[] s=text.getBytes("GB2312");
			print_stream(s, s.length);
			len=s.length+1;
			if ((len&0x3)>0) len=((len+0x3)&(~0x3));
			gb2312=new byte[len];
			blockFill(gb2312, 0, len, (byte)0);
			blockCopy(gb2312, 0, s, 0, s.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gb2312=new byte[0];
		}
		seek=data_seek;
		size=VOICE_STRUCT_SIZE+len;
		size=((size+0x3)&(~0x3));
		blockFill(data_stream, seek, size, (byte)0);
		shortFill(data_stream, seek, (short)OBJECT_VOICE);
		intFill(data_stream, seek+OBJECT_SIZE_OFFSET, size);
		intFill(data_stream, seek+VOICE_INTERVAL_OFFSET, interval);
		intFill(data_stream, seek+VOICE_TIMES_OFFSET, times);
		byteFill(data_stream, seek+VOICE_SPEAKER_OFFSET, (byte)speaker);
		byteFill(data_stream, seek+VOICE_VOLUMN_OFFSET, (byte)volumn);
		byteFill(data_stream, seek+VOICE_SPEED_OFFSET, (byte)speed);
		byteFill(data_stream, seek+VOICE_TONE_OFFSET, (byte)tone);
		blockCopy(data_stream, seek+VOICE_STRUCT_SIZE, gb2312, 0, len);
		
		if (leaf_seek>0) {
			leaf_count++;
			shortFill(data_stream, leaf_seek+LEAF_COUNT_OFFSET, (short)leaf_count);
			leaf_size+=size;
			intFill(data_stream, leaf_seek+LEAF_SIZE_OFFSET, leaf_size);
		}
		if (region_seek>0) {
			region_size+=size;
			intFill(data_stream, region_seek+REGION_SIZE_OFFSET, region_size);
		}
		if (chapter_seek>0) {
			chapter_size+=size;
			intFill(data_stream, chapter_seek+CHAPTER_SIZE_OFFSET, chapter_size);
		}
		root_size+=strings_size;
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=seek+size;
		//System.out.println("strings="+data_seek);
	}
	
	// 起始包
	public int pkg_begin(byte[] packet, int addr) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_BEGIN);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}
	
	// 结束包
	public int pkg_end(byte[] packet, int addr, int serial_no) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_END);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		intFill(cmd_stream, PKG_HEADER_SERIALNO_OFFSET, serial_no);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}

	// 数据包
	public int pkg_data(byte[] packet, int addr, int serial_no, int pkg_length) {
		int size;
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_DATA);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		intFill(cmd_stream, PKG_HEADER_SERIALNO_OFFSET, serial_no);
		size=(data_seek-(serial_no-1)*pkg_length);
		if (size>0){
			if (size>pkg_length) size=pkg_length;
			blockCopy(cmd_stream, PKG_HEADER_STRUCT_SIZE, data_stream, (serial_no-1)*pkg_length, size);
			return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE+size);
		}
		else{
			return 0;
		}
	}
	
	// 获取数据包数量
	public int get_pkg_count(int pkg_length){
		if ((data_seek%pkg_length)>0){
			return data_seek/pkg_length+1;
		}
		else{
			return data_seek/pkg_length;
		}
	}

	public int parse_trans_respond(byte[] packet, int size, int command, int serialno){
		size=Depack(respond_stream, packet, size);
		if (size>=RESPOND_STRUCT_SIZE && asShort(respond_stream, 0)==PKC_RESPOND &&
				asShort(respond_stream, PKG_HEADER_STRUCT_SIZE+RESPOND_COMMAND_OFFSET)==command){
			switch (command){
			case PKC_DATA:
				if (asShort(respond_stream, PKG_HEADER_STRUCT_SIZE+RESPOND_RESULT_OFFSET)==1)
				{
					if (serialno==asInt(respond_stream, PKG_HEADER_SERIALNO_OFFSET))
						return 1;
				}
				else
				{
					fix_serialno=asInt(respond_stream, PKG_HEADER_SERIALNO_OFFSET);
					return 2;
				}
				break;
			default:
				if (serialno==asInt(respond_stream, PKG_HEADER_SERIALNO_OFFSET))
					return 1;
				break;
			}
		}
		return 0;
	}

	public int parse_notify(byte[] packet, int size){
		size=Depack(respond_stream, packet, size);
		if (size>=PKG_HEADER_STRUCT_SIZE && asShort(respond_stream, PKG_HEADER_COMMAND_OFFSET)==PKC_NOTIFY){
			return asInt(respond_stream, PKG_HEADER_SERIALNO_OFFSET);
		}
		return 0;
	}

	// 复位控制卡节目显示
	public int pkg_reset_display(byte[] packet, int addr, int power) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_SET_POWER);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}

	// 读取电源状态
	public int pkg_get_power(byte[] packet, int addr) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_GET_POWER);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}
	
	// 设置电源状态，power=0关闭电源，power=1打开电源
	public int pkg_set_power(byte[] packet, int addr, int power) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_SET_POWER);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		intFill(cmd_stream, PKG_HEADER_SERIALNO_OFFSET, power);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}

	// 读取亮度
	public int pkg_get_bright(byte[] packet, int addr) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_GET_BRIGHT);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}

	// 设置亮度，bright=0到7
	public int pkg_set_bright(byte[] packet, int addr, int bright) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_SET_BRIGHT);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		intFill(cmd_stream, PKG_HEADER_SERIALNO_OFFSET, bright);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}

	// 校正时间
	public int pkg_adjust_time_ex(byte[] packet, int addr, int year, int month, int week, int day, int hour, int minute, int second) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_ADJUST_TIME);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		shortFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_YEAR_OFFSET, (short)year);
		shortFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_MONTH_OFFSET, (short)month);
		shortFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_WEEK_OFFSET, (short)week);
		shortFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_DAY_OFFSET, (short)day);
		shortFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_HOUR_OFFSET, (short)hour);
		shortFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_MINUTE_OFFSET, (short)minute);
		shortFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_SECOND_OFFSET, (short)second);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE+SYSTEMTIME_SIZE);
	}

	// 校正时间
	public int pkg_adjust_time(byte[] packet, int addr) {
		Calendar c = Calendar.getInstance();
		//12小时制
		//return pkg_adjust_time_ex(packet, addr, c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_WEEK)-1, 
		//		c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
		//24小时制
		return pkg_adjust_time_ex(packet, addr, c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_WEEK)-1, 
				c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
	}

	// 设置5V输出状态，value=0关闭5V输出，value=1开启5V输出
	public int pkg_set_switch(byte[] packet, int addr, int value) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_SET_SWITCH);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		intFill(cmd_stream, PKG_HEADER_SERIALNO_OFFSET, value);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}

	// 读取控制卡参数
	public int pkg_get_boardparam(byte[] packet, int addr) {
		get_boardparam_respond_size = 0;
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_GET_PARAM);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE);
	}
	
	// 设置控制卡参数_IP
	public int pkg_set_boardparam_ip(byte[] packet, int addr, byte[] ip) {
		int boart_param_size;
		if (get_boardparam_respond_size>0)
		{
			boart_param_size=get_boardparam_respond_size-RESPOND_STRUCT_SIZE;
			get_boardparam_respond_size = 0;
			blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE+boart_param_size, (byte)0);
			shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_SET_PARAM);
			byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
			blockCopy(cmd_stream, PKG_HEADER_STRUCT_SIZE, respond_stream, RESPOND_STRUCT_SIZE, boart_param_size);
			blockCopy(cmd_stream, PKG_HEADER_STRUCT_SIZE + BOARDPARAM_IP_OFFSET, ip, 0, 4);
			return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE+boart_param_size);
		}
		return 0;
	}
	
	// 设置定时开关屏计划  -- 按一周7天每日定时开关屏，每日可以定义3个时段
	public int pkg_power_schedule_weekday(byte[] packet, int addr, boolean enabled) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_SET_POWER_SCHEDULE);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		if (enabled){
			intFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_ENABLED_OFFSET, 0x55AAAA55);
		}else{
			intFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_ENABLED_OFFSET, 0);
		}
		intFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_MODE_OFFSET, 0);

		//周日，3个开关屏时段，周1到周6同理
		//   时段1
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SUNDAY_OPENTIME_OFFSET, 2015, 9, 30, 9, 0, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SUNDAY_CLOSETIME_OFFSET, 2015, 9, 30, 12, 0, 0);
		//   时段2
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SUNDAY_OPENTIME_OFFSET+8, 2015, 9, 30, 13, 30, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SUNDAY_CLOSETIME_OFFSET+8, 2015, 9, 30, 18, 22, 0);
		//   时段3
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SUNDAY_OPENTIME_OFFSET+16, 2015, 9, 30, 18, 23, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SUNDAY_CLOSETIME_OFFSET+16, 2015, 9, 30, 21, 0, 0);
		//周一
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_MONDAY_OPENTIME_OFFSET, 2015, 9, 30, 8, 30, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_MONDAY_CLOSETIME_OFFSET, 2015, 9, 30, 17, 0, 0);
		//周二
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_TUESDAY_OPENTIME_OFFSET, 2015, 9, 30, 8, 30, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_TUESDAY_CLOSETIME_OFFSET, 2015, 9, 30, 17, 0, 0);
		//周三
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_WEDNESDAY_OPENTIME_OFFSET, 2015, 9, 30, 8, 30, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_WEDNESDAY_CLOSETIME_OFFSET, 2015, 9, 30, 17, 0, 0);
		//周四
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_THURSDAY_OPENTIME_OFFSET, 2015, 9, 30, 8, 30, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_THURSDAY_CLOSETIME_OFFSET, 2015, 9, 30, 17, 0, 0);
		//周五
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_FRIDAY_OPENTIME_OFFSET, 2015, 9, 30, 8, 30, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_FRIDAY_CLOSETIME_OFFSET, 2015, 9, 30, 17, 0, 0);
		//周六
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SATURDAY_OPENTIME_OFFSET, 2015, 9, 30, 9, 0, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SATURDAY_CLOSETIME_OFFSET, 2015, 9, 30, 12, 0, 0);
		
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SIZE);
	}

	// 设置定时开关屏计划  -- 按指定的起止日期时间播放，一共可以定义21个时段
	public int pkg_power_schedule_period(byte[] packet, int addr, boolean enabled) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_SET_POWER_SCHEDULE);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		if (enabled){
			intFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_ENABLED_OFFSET, 0x55AAAA55);
		}else{
			intFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_ENABLED_OFFSET, 0);
		}
		intFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_MODE_OFFSET, 1);

		//一共21个时段，每个时段偏移量间隔8个字节
		//下面例子为：五一开屏3天，六一、七一、八一各开屏1天
		//   时段1
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_OPENTIME_OFFSET, 2015, 5, 1, 0, 0, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SUNDAY_CLOSETIME_OFFSET, 2015, 5, 3, 23, 59, 59);
		//   时段2
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_OPENTIME_OFFSET+8, 2015, 6, 1, 0, 0, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_CLOSETIME_OFFSET+8, 2015, 6, 1, 23, 59, 59);
		//   时段3
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_OPENTIME_OFFSET+16, 2015, 7, 1, 0, 0, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_CLOSETIME_OFFSET+16, 2015, 7, 1, 23, 59, 59);
		//   时段4
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_OPENTIME_OFFSET+24, 2015, 8, 1, 0, 0, 0);
		timestampFill(cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_CLOSETIME_OFFSET+24, 2015, 8, 1, 23, 59, 59);
		//   ......
		
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE+PWOERSCHEDULE_SIZE);
	}
	
	// 串口转发
	public int pkg_com_transfer(byte[] packet, int addr, byte[] buffer, int size) {
		blockFill(cmd_stream, 0, PKG_HEADER_STRUCT_SIZE, (byte)0);
		shortFill(cmd_stream, PKG_HEADER_COMMAND_OFFSET, (short)PKC_COM_TRANSFER);
		byteFill(cmd_stream, PKG_HEADER_DSTADDR_OFFSET, (byte)addr);
		blockCopy(cmd_stream, PKG_HEADER_STRUCT_SIZE, buffer, 0, size);
		return Pack(packet, cmd_stream, PKG_HEADER_STRUCT_SIZE+size);
	}

	private void Encrypt(byte[] src, byte[] dst, int start, int size){
		int key=57943;
		int key1=36028;
		int key2=47926;
		int vsrc, vdst;
		int i;
		for (i=0; i<=((size>>1)-1); i++)
		{
			vsrc=asShort(src, (i+(start>>1))<<1);
			if (vsrc<0) vsrc=65536+vsrc;
			vdst=vsrc^key;
			shortFill(dst, (i+(start>>1))<<1, (short)vdst);
			key=key*key1+key2;
		}
	}
	
	private int DecryptToStream(String filename, byte[] stream, int start){
		InputStream in = null;
		int seek=start;
		int crc1, crc2;
		try {
			// 一次读多个字节
			byte[] buffer = new byte[130];
			int rsize = 0;
			in = new FileInputStream(filename);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((rsize = in.read(buffer)) != -1) {
				if (rsize!=0){
					crc1=CreateCRC(buffer, rsize-2);
					crc2=asShort(buffer, rsize - 2);
					if (crc2<0) crc2=65536+crc2;
					if (crc1==crc2){
						blockCopy(stream, seek, buffer, 0, rsize-2);
						seek+=(rsize-2);
					}
				}
			}
			Encrypt(stream, stream, start, seek-start);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
		return seek;
	}

	public void Upgrade(String filename) {
		root_seek=0;
		root_count=0;
		root_size=12;
		blockFill(data_stream, root_seek, root_size, (byte)0);
		shortFill(data_stream, root_seek, (short)0x13);
		root_size=DecryptToStream(filename, data_stream, root_size);
		intFill(data_stream, root_seek+ROOT_SIZE_OFFSET, root_size);
		data_seek=root_seek+root_size;
	}

	// 解析获取电源状态的应答
	public int parse_cmd_respond(byte[] packet, int size){
		size=Depack(respond_stream, packet, size);
		if (size>=RESPOND_STRUCT_SIZE && asShort(respond_stream, 0)==PKC_RESPOND){
			switch(asShort(respond_stream, PKG_HEADER_STRUCT_SIZE+RESPOND_COMMAND_OFFSET)){
			case PKC_GET_POWER:
				if (asInt(respond_stream, PKG_HEADER_SERIALNO_OFFSET)==0){
					System.out.println("读取电源状态完成，电源关闭");
				}else{
					System.out.println("读取电源状态完成，电源开启");
				}
				break;
			case PKC_SET_POWER:
				if (asInt(respond_stream, PKG_HEADER_SERIALNO_OFFSET)==0){
					System.out.println("设置电源状态完成，电源关闭");
				}else{
					System.out.println("设置电源状态完成，电源开启");
				}
				break;
			case PKC_GET_BRIGHT:
				System.out.println("读取亮度完成，亮度="+asInt(respond_stream, PKG_HEADER_SERIALNO_OFFSET));
				break;
			case PKC_SET_BRIGHT:
				System.out.println("设置亮度完成，亮度="+asInt(respond_stream, PKG_HEADER_SERIALNO_OFFSET));
				break;
			case PKC_ADJUST_TIME:
				System.out.println("校正时间完成");
				break;
			case PKC_SET_POWER_SCHEDULE:
				System.out.println("设置定时开关屏完成");
				break;
			case PKC_GET_PARAM:
				System.out.println("读取控制卡参数完成。" + 
						"  宽度="+asShort(respond_stream, RESPOND_STRUCT_SIZE+BOARDPARAM_WIDTH_OFFSET) +
						"  高度="+asShort(respond_stream, RESPOND_STRUCT_SIZE+BOARDPARAM_HEIGHT_OFFSET) +
						"  基色="+asShort(respond_stream, RESPOND_STRUCT_SIZE+BOARDPARAM_COLORMODE_OFFSET)
						);
				get_boardparam_respond_size=size;
				break;
			case PKC_SET_PARAM:
				System.out.println("设置控制卡参数完成");
				break;
			}
			return 1;
		}
		return 0;
	}

	public void Compress(){
		Lzss lzss = new Lzss();
		byte[] lzss_buf = new byte[root_size + 1024];
		int lzss_size;
		blockCopy(lzss_buf, 0, data_stream, 0, ROOT_STRUCT_SIZE);
		lzss_size = lzss.Encode(data_stream, ROOT_STRUCT_SIZE, root_size-ROOT_STRUCT_SIZE, lzss_buf, ROOT_STRUCT_SIZE);
		intFill(lzss_buf, ROOT_SIZE_OFFSET, lzss_size+ROOT_STRUCT_SIZE);
		shortFill(lzss_buf, ROOT_FLAG_OFFSET, (short)(asShort(lzss_buf, ROOT_FLAG_OFFSET) | 1));
		blockCopy(data_stream, 0, lzss_buf, 0, lzss_size+ROOT_STRUCT_SIZE);
		data_seek=lzss_size+ROOT_STRUCT_SIZE;
	}
	
	public int GetDataSize(){
		return data_seek;
	}

	public LEDSender2010() {
		data_stream = new byte[1<<24];
		cmd_stream = new byte[1024];
		respond_stream = new byte[1024];
		packet_stream = new byte[1280];
    }    
}