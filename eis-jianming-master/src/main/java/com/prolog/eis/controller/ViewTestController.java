package com.prolog.eis.controller;

import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.JsonResult;
import com.prolog.eis.service.AgvStorageLocationService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.service.test.TestService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.utils.MapUtils;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
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
    public void deleteStore(@RequestParam("containerNo")String containerNo, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        OutputStream out = response.getOutputStream();
        try {
            testService.deleteStore(containerNo);
            JSONObject jsonObject = new JSONObject();
            out.write(jsonObject.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (Exception e) {
            out.write(e.getMessage().getBytes("UTF-8"));
            out.flush();
            out.close();
        }
    }


    //入库失败更新库存测试接口
    @PostMapping("/update")
    @ResponseBody
    public String updateInBound(@RequestParam("id")String containerNo)throws Exception{

        return PrologApiJsonHelper.toJson(qcInBoundTaskService.rukuSxStoreUpdate(containerNo));
    }

}
