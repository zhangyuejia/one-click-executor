## 【功能】检出源码并检出为指定分支功能
## clone代码
#component,exec,exec.yml
## 更新代码
#component,pull-code,pull-code.yml
## 挂载audit模块代码到business
#shell,cmd /c mklink /j #{[dir]}\umc-portal\business\src\main\java\com\montnets\umc\portal\audit #{[dir]}\umc-portal-audit\src\main\java\com\montnets\umc\portal\audit,#{[dir]}
## 替换本地properties配置
#component,replace-properties,replace-properties.yml

## 【功能】替换本地properties配置
#component,replace-properties,exec.yml

## 【功能】更新代码
component,pull-code,pull-code.yml

# 【功能】合并文件夹下的文件
#component,splice-file,splice-file.yml