package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import java.util.Map;

import static com.hmdp.constant.LoginConst.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result sendCode(String phone, HttpSession session) {
        // 1. 校验手机号
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phone);
        if (phoneInvalid) {
            return Result.fail("手机号格式不正确");
        }
        // 2. 生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 3. 保存验证码到session
        log.info("手机号：{}，验证码：{}", phone, code);
        session.setAttribute(LOGIN_CODE, code);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        String phone = loginForm.getPhone();
        // 1. 校验手机号
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phone);
        if (phoneInvalid) {
            return Result.fail("手机号格式不正确");
        }
        // 2. 校验验证码
        String code = loginForm.getCode();
        String sessionCode = (String) session.getAttribute(LOGIN_CODE);
        if (!code.equals(sessionCode)) {
            return Result.fail("验证码不正确");
        }
        log.info("手机号：{}，验证码：{}", phone, code);
        // 3. 根据手机号查询用户
        User user = this.query().eq("phone", phone).one();
        if (user == null) {
            // 4. 如果用户不存在，就创建一个用户
            user = new User();
            user.setPhone(phone);
            user.setNickName(NICKNAME_PREFIX + RandomUtil.randomNumbers(6));
            this.save(user);
        }
        // 5. 保存用户信息到session
        session.setAttribute(USER_SESSION, user);
        session.setMaxInactiveInterval(60 * 60 * 24 * 7);
        return Result.ok(user);
    }

}
