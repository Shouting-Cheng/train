package com.hand.hcf.app.train.utils;

public interface RespCode {
    /**
     * 成功
     */
    String RES_0000 = "0000";

    /** 参数{0}格式错误: {1} */
    String RES_0010 = "0010";
    /** 参数{0}必须为有效的枚举值 */
    String RES_0011 = "0011";
    /** 参数{0}不能为空 */
    String RES_0012 = "0012";
    /** 参数{0}取值不在范围内 */
    String RES_0013 = "0013";

    /** 请求速度过快! */
    String RES_9960 = "9960";
    /** 您无权请求该资源! */
    String RES_1000 = "1000";

    /*通用*/
    String COLUMN_SHOULD_BE_EMPTY = "00001"; // 列应该为空
    String COLUMN_SHOULD_NOT_BE_EMPTY = "00002"; // 列不应该为空
    String DATASOURCE_CANNOT_FIND_OBJECT = "00003"; //数据库找不到对象
    String DATA_EXISTS = "00004";  //数据已存在
    String DATA_NOT_EXISTS = "00005";  //数据不存在
    String ID_NULL="00006";// id 不应该为空!
    String ID_NOT_NULL="00007";// id 应该为空!
    String DATA_ASSIGN="00008";  //已分配该数据
    String STRING_IS_NULL="00009"; //格式化string为空
    String DATE_FORMAT_NOT_TRUE="00010";//格式不正确
    String OPERATION_DATA_NOT_BE_EMPTY = "00011";  //参与运算的数据不能为空
    String ATTACHMENT_TYPE_NOT_NULL = "00012";//附件类型为空
    String VERSION_NUMBER_CHANGED = "00013";   //版本号不一致
    String READ_FILE_FAILED = "00014"; //读取文件失败
    String PERIOD_NOT_FOUND = "00015";//获取期间详细信息失败
    /**用户相关*/
    String TENANT_MESSAGE_NOT_EXISTS = "00101";   //租户信息为空
    String USER_MESSAGE_NOT_EXISTS = "00102";    //用户信息为空
    String COMPANY_MESSAGE_NOT_EXISTS = "00103";     //公司信息为空
    String EMPLOYEE_MESSAGE_NOT_EXISTS = "00104";     //员工信息为空

}
