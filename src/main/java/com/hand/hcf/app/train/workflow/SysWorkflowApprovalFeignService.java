package com.hand.hcf.app.train.workflow;

import com.hand.hcf.app.client.workflow.WorkflowInterface;
import com.hand.hcf.app.client.workflow.dto.WorkFlowDocumentRefCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by maohui.zhuang@hand-china.com on 2018/12/24.
 */
@Service
public class SysWorkflowApprovalFeignService {
    @Autowired
    private WorkflowInterface workflowInterface;

    public String submitWorkflow(WorkFlowDocumentRefCO workFlowDocumentRef) {
        return workflowInterface.submitWorkflow(workFlowDocumentRef);
    }
}
