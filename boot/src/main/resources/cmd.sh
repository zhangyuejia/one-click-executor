##------------------------------dev-------------------------------
##  更新代码

## 配置替换
#param;set --mode=dev2 --jd=
#component;replace-yml

##------------------------------test-------------------------------
###  更新代码
#component;pull-code --currentRefId=wms2.0_dev --enablePullAsync=true;;async
#component;pull-code --currentRefId=wms2.0_test --enablePullAsync=true;;async
## 配置替换
#param;set --mode=test2 --jd=
#component;replace-yml

##------------------------------prod-ss-------------------------------
####  更新代码
param;set --jd=_ss
component;pull-code --currentRefId=wms2.0_prod --enablePullAsync=true;;async
### 配置替换
#param;set --mode=prod --jd=_cs
#component;replace-yml

##------------------------------prod-master-------------------------------
#param;set --jd=
#component;pull-code --currentRefId=wms2.0_master

#component;dir-to-text --directionMode=from --textPath=test
#component;move-pointer-keyboard

#component;pull-code --currentRefId=wms2.0_dev_mybatisplus --enablePullAsync=true;;async

##------------------------------3.0-dev-------------------------------
## 配置替换
#param;set --mode=dev3
#component;replace-yml;#{[classpath]}component\config\replace-yml-wms3.yml
#param;set --mode=test3
#component;replace-yml;#{[classpath]}component\config\replace-yml-wms3.yml

##------------------------------3.0-test-------------------------------
###  更新代码
#component;pull-code --currentRefId=wms3.0_dev --enablePullAsync=true;#{[classpath]}component\config\pull-code-wms3.yml;async
#component;pull-code --currentRefId=wms3.0_test --enablePullAsync=true;#{[classpath]}component\config\pull-code-wms3.yml;async
#component;pull-code --currentRefId=wms3.0_prod --enablePullAsync=true;#{[classpath]}component\config\pull-code-wms3.yml;async
## 配置替换
#param;set --mode=test --jd=
#component;replace-yml
