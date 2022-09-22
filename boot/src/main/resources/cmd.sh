## 检出umc源码功能
component,exec
component,pull-code
shell,cmd /c mklink /j #{[dir]}\umc-portal\business\src\main\java\com\montnets\umc\portal\audit #{[dir]}\umc-portal-audit\src\main\java\com\montnets\umc\portal\audit
component,replace-properties

## 替换配置
#component,replace-properties

# 更新umc源码
#component,pull-code