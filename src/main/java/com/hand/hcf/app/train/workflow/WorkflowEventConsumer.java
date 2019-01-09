package com.hand.hcf.app.train.workflow;

/**
 * Created by maohui.zhuang@hand-china.com on 2018/12/24.
 * 用于监听工作流的事件，主要是用于工作流审批后，相应的更新单据的状态
 */

import com.codingapi.tx.annotation.TxTransaction;
import com.hand.hcf.app.train.enums.DocumentOperationEnum;
import com.hand.hcf.app.client.workflow.dto.WorkflowMessageCO;
import com.hand.hcf.app.client.workflow.event.WorkflowCustomRemoteEvent;
import com.hand.hcf.app.client.workflow.event.WorkflowEventConsumerInterface;
import com.hand.hcf.core.security.domain.PrincipalLite;
import com.hand.hcf.core.util.LoginInformationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WorkflowEventConsumer implements WorkflowEventConsumerInterface {
    @Value("${spring.application.name:}")
    private String applicationName;
    private static final Logger logger = LoggerFactory.getLogger(WorkflowEventConsumer.class);

    /**
     * 该监听用于 工作流的撤回，审批拒绝(驳回)，审批通过 时 修改单据的相应状态
     *
     * @param workflowCustomRemoteEvent
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public void workFlowConsumer(WorkflowCustomRemoteEvent workflowCustomRemoteEvent) {
        logger.info("预算接收到工作流事件消息：" + workflowCustomRemoteEvent);

        WorkflowMessageCO workflowMessage = workflowCustomRemoteEvent.getWorkflowMessage();
        PrincipalLite userBean = workflowMessage.getUserBean();
        LoginInformationUtil.setAuthentication(userBean);
        //增加一层判断，只有目标服务为自己的服务时，且状态 为审批通过，撤回，驳回 时 才处理
        if ((applicationName + ":**").equalsIgnoreCase(workflowCustomRemoteEvent.getDestinationService()) && DocumentOperationEnum.APPROVAL.getId().compareTo(workflowMessage.getStatus()) <= 0) {
            //更新单据的状态为审批通过/撤回/驳回
        }
    }

}
