package com.prolog.eis.controller;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.AgvStorageLocationService;
import com.prolog.eis.service.enums.AgvMove;
import com.prolog.eis.service.impl.unbound.DefaultOutBoundPickCodeStrategy;
import com.prolog.eis.service.rcs.RcsRequestService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.service.test.TestService;
import com.prolog.eis.service.test.impl.SxStoreViewMapDto;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.utils.MapUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/master/view")
public class ViewTestController {

    @Autowired
    private QcInBoundTaskService qcInBoundTaskService;
    @Autowired
    private AgvStorageLocationService agvStorageLocationService;

    @Autowired
    private TestService testService;
    @Autowired
    private RcsRequestService rcsRequestService;

    @Autowired
    private PrologJmMCSController prologJmMCSController;

    @Autowired
    private DefaultOutBoundPickCodeStrategy defaultOutBoundPickCodeStrategy;
    @Autowired
    private ContainerTaskMapper containerTaskMapper;


    /**
     * 三楼入库口 调用链条机前进
     * @param
     * @return
     */
    @PostMapping("/inPut3")
    public Object layer3Inbound(@RequestParam("containerCode") String containerCode){
        try {
            AgvStorageLocation agvLocation = agvStorageLocationService.findByRcs("073040XY051410");
            qcInBoundTaskService.rcsCompleteForward(containerCode,agvLocation.getId());

            return MapUtils.put("result",true).put("msg","").getMap();
        } catch (Exception e) {
            return MapUtils.put("result",false).put("msg",e.getMessage()).getMap();
        }

    }

    /**
     * 四楼任务托入库口 调用链条机前进
     * @param
     * @return
     */
    @PostMapping("/inPut4Task")
    public Object layer4TaskInbound(@RequestParam("containerCode") String containerCode){
        try {
            AgvStorageLocation agvLocation = agvStorageLocationService.findByRcs("062960AB050000");
            qcInBoundTaskService.rcsCompleteForward(containerCode,agvLocation.getId());

            return MapUtils.put("result",true).put("msg","").getMap();
        } catch (Exception e) {
            return MapUtils.put("result",false).put("msg",e.getMessage()).getMap();
        }

    }

    /**
     * 四楼空托入库口 调用链条机前进
     * @param
     * @return
     */
    @PostMapping("/inPut4Empty")
    public Object layer4EmptyInbound(@RequestParam("containerCode") String containerCode){
        try {
            AgvStorageLocation agvLocation = agvStorageLocationService.findByRcs("060080AB054000");
            qcInBoundTaskService.rcsCompleteForward(containerCode,agvLocation.getId());
            return MapUtils.put("result",true).put("msg","").getMap();
        } catch (Exception e) {
            return MapUtils.put("result",false).put("msg",e.getMessage()).getMap();
        }

    }

    /**
     *
     * @param
     * @return
     */
    @PostMapping("/sxStoreLock")
    public Object sxStoreLock(@RequestParam("layer") Integer layer,@RequestParam("isLock") Integer isLock){
        try {
            testService.updateIsLockByLayer(isLock,layer);
            return MapUtils.put("result",true).put("msg","").getMap();
        } catch (Exception e) {
            return MapUtils.put("result",false).put("msg",e.getMessage()).getMap();
        }

    }

    @ApiOperation(value = "库存删除接口", notes = "库存删除接口")
    @PostMapping("/deletestore")
    public Object deleteStore(@RequestParam("containerNo")String containerNo, HttpServletResponse response) throws Exception {
        testService.deleteStore(containerNo);
        //删除containerTask任务
        ContainerTask containerTask= containerTaskMapper.queryContainerTaskByConcode(containerNo);
        containerTaskMapper.deleteById(containerTask.getId(),ContainerTask.class);
        return MapUtils.put("result",true).put("msg","").getMap();

    }


    //入库失败更新库存测试接口
    @PostMapping("/update")
    @ResponseBody
    public Object updateInBound(@RequestParam("id")String containerNo)throws Exception{

        return qcInBoundTaskService.rukuSxStoreUpdate(containerNo);
    }

    //入库失败更新库存测试接口
    @PostMapping("/updateByP")
    @ResponseBody
    public Object updateInBound(@RequestParam("layer")int layer,@RequestParam("x")int x,@RequestParam("y")int y)throws Exception{
        String containerNo= testService.getSxStoreContainerNo(layer,x,y);
        return qcInBoundTaskService.rukuSxStoreUpdate(containerNo);
    }

    //调用agv前进
    @PostMapping("/agvMove")
    @ResponseBody
    public Object agvMove(@RequestParam("startP")String startP,@RequestParam("endP")String endP)throws Exception{
        String containerCode= PrologStringUtils.newGUID();
        String taskCode= PrologStringUtils.newGUID()+AgvMove.agvMoveTaskCodeEndPrex; //手动调用Agv搬运 taskCode暂时定为prolog PrologRcsController回告已经修改
        return rcsRequestService.sendTask(taskCode, containerCode, startP, endP, "F01", "1");
    }


    //碟盘机入库失败给前进指令
    @PostMapping("/foldInBoundMove")
    @ResponseBody
    public void foldInBoundMove(@RequestParam("deviceNo") String deviceNo,@RequestParam("containerNo") String containerNo, HttpServletResponse response)throws Exception{
       Map map=MapUtils.put("deviceNo",deviceNo).put("containerNo",containerNo).getMap();
       String json= JSONObject.toJSONString(map);
        prologJmMCSController.foldInBound(json,  response);
    }

    //查询所有可用拣选站
    @PostMapping("/queryPickStation")
    @ResponseBody
    public Object queryPickStation()throws Exception{
       return defaultOutBoundPickCodeStrategy.getAvailablePickStation();
    }

    //查询指定layer库存
    @PostMapping("/getSxStoreViewDto")
    @ResponseBody
    public Object getSxStoreViewDto(@RequestParam("layer") Integer layer){
        return testService.getSxStoreViewDto(layer);
    }

    //查询指定layer库存
    @PostMapping("/getSxStoreViewDtoSimpleDto")
    @ResponseBody
    public Object getSxStoreViewDtoSimpleDto(@RequestParam("layer") Integer layer){
        return testService.getSxStoreViewDtoSimpleDto(layer);
    }
    //查询指定layer库存监控
    @PostMapping("/getSxStoreViewMap")
    @ResponseBody
    public Object getSxStoreViewMapDtoByLayer(@RequestParam("layer") Integer layer){
        SxStoreViewMapDto sxStoreViewMapDto=testService.getSxStoreViewMapDtoByLayer(layer);
           return sxStoreViewMapDto;
    }

}
