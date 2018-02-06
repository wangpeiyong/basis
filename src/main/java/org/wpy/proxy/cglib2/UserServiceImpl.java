package org.wpy.proxy.cglib2;

import com.alibaba.fastjson.JSON;
import org.wpy.utils.User;

public class UserServiceImpl {


    public void add(User user) {
        System.out.println("保存到database： " + JSON.toJSONString(user));
    }
}
