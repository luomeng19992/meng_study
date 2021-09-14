package com.breakout.meng.service.impl;

import com.breakout.meng.common.api.CommonResult;
import com.breakout.meng.common.utils.JwtTokenUtil;
import com.breakout.meng.dao.UmsAdminRoleRelationDao;
import com.breakout.meng.mgb.mapper.UmsAdminMapper;
import com.breakout.meng.mgb.model.UmsAdmin;
import com.breakout.meng.mgb.model.UmsAdminExample;
import com.breakout.meng.mgb.model.UmsPermission;
import com.breakout.meng.service.MeberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

@Service
@ConfigurationProperties(prefix = "authcode")
public class MeberServiceImpl implements MeberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeberServiceImpl.class);
    private String prifix;
    private Long expire;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UmsAdminRoleRelationDao umsAdminRoleRelationDao;
    @Autowired
    MeberService meberService;
    @Autowired
    UmsAdminMapper umsAdminMapper;
    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringJoiner stringJoiner=new StringJoiner("","","");
        SecureRandom secureRandom=new SecureRandom();
        for (int i = 0; i < 6; i++) {
            stringJoiner.add(secureRandom.nextInt(10)+"");
        }
        redisTemplate.opsForValue().set(prifix+telephone,stringJoiner.toString());
        redisTemplate.expire(prifix+telephone,expire, TimeUnit.SECONDS);
        return CommonResult.success(stringJoiner.toString());
    }

    @Override
    public CommonResult verifyAuthCode(String telephone, String authCode) {
        if (StringUtils.isEmpty(authCode)){
            return CommonResult.failed("请输入验证码");
        }
        String s = (String) redisTemplate.opsForValue().get(prifix + telephone);
        if (authCode.endsWith(s)){
            redisTemplate.delete(prifix + telephone);
            return CommonResult.success("ok");
        }else {
            return CommonResult.failed("验证码错误");
        }
    }


    @Override
    public List<UmsPermission> getPermissionList(Long adminId) {
        return umsAdminRoleRelationDao.getPermissionList(adminId);
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdmin umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = umsAdminMapper.selectByExample(example);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        umsAdminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }


    public String getPrifix() {
        return prifix;
    }

    public void setPrifix(String prifix) {
        this.prifix = prifix;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
