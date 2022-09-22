## 检出umc源码功能
component,exec,exec.yml
component,pull-code,pull-code.yml
shell,cmd /c mklink /j #{[dir]}\umc-portal\business\src\main\java\com\montnets\umc\portal\audit #{[dir]}\umc-portal-audit\src\main\java\com\montnets\umc\portal\audit,#{[dir]}
component,replace-properties,replace-properties.yml

## 替换配置
#component,replace-properties,exec.yml

# 更新umc源码
#component,pull-code,pull-code.yml