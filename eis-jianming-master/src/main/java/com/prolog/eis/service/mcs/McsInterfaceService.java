package com.prolog.eis.service.mcs;

import com.prolog.eis.model.mcs.MCSTask;
import com.prolog.eis.service.mcs.impl.McsTaskWithOutPathAsycDto;
import com.prolog.eis.service.mcs.impl.RecallMcsTaskDto;

import java.util.List;

public interface McsInterfaceService {
    void updateBuildEmptyContainerSupply(boolean exit,String rcsPositionCode);
     List<MCSTask> findFailMCSTask();
    public void updatesendMcsTaskWithOutPathAsyc(McsTaskWithOutPathAsycDto mcsTaskWithOutPathAsycDto, int type, String containerNo, String address, String target, String weight, String priority, int state );
    public void updateRecallMcsTask(RecallMcsTaskDto recallMcsTaskDto, MCSTask mcsTask);
}
