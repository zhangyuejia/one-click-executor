## !!!变量说明：dir:指application.yml中dir resourceDir:指本项目resource绝对路径
##############################【变量】增加自定义变量p1#################################
#param;set --p1=123
#shell;cmd /c echo #{[p1]};#{[dir]}
##############################【功能】合并文件夹下的文件################################
#component;splice-file
##############################【功能】打印文件大小功能##################################
# component;print-file-size
##############################【功能】刷新DNS功能#####################################
#component;flush-dns

##############################【功能】检出源码并检出为指定分支功能########################
## clone代码
#component;exec
### 更新代码
#component;pull-code
### 挂载audit模块代码到business
#shell;cmd /c mklink /j #{[dir]}\umc-portal\business\src\main\java\com\montnets\umc\portal\audit #{[dir]}\umc-portal-audit\src\main\java\com\montnets\umc\portal\audit;#{[dir]}
## 替换本地properties配置
#component;replace-properties

##############################【功能】更新后端代码########################################
# component;pull-code

##############################【功能】替换本地properties配置##############################
#component;replace-properties

##############################【功能】更新前端代码########################################
# component;exec;#{[classpath]}component\config\exec-pull-web.yml

##############################【功能】启动xxl-job########################################
# shell --enableOutput=false;cmd /c start #{[classpath]}component\file\start-xxl-job.bat;#{[dir]}

##############################【功能】启动zkServer########################################

# shell --enableOutput=false;cmd /c start #{[classpath]}component\file\start-zk-server.bat;#{[dir]}


##############################【功能】更新后端代码########################################
# component;pull-code
# component;replace-yml
# component;replace-str
# component;replace-str-back;#{[classpath]}component\config\replace-str.yml

##############################【功能】vmware虚拟机启停####################################
param;set --vm_exe_path=D:\Program Files (x86)\VMware\VMware Workstation\vmrun.exe
param;set --vm_centos_path=C:\Users\zhanglj\Documents\Virtual Machines\CentOS 7 64 位\CentOS 7 64 位.vmx
param;set --vm_path=D:\VMware\Win10\Win10 x64.vmx

#shell;#{[vm_exe_path]} start "#{[vm_path]}" nogui
#component;sleep -t:5;#{[classpath]}component\config\sleep.yml
#shell --enableOutput=false;#{[classpath]}component/file/one-click-remote.bat

# shell;#{[vm_exe_path]} start "#{[vm_centos_path]}" nogui

# shell;#{[vm_exe_path]} stop "#{[vm_path]}"
# shell;#{[vm_exe_path]} stop "#{[vm_centos_path]}"
##############################【功能】一键远程WIN虚拟机####################################
# shell;taskkill /IM mstsc.exe


component;transfer-dir
##############################【功能】更新前端代码并启动###################################
#param;set --git_bash_dir=C:\Program Files\Git\
## 启动fts前端
#component;exec;#{[classpath]}component\config\exec-pull-web.yml
##shell;git push upstream master
#shell;#{[git_bash_dir]}git-bash.exe -c "git push upstream master;bash"
# # # 服务端替换为本地地址
#component;replace-properties;#{[classpath]}component\config\replace-properties-web.yml
#component;exec;#{[classpath]}component\config\exec-start-web.yml

# 启动c-fts
# component;replace-properties;#{[classpath]}component\config\replace-properties-c-web.yml
# component;exec;#{[classpath]}component\config\exec-start-c-fts-web.yml

# component;read-pdf
# component;xy-decrypt
#component;compress-file

##------------------------------dir-to-text-------------------------------
#component;dir-to-text --directionMode=from --fromDir=D:\Projects\wms_2.0\dev\wms2\.git --textPath=ram.txt
#component;dir-to-text --directionMode=to --fromDir=D:\Projects\wms_2.0\dev\wms2\.git --textPath=ram.txt
#component;dir-to-text --directionMode=from --textPath=ram4
#component;dir-to-text --directionMode=to --textPath=ram4
#shell;git checkout .

#component;move-pointer-keyboard




