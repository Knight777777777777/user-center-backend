package com.lcc.ucbackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcc.ucbackend.common.BaseResponse;
import com.lcc.ucbackend.common.ErrorCode;
import com.lcc.ucbackend.common.ResultUtils;
import com.lcc.ucbackend.exception.BusinessException;
import com.lcc.ucbackend.model.domain.User;
import com.lcc.ucbackend.model.domain.request.UserLoginRequest;
import com.lcc.ucbackend.model.domain.request.UserRegisterRequest;
import com.lcc.ucbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.lcc.ucbackend.constant.userConstant.ADMIN_ROLE;
import static com.lcc.ucbackend.constant.userConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    public UserService userService;

    @PostMapping("/register")
    public BaseResponse userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);

        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
           throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户或密码为空");
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求为空");
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
           throw new BusinessException(ErrorCode.NULL_ERROR,"当前用户为空");
        }
        long userId = currentUser.getId();
        //todo 检验用户是否合法
        User user = userService.getById(userId);
        User result = userService.getSafeUser(user);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> result = userList.stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非法id");
        }
        Boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //仅管理员查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


}
