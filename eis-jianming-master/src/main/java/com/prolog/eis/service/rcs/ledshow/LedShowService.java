package com.prolog.eis.service.rcs.ledshow;

import com.prolog.eis.controller.led.PrologLedViewService;
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
import com.prolog.eis.service.impl.unbound.DivideAndRemainderToFloat;
import com.prolog.eis.util.PrologCoordinateUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Aspect
public class LedShowService {


    @Autowired
    private ContainerTaskService containerTaskService;
    @Autowired
    private LedShowDto ledShowDto;
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
    @Resource
    PrologLedViewService prologLedViewService;


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
     * ??????LED??????
     * @param taskCode
     * @param method
     */
    @Before("doAction(taskCode,method)")
    public void pickStationShow(String taskCode, String method) {
        //??????????????? ?????? ????????????
        List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode(taskCode);
        if (StringUtils.isEmpty(containerTasks)) {return;}
        if (containerTasks != null && containerTasks.size() == 0)  {return;}
        ContainerTask containerTask = containerTasks.get(0);
        if (containerTask.getTargetType() == 1) {
            BigDecimal pQty = containerTaskDetailMapper.queryPickQtyByConcode(containerTask.getContainerCode());
            if (pQty == null) {
                pQty =BigDecimal.ZERO;
            }
            BigDecimal rQty= DivideAndRemainderToFloat.subtract(containerTask.getQty(),pQty);
            Map<String, LedShowDto> mapLedShows = ledShowDto.getLedShowDtoMap();
            if (mapLedShows.containsKey(containerTask.getTarget())) {
                LedShowDto ledShowDtoS = mapLedShows.get(containerTask.getTarget());
                LedShow ledShow = ledShowMapper.findById(ledShowDtoS.getId(), LedShow.class);
                if (ledShow != null) {
                     try {
                        prologLedViewService.pick(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName()==null?"":containerTask.getItemName(), pQty.divide(new BigDecimal("1000")), containerTask.getLot(),  rQty.divide(new BigDecimal("1000")), ledShowDtoS.getPickStation());
                    } catch (Exception e) {
                        LogServices.logSys(e);
                    }
                }
            }
        }
    }

    /**
     * ?????????led??????
     * @param containerCode
     * @param targetLayer
     * @param targetX
     * @param targetY
     * @param address
     * @throws Exception
     */
    @After("container( containerCode,targetLayer,targetX,targetY,address)")
    public void chukuLedShow(String containerCode, int targetLayer, int targetX, int targetY, String address) throws Exception {
        ContainerTask containerTask = containerTaskMapper.queryContainerTaskByConcode(containerCode);
        if(containerTask==null) {return ;}
        String station = agvStorageLocationMapper.queryPickStationByCode(containerTask.getTarget());
        LedShow ledShow = ledShowMapper.findById(3, LedShow.class);

        if (ledShow != null) {
             try {
                prologLedViewService.outStore(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName()==null?"":containerTask.getItemName(), containerTask.getQty().divide(new BigDecimal("1000")), containerTask.getLot(), station);
            } catch (Exception e) {
                LogServices.logSys(e);
            }

        }
    }

    /**
     * ?????????Led??????
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
        //????????????
        //????????????
        Double weight = 0d;
        if (!com.prolog.framework.utils.StringUtils.isEmpty(inBoundRequest.getWeight())) {
            weight = Double.valueOf(inBoundRequest.getWeight()) / 10.00;
        }

        String state = "";
        if (weight >= limitWeight) {
            state = "??????";
        } else if (detection != 1) {
            state = "????????????";
        } else {
            state = "??????";
        }
        //???????????????????????????????????????
        ContainerTask containerTask = containerTaskMapper.queryContainerTaskByConcode(containerNo);
        if(containerTask==null) {return;}
        //??????,????????????led???
        if (sourceLayer == 1||sourceLayer==2) {
            LedShow ledShow = ledShowMapper.findById(sourceLayer, LedShow.class);

            if (ledShow != null) {
                 try {
                    prologLedViewService.reStore(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName()==null?"":containerTask.getItemName(), weight, containerTask.getLot(), state);
                } catch (Exception e) {
                    LogServices.logSys(e);
                }
            }
        }


    }


}
