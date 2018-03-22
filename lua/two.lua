--- 判断是否登陆
function is_login(field, redis_connect)
    local res, _ = redis_connect:get(field)
    if not res then
        return false
    end
    return res ~= nil;
end


---  新建redis 链接
function create_connect()
    local redis = require "resty.redis"
    local red = redis:new()
    red:set_timeout(1000) -- 1 sec
    local ok, err = red:connect("192.168.159.129", 6379)
    if not ok then
        ngx.say("failed to connect: ", err)
        return
    end
    return red
end


--- 登陆操作
function login(redis_connect)
    local name = args.name
    local password = args.password
    local user = query(string.format("select * from t_users where name = '%s' and password = '%s'",name,ngx.md5(password)),db)
    if user == nil or table.getn(user) == 0 then
        ngx.log(ngx.NOTICE,string.format("name : %s \t input name : %s","zhangsan",name))
        ngx.exit(ngx.HTTP_FORBIDDEN)
    end

    -- 存放到redis中
    local md5 = ngx.md5(string.format("%s%s%s",name,password,args.addr))
    ok, err = redis_connect:set(md5, name)
    ok, err = redis_connect:expire(md5,10000)
    if not ok then
        ngx.log(ngx.ERR, err)
    end

    -- 存放到cookie
    local cookie_ok, err = cookie:set({
        key = cookie_name,
        value = md5,
        path = "/"
    })
    if not cookie_ok then
        ngx.log(ngx.ERR, err)
    end
    ngx.log(ngx.NOTICE,"---------------------------redis----------------")
end


--- 创建cookie
function create_cookies()
    ck = require "resty.cookie"
    local cookie, err = ck:new()
    if not cookie then
        ngx.log(ngx.ERR, err)
        return
    end
    return cookie;
end


--- 释放redis链接
function release_redis_connect(redis_connect)
    -- put it into the connection pool of size 100,
    -- with 10 seconds max idle time
    local ok, err = redis_connect:set_keepalive(10000, 100)
    if not ok then
        ngx.say("failed to set keepalive: ", err)
        return
    end
end





--- 处理http 请求

function http_hander()
    --- local 为局部变量、非local为全局变量
    args = ngx.req.get_uri_args()
    cookie_name = "_sdfdsa"
    db = create_mysql_connect()
    local redis_connect = create_connect()
    cookie =  create_cookies()
    -- get single cookie
    local field, _ = cookie:get(cookie_name)
    if not field then
        login(redis_connect)
        --- return 不能return出，return出等于结束整个请求流程
    elseif field == nil then
        login(redis_connect)
    elseif not is_login(field, redis_connect) then
        login(redis_connect)
    end

    --ngx.say("恭喜您登陆成功！")
    release_redis_connect(redis_connect)
    ngx.redirect("/home")
end


--- 建立mysql链接
function create_mysql_connect()
    local mysql = require "resty.mysql"
    local db, err = mysql:new()
    if not db then
        ngx.say("failed to instantiate mysql: ", err)
        return
    end
    db:set_timeout(1000)        -- 1 sec
    local ok, err, errcode, sqlstate = db:connect{
        host = "127.0.0.1",
        port = 3306,
        database = "test",
        user = "root",
        password = "12345",
        charset = "utf8",
        max_packet_size = 1024 * 1024,
    }

    if not ok then
        ngx.log(ngx.ERR,"failed to connect: ", err, ": ", errcode, " ", sqlstate)
        return
    end
    return db
end


function query(sql,db)
    local res, err, errcode, sqlstate = db:query(sql)
    if not res then
        ngx.log(ngx.ERR,"bad result: ", err, ": ", errcode, ": ", sqlstate, ".")
        return nil
    end
    return res
end





http_hander()

