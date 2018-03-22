-- numkeys 为 key的个数
-- EVAL script numkeys key/[key ...] arg/[arg ...]      （KEYS：为key的数组，ARGV：为参数的数组）
-- -- 例如： eval "return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}" 2 username age jack 20

---redis.call() 和 redis.pcall() 的唯一区别在于它们对错误处理的不同。当 redis.call() 在执行命令的过程中发生错误时，脚本会停止执行，并返回一个脚本错误，错误的输出信息会说明错误造成的原因：
---和 redis.call() 不同， redis.pcall() 出错时并不引发(raise)错误，而是返回一个带 err 域的 Lua 表(table)，用

function function_1()
    local times = redis.call('incr',KEYS[1])

    if times == 1 then
        redis.call('expire',KEYS[1], ARGV[1])
    end

    if times > tonumber(ARGV[2]) then
        return 0
    end
    return 1
end


function function_incr()
    local num = redis.call("get",KEYS[1])
    if not num then
        redis.call("set",KEYS[1],100)
    end
    if num < 1 then
        return 0
    else
        redis.set("set",KEYS[1],num - 1)
        return 1
    end
end



--- redis lua script
function redis_has() local num = redis.call("get",KEYS[1]) if not num then return 0 end return 1 end


--- 实现redis原子性 自减少【库存】
function redis_desc()
    local num = redis.call("get",KEYS[1]) if not num then redis.call("set",KEYS[1],100) num = 100 end if tonumber(num) < 1 then return 0 else redis.call("set",KEYS[1],tonumber(num) - 1) return 1 end
end



--- 分布式锁实现
function redis_lock()
    -- call 函数直接终止lua命令    expire key second
    local empty = redis.call("setnx",KEYS[1],"distributed_lock") if tonumber(empty) > 0 then redis.call("expire",KEYS[1],ARGV[1]) return 1 end return 0

end




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