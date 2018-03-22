--- 【内部，不能请求外链接】发送http请求
local res = ngx.location.capture("/home")
ngx.print(res.body)
ngx.print("( • ̀ω•́ )✧中国")
ngx.say("\n rewrite rewrite_by_lua_block")
