## !!!变量说明：dir:指application.yml中dir resourceDir:指本项目resource绝对路径
##############################【变量】增加自定义变量p1#################################
#param,set p1=123
#shell,cmd /c echo #{[p1]},#{[dir]}
##############################【功能】合并文件夹下的文件################################
#component,splice-file,#{[classpath]}component\config\splice-file.yml
##############################【功能】打印文件大小功能##################################
# component,print-file-size,#{[classpath]}component\config\print-file-size.yml
##############################【功能】刷新DNS功能#####################################
#component,flush-dns,#{[classpath]}component\config\flush-dns.yml

##############################【功能】检出源码并检出为指定分支功能########################
## clone代码
#component,exec,#{[classpath]}component\config\exec.yml
### 更新代码
#component,pull-code,#{[classpath]}component\config\pull-code.yml
### 挂载audit模块代码到business
#shell,cmd /c mklink /j #{[dir]}\umc-portal\business\src\main\java\com\montnets\umc\portal\audit #{[dir]}\umc-portal-audit\src\main\java\com\montnets\umc\portal\audit,#{[dir]}
## 替换本地properties配置
#component,replace-properties,#{[classpath]}component\config\replace-properties.yml

##############################【功能】更新后端代码########################################
# component,pull-code,#{[classpath]}component\config\pull-code.yml

##############################【功能】替换本地properties配置##############################
#component,replace-properties,#{[classpath]}component\config\replace-properties.yml

##############################【功能】更新前端代码########################################
# component,exec,#{[classpath]}component\config\exec-pull-web.yml

##############################【功能】启动xxl-job########################################
# shell -enableOutput=false,cmd /c start #{[classpath]}component\file\start-xxl-job.bat,#{[dir]}

##############################【功能】启动zkServer########################################

# shell -enableOutput=false,cmd /c start #{[classpath]}component\file\start-zk-server.bat,#{[dir]}


##############################【功能】更新后端代码########################################
# component,pull-code,#{[classpath]}component\config\pull-code.yml
# component,replace-yml,#{[classpath]}component\config\replace-yml.yml
# component,replace-str,#{[classpath]}component\config\replace-str.yml
# component,replace-str-back,#{[classpath]}component\config\replace-str.yml

##############################【功能】vmware虚拟机启停####################################
# param,set vm_exe_path=D:\Program Files (x86)\VMware\VMware Workstation\vmrun.exe
# param,set vm_path=D:\VMware\Win10\Win10 x64.vmx
# shell,#{[vm_exe_path]} start "#{[vm_path]}" nogui
# shell,#{[vm_exe_path]} stop "#{[vm_path]}"

##############################【功能】更新前端代码并启动###################################
# 启动fts前端
component,exec,#{[classpath]}component\config\exec-pull-web.yml
shell,git push upstream master
# # 服务端替换为本地地址
component,replace-properties,#{[classpath]}component\config\replace-properties-web.yml
# component,exec,#{[classpath]}component\config\exec-start-web.yml
param,set vm_path1=D:\VMware\Win10\Win10 x64.vmx

# 启动c-fts
# component,replace-properties,#{[classpath]}component\config\replace-properties-c-web.yml
# component,exec,#{[classpath]}component\config\exec-start-c-fts-web.yml

# component,read-pdf,#{[classpath]}component\config\read-pdf.yml
# component,xy-decrypt,#{[classpath]}component\config\xy-decrypt.yml
#component,compress-file,#{[classpath]}component\config\compress-file.yml


