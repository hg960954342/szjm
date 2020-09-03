package com.prolog.eis.service.rcs.ledshow;

import com.prolog.eis.controller.led.PrologLedController;
import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.base.SysParameMapper;
import com.prolog.eis.dao.led.LedShowMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.dto.eis.mcs.InBoundRequest;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.base.SysParame;
import com.prolog.eis.model.led.LedShow;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.util.PrologCoordinateUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("all")
@Aspect
public class LedShowService {


    @Autowired
    private ContainerTaskService containerTaskService;
    @Autowired
    private LedShowDto LedShowDto;
    @Autowired
    private LedShowMapper ledShowMapper;
    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapper;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    ContainerTaskMapper containerTaskMapper;
    @Autowired
    SysParameMapper sysParameMapper;


    @Pointcut("execution(* com.prolog.eis.service.store.impl.MCSCallBackChuKu.container(String,int,int,int,String))&&args(containerCode,targetLayer,targetX,targetY,address)")
    public void container(String containerCode, int targetLayer, int targetX, int targetY, String address) throws Exception {
    }


    @Pointcut("execution(* com.prolog.eis.service.rcs.impl.EndMethod.doAction(String,String ))&&args(taskCode,method)")
    public void doAction(String taskCode, String method) {
    }

    @Pointcut("execution(* com.prolog.eis.service.store.impl.QcInBoundTaskServiceImpl.inBoundTask(..))&&args(inBoundRequest)")
    public void inBoundTask(InBoundRequest inBoundRequest) throws Exception {
    }

    /**
     * 站台LED显示
     * @param taskCode
     * @param method
     */
    @Before("doAction(taskCode,method)")
    public void pickStationShow(String taskCode, String method) {
        //根据任务号 查询 托盘任务
        List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode(taskCode);
        if (StringUtils.isEmpty(containerTasks)) return;
        if (containerTasks != null && containerTasks.size() == 0) return;
        ContainerTask containerTask = containerTasks.get(0);
        if (containerTask.getTargetType() == 1) {
            Double pQty = containerTaskDetailMapper.queryPickQtyByConcode(containerTask.getContainerCode());
            if (pQty == null) {
                pQty = 0.0;
            }
            double rQty = containerTask.getQty() - pQty;
            Map<String, LedShowDto> mapLedShows = LedShowDto.getLedShowDtoMap();
            if (mapLedShows.containsKey(containerTask.getSource())) {
                LedShowDto ledShowDtoS = mapLedShows.get(containerTask.getSource());
                LedShow ledShow = ledShowMapper.findById(ledShowDtoS.getId(), LedShow.class);
                if (ledShow != null) {
                    PrologLedController prologLedController = new PrologLedController();
                    try {
                        prologLedController.pick(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName(), pQty, containerTask.getLotId(), rQty, ledShowDtoS.getPickStation());
                    } catch (Exception e) {
                        LogServices.logSys(e);
                    }
                }
            }
        }
    }

    /**
     * 出库口led显示
     * @param containerCode
     * @param targetLayer
     * @param targetX
     * @param targetY
     * @param address
     * @throws Exception
     */
    @After("container( containerCode,targetLayer,targetX,targetY,address)")
    public void chukuLedShow(String containerCode, int targetLayer, int targetX, int targetY, String address) throws Exception {
        ContainerTask containerTask = containerTaskMapper.selectStartTaskByContainerCode(containerCode);
        String station = agvStorageLocationMapper.queryPickStationByCode(containerTask.getTarget());
        LedShow ledShow = ledShowMapper.findById(3, LedShow.class);

        if (ledShow != null) {
            PrologLedController prologLedController = new PrologLedController();
            try {
                prologLedController.outStore(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName(), containerTask.getQty(), containerTask.getLotId(), station);
            } catch (Exception e) {
                LogServices.logSys(e);
            }

        }
    }

    /**
     * 入库口Led显示
     * @param inBoundRequest
     */
    @Before("inBoundTask(inBoundRequest)")
    public void rukuLedShow(InBoundRequest inBoundRequest) {

        String containerNo = inBoundRequest.getStockId();
        String source = inBoundRequest.getSource();
        Coordinate coordinate = PrologCoordinateUtils.analysis(source);
        int sourceLayer = coordinate.getLayer();
        int sourceX = coordinate.getX();
        int sourceY = coordinate.getY();
        int detection = inBoundRequest.getDetection();

        SysParame sysParame = sysParameMapper.findById("LIMIT_WEIGHT", SysParame.class);
        Double limitWeight = Double.valueOf(sysParame.getParameValue());
        //校验重量
        //验证超重
        Double weight = 0d;
        if (!com.prolog.framework.utils.StringUtils.isEmpty(inBoundRequest.getWeight())) {
            weight = Double.valueOf(inBoundRequest.getWeight()) / 10.00;
        }

        String state = "";
        if (weight >= limitWeight) {
            state = "超重";
        } else if (detection != 1) {
            state = "尺寸异常";
        } else {
            state = "正常";
        }
        //根据托盘码查询入库托盘任务
        ContainerTask containerTask = containerTaskMapper.queryContainerTaskByConcode(containerNo);
        //入库,回库显示led屏
        if (sourceLayer == 1) {
            LedShow ledShow = ledShowMapper.findById(1, LedShow.class);

            if (ledShow != null) {
                PrologLedController prologLedController = new PrologLedController();
                try {
                    prologLedController.reStore(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName(), weight, containerTask.getLotId(), state);
                } catch (Exception e) {
                    LogServices.logSys(e);
                }
            }
        }
        if (sourceLayer == 2) {
            LedShow ledShow = ledShowMapper.findById(2, LedShow.class);

            if (ledShow != null) {
                PrologLedController prologLedController = new PrologLedController();
                try {
                    prologLedController.reStore(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName(), weight, containerTask.getLotId(), state);
                } catch (Exception e) {
                    LogServices.logSys(e);
                }

            }
        }

    }


}
