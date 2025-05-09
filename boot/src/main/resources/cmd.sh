##------------------------------dev-------------------------------
##  更新代码
#param,set --enableRefId=wms2.0_dev
#component,pull-code,#{[classpath]}component\config\pull-code.yml,async
## 配置替换
#param,set --mode=dev
#param,set --jd=
#component,replace-yml,#{[classpath]}component\config\replace-yml.yml

##------------------------------test-------------------------------
###  更新代码
#param,set --enableRefId=wms2.0_test
#component,pull-code,#{[classpath]}component\config\pull-code.yml,async
### 配置替换
#param,set --mode=test
#param,set --jd=
#component,replace-yml,#{[classpath]}component\config\replace-yml.yml

##------------------------------prod-ss-------------------------------
##  更新代码
#param,set --enableRefId=wms2.0_prod
#param,set --jd=_ss
#component,pull-code,#{[classpath]}component\config\pull-code.yml
## 配置替换
#param,set --mode=prod
#param,set --jd=_ss
#component,replace-yml,#{[classpath]}component\config\replace-yml.yml

param,set --directionMode=from
component,dir-to-text,#{[classpath]}component\config\dir-to-text.yml
shell,git checkout .




