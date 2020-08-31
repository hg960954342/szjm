package com.prolog.eis.controller;

import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.JsonResult;
import com.prolog.eis.service.AgvStorageLocationService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.service.test.TestService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param json
     * @return
     */
    @PostMapping("/inPut3")
    public Object layer3Inbound(@RequestBody String json){
        try {
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
            String containerCode = helper.getString("containerCode");
            AgvStorageLocation agvLocation = agvStorageLocationService.findByRcs("073040XY051410");
            qcInBoundTaskService.rcsCompleteForward(containerCode,agvLocation.getId());

            return MapUtils.put("result",true).put("msg","").getMap();
        } catch (Exception e) {
            return MapUtils.put("result",false).put("msg",e.getMessage()).getMap();
        }

    }

    /**
     * 四楼任务托入库口 调用链条机前进
     * @param json
     * @return
     */
    @PostMapping("/inPut4Task")
    public Object layer4TaskInbound(@RequestBody String json){
        try {
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
            String containerCode = helper.getString("containerCode");
            AgvStorageLocation agvLocation = agvStorageLocationService.findByRcs("062960AB050000");
            qcInBoundTaskService.rcsCompleteForward(containerCode,agvLocation.getId());

            return MapUtils.put("result",true).put("msg","").getMap();
        } catch (Exception e) {
            return MapUtils.put("result",false).put("msg",e.getMessage()).getMap();
        }

    }

    /**
     * 四楼空托入库口 调用链条机前进
     * @param json
     * @return
     */
    @PostMapping("/inPut4Empty")
    public Object layer4EmptyInbound(@RequestBody String json){
        try {
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
            String containerCode = helper.getString("containerCode");
            AgvStorageLocation agvLocation = agvStorageLocationService.findByRcs("060080AB054000");
            qcInBoundTaskService.rcsCompleteForward(containerCode,agvLocation.getId());
            return MapUtils.put("result",true).put("msg","").getMap();
        } catch (Exception e) {
            return MapUtils.put("result",false).put("msg",e.getMessage()).getMap();
        }

    }

    /**
     *
     * @param json
     * @return
     */
    @PostMapping("/sxStoreLock")
    public Object sxStoreLock(@RequestBody String json){
        try {
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
            Integer layer = Integer.parseInt(helper.getString("layer"));
            Integer isLock = Integer.parseInt(helper.getString("isLock"));
            testService.updateIsLockByLayer(isLock,layer);
            return MapUtils.put("result",true).put("msg","").getMap();
        } catch (Exception e) {
            return MapUtils.put("result",false).put("msg",e.getMessage()).getMap();
        }

    }

}
