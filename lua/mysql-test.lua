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
        password = "123456",
        charset = "utf8",
        max_packet_size = 1024 * 1024,
    }

    if not ok then
        ngx.log(ngx.ERR,"failed to connect: ", err, ": ", errcode, " ", sqlstate)
        return
    end

    return db
end

--- 创新创建表
function flush_table(table,db)
    ngx.log(ngx.NOTICE,"drop table if exists "..table)

    local res, err, errcode, sqlstate = db:query("drop table if exists "..table )
    if not res then
        ngx.log(ngx.ERR,"bad result: ", err, ": ", errcode, ": ", sqlstate, ".")
        return
    end


    res, err, errcode, sqlstate =
    db:query("create table "..table.." "
            .. "(id serial primary key, "
            .. "name varchar(20) , password varchar(35))")
    if not res then
        ngx.say("bad result: ", err, ": ", errcode, ": ", sqlstate, ".")
        return
    end
end


--- 插入数据
--- 数组索引从1开始
function insertsql(sqls,db)
    local length = table.getn(sqls)
    for i= 1, length do
        local sql = sqls[i]
        ngx.log(ngx.NOTICE,"sql : ", sql ,"\t i : ",i ,"\t length : ",length)
        if sql and sql ~= nil then
            ngx.log(ngx.NOTICE,"sql : ", sql )
            local res, err, errcode, sqlstate = db:query(sql)
            if not res then
                ngx.log(ngx.ERR,"bad result: ", err, ": ", errcode, ": ", sqlstate, ".")
                return
            end
        end
    end
end


--- 查询记录
function query(sql,db)
    local res, err, errcode, sqlstate = db:query(sql)
    if not res then
        ngx.log(ngx.ERR,"bad result: ", err, ": ", errcode, ": ", sqlstate, ".")
        return
    end
    local cjson = require "cjson"
    ngx.say("result: ", cjson.encode(res))
end


--- 释放到连接池中
function release_connect(db)
    local ok, err = db:set_keepalive(10000, 100)
    if not ok then
        ngx.say("failed to set keepalive: ", err)
        return
    end
end


local db = create_mysql_connect()
flush_table("t_users",db)
insertsql({string.format("insert into t_users (name,password) values ('zhangsan','%s')",ngx.md5('12345'))},db)
query("select * from t_users where name = 'zhangsan'",db)
release_connect(db)