# OpenResty-Learning
## 1、学习资料

> * [OpenResty最佳实践](https://www.gitbook.com/book/moonbingbing/openresty-best-practices)
> * [OpenResty官方文档](https://github.com/openresty/lua-nginx-module)



## 2、明日学习任务
 
>   * ngx lua helloworld
>   * http 解析返回cookie
>   * lua  redis  MySQL
>   * ngx cache  和 openresty cache
>   * openrestry 最佳实践
>   * shareddict 和 openrestry lrucache 优劣
>   * ngx lua openrestry  文档
>   * openrestry 最佳实践 有Windows版的
>   * shareddict 和 openrestry lrucache 优劣
>   * 缓存失效风暴
>       * 并发过大，同时几万个并发请求获取缓存。打爆数据库。考虑使用lua_resty_lock 加锁实现。



## 3、缓存失效风暴

**解释：** 并发过大，同时几万个并发请求获取缓存。打爆数据库。

**解决方案:** 考虑使用lua_resty_lock 加锁实现。



## 4、配置文件

```text
		worker_processes  1;        #nginx worker 数量
        error_log logs/error.log info;   #设置log的级别为info
        
        events {
            worker_connections 1024;
        }
        
        http {
            server {
                #监听端口，若你的6699端口已经被占用，则需要修改
                listen 80;
        		lua_code_cache off;
        		charset utf-8;
                location / {
                    default_type text/html;
        
                    content_by_lua_block {
                        ngx.say("HelloWorld")
                    }
                }
        		
        		 location ~ ^/app/([-_a-zA-Z0-9/]+) {
        			set $path $1;
        			content_by_lua_file lua/app/$path.lua;
        		}
        		
        		location ~ ^/home/* {
                    default_type text/html;
        
                    content_by_lua_block {
                        ngx.say(" home")
                    }
                }
        		
        		
        		location ~ ^/auth/* {
        			access_by_lua_file lua/app/auth_filter.lua;
        			proxy_pass http://www.ttlsa.com;
        		}
        		
        		location ~ ^/rewrite/* {
        			
        			rewrite_by_lua_file lua/app/rewrite_filter.lua;
        			content_by_lua_block {
                        ngx.say(" rewrite ")
                    }
        		}
        		
        		
        		location = /t {
                    content_by_lua '
                        require("my-lrucache").go()
                    ';
                }
        		
            }
        }
```

## 5、缓存shared_dict、LRUCache 优劣：

* **shared_dict：** 

        1、多个worker之间共享的变量。
        2、非线程安全，多worker 必须加锁。 
        3、内存共享，更省内存。
        
* **LRUCache：** 

        1、worker私有的内存，线程安全的。
        2、LRU 最近使用size个元素
        3、无锁更快速。
        

## 6、redis + lua 

### 6.1、实现原子操作的分布式锁

lua代码：
```lua
--- 分布式锁实现
    -- call 函数直接终止lua命令    expire key second
    local empty = redis.call("setnx",KEYS[1],"distributed_lock") if tonumber(empty) > 0 then redis.call("expire",KEYS[1],ARGV[1]) return 1 end return 0

```

JAVA客户端的调用:
```java

    
    import com.alibaba.fastjson.JSONObject;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.data.redis.core.BoundValueOperations;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.data.redis.core.StringRedisTemplate;
    import org.springframework.data.redis.core.script.DefaultRedisScript;
    import org.springframework.test.context.junit4.SpringRunner;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Random;
    import java.util.concurrent.TimeUnit;
    
    import static org.junit.Assert.*;
    
    
    @RunWith(SpringRunner.class)
    @SpringBootTest
    public class RedisConfigurationTest {
    
        @Autowired
        private RedisTemplate<String, Object> redisTemplate;
    
    
        @Test
        public void main() throws InterruptedException {
            Random random = new Random();
            List<String> keys = Arrays.asList("lock");
            String lua = "local empty = redis.call(\"setnx\",KEYS[1],\"distributed_lock\") if tonumber(empty) > 0 then redis.call(\"expire\",KEYS[1],ARGV[1]) return 1 end return 0";
            for (int i = 0; i < 100; i++) {
                Thread.sleep(random.nextInt(20) * 1000L);
                Object execute = redisTemplate.execute(new DefaultRedisScript(lua,Long.class),keys,10);
                System.err.println(execute);
            }
        }
    }

```


### 6.2、实现redis原子操作
LUA代码：
```lua
    
    local num = redis.call("get",KEYS[1]) if not num then redis.call("set",KEYS[1],100) num = 100 end if tonumber(num) < 1 then return 0 else redis.call("set",KEYS[1],tonumber(num) - 1) return 1 end

```

```commandline
   eval script keynum keys args
   keys引用 KEYS数组
   args引用 ARGV数组
   
   evalsha1 script_sha1 keynum keys args
   
   eval 'local num = redis.call("get",KEYS[1]) if not num then redis.call("set",KEYS[1],100) num = 100 end if tonumber(num) < 1 then return 0 else redis.call("set",KEYS[1],tonumber(num) - 1) return 1 end' 1 lock 10
   
```


### 6.3、lua实现redis eval操作

lua代码：

```lua
    
    --- 释放redis 链接到连接池中
    -- @param red
    --
    local function close_redis(red)
        if not red then
            return
        end
        --释放连接(连接池实现)
        local pool_max_idle_time = 10000 --毫秒
        local pool_size = 100 --连接池大小
        local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)
        if not ok then
            ngx.say("set keepalive error : ", err)
        end
    end
    
    
    --- 执行lua函数
    -- @param red
    --
    function function_eval(red)
        local resp, _ = red:eval("return redis.call('get', KEYS[1])", 1, "msg");
        ngx.print(resp)
    end
    
    
    ---  执行redis中的函数evalsha1、   装载redis函数 script load
    -- @param red   redis 链接
    --
    function function_evalsha(red)
        local sha1, err = red:script("load",  "return redis.call('get', KEYS[1])");
        if not sha1 then
            ngx.say("load script error : ", err)
            return close_redis(red)
        end
        ngx.say("sha1 : ", sha1, "<br/>")
        resp, err = red:evalsha(sha1, 1, "msg");
        ngx.print(resp)
    end
    
    
    
    --- 创建redis链接
    --
    function redis_client()
        local redis = require "resty.redis"
        local red = redis:new()
        red:set_timeout(1000) -- 1 sec
        local ok, err = red:connect("192.168.61.128", 6379)
        if not ok then
            ngx.say("failed to connect: ", err)
            return
        end
        return red
    end
    
    
    local red = redis_client()
    function_eval(red)
    function_evalsha(red)
```
