
package com.hand.hcf.app.train.enums;


import com.hand.hcf.core.enums.SysEnum;

/**
 * @description: 单据操作状态 (混用历史操作类型)
 * @version: 1.0
 * @author: wenzhou.tang@hand-china.com
 * @date: 2018/1/24 14:43
 */

public enum DocumentOperationEnum implements SysEnum {
    GENERATE(1001)//编辑中
    , APPROVAL(1002)//审批中
    , WITHDRAW(1003) // 撤回
    , APPROVAL_PASS(1004) // 审批通过
    , APPROVAL_REJECT(1005) // 审批驳回
    , AUDIT_PASS(1006) // 审核通过
    , AUDIT_REJECT(1007) // 审核驳回
    //对公报销单 2001

    //预付款申请单 3001

    //预算日记账 5001
    ,POSTED(5001)//复核(过账)
    ,BACKLASH_SUBMIT(5002)//:反冲提交
    ,BACKLASH_CHECKED(5003)//:反冲审核
    //合同  6001
    ,HOLD(6001)//暂挂中
    ,CANCEL(6002)//已取消
    ,FINISH(6003)//已完成
    // 记录操作历史加的
    ,NOT_HOLD(6004) //取消暂挂
    //付款申请单 9001
    ,PAYMENT(9001)// 支付  支付模块的状态
    ,RETURN(9002)// 退款   支付模块的状态
    ,REFUND(9003)// 退票   支付模块的状态
    ,RESERVED(9004)// 反冲  支付模块的状态

    ;
    private Integer id;

    DocumentOperationEnum(Integer id) {
        this.id = id;
    }

    public static DocumentOperationEnum parse(Integer id) {
        for (DocumentOperationEnum fieldType : DocumentOperationEnum.values()) {
            if (fieldType.getId().equals(id)) {
                return fieldType;
            }
        }
        return null;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

}

