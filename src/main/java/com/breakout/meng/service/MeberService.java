package com.breakout.meng.service;

import com.breakout.meng.common.api.CommonResult;
import com.breakout.meng.mgb.model.UmsAdmin;
import com.breakout.meng.mgb.model.UmsAdminExample;
import com.breakout.meng.mgb.model.UmsPermission;

import java.util.List;

public interface MeberService {
    /**
     * 生成验证码
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 判断验证码和手机号码是否匹配
     */
    CommonResult verifyAuthCode(String telephone, String authCode);



    public List<UmsPermission> getPermissionList(Long adminId);

    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     */
    UmsAdmin register(UmsAdmin umsAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

}
