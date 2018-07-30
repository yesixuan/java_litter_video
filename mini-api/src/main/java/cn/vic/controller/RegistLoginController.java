package cn.vic.controller;

import cn.vic.pojo.Users;
import cn.vic.service.UserService;
import cn.vic.utils.IMoocJSONResult;
import cn.vic.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/regist")
    public IMoocJSONResult regist(@RequestBody Users user) throws Exception {
        // 1. 判空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名和密码不能为空");
        }
        // 2. 判断是否重复
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

        // 3. 保存用户
        if (!usernameIsExist) {
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } else {
            return IMoocJSONResult.errorMsg("用户名已存在");
        }
        return IMoocJSONResult.ok();
    }
}
