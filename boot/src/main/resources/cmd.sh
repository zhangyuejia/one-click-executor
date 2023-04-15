## !!!变量说明：dir:指application.yml中dir resourceDir:指本项目resource绝对路径
##############################【变量】增加自定义变量p1#################################
#param,set p1=123
#shell,cmd /c echo #{[p1]},#{[dir]}
##############################【功能】合并文件夹下的文件################################
#component,splice-file,#{[classpath]}component\config\splice-file.yml
##############################【功能】打印文件大小功能##################################
#component,print-file-size,#{[classpath]}component\config\print-file-size.yml
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
#component,pull-code,#{[classpath]}component\config\pull-code.yml

##############################【功能】替换本地properties配置##############################
#component,replace-properties,#{[classpath]}component\config\replace-properties.yml

##############################【功能】更新前端代码########################################
#component,exec,#{[classpath]}component\config\exec-pull-web.yml

##############################【功能】更新前端代码并启动###################################
#component,exec,#{[classpath]}component\config\exec-start-web.yml

component,read-pdf,#{[classpath]}component\config\read-pdf.yml
#component,compress-file,#{[classpath]}component\config\compress-file.yml


