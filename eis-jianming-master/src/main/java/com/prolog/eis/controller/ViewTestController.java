package com.prolog.eis.controller;

import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.JsonResult;
import com.prolog.eis.service.AgvStorageLocationService;
import com.prolog.eis.service.enums.AgvMove;
import com.prolog.eis.service.rcs.RcsRequestService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.service.test.TestService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.utils.MapUtils;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
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
        return MapUtils.put("result",true).put("msg","").getMap();

    }


    //入库失败更新库存测试接口
    @PostMapping("/update")
    @ResponseBody
    public Object updateInBound(@RequestParam("id")String containerNo)throws Exception{

        return qcInBoundTaskService.rukuSxStoreUpdate(containerNo);
    }

    //调用agv前进
    @PostMapping("/agvMove")
    @ResponseBody
    public Object agvMove(@RequestParam("startP")String startP,@RequestParam("endP")String endP)throws Exception{
        String containerCode= PrologStringUtils.newGUID();
        String taskCode= AgvMove.agvMoveTaskCode; //手动调用Agv搬运 taskCode暂时定为prolog PrologRcsController回告已经修改
        return rcsRequestService.sendTask(taskCode, containerCode, startP, endP, "F01", "1");
    }

}
