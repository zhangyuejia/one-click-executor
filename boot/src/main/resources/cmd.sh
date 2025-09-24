##------------------------------配置替换-------------------------------
## 配置替换
#param;set --codeMode=dev2 --configMode=dev2 && component;replace-yml
#param;set --codeMode=test2 --configMode=test2 && component;replace-yml
#param;set --codeMode=prod2 --configMode=prod_cs && component;replace-yml
#param;set --codeMode=dev2 --configMode=dev2_xz_split && component;replace-yml

##------------------------------更新代码-------------------------------
####  更新代码
component;pull-code --currentRefId=wms2.0_dev;;async
component;pull-code --currentRefId=wms2.0_test;;async
##  更新代码-master
#param;set --jd= && component;pull-code --currentRefId=wms2.0_master;;async
##  更新代码-生产基地
#param;set --jd=_xa && component;pull-code --currentRefId=wms2.0_prod;;async

#component;dir-to-text --directionMode=from --textPath=ram

##------------------------------3.0-dev-------------------------------
## 配置替换
#param;set --mode=dev3 && component;replace-yml;#{[classpath]}component\config\replace-yml-wms3.yml
#param;set --mode=test3 && component;replace-yml;#{[classpath]}component\config\replace-yml-wms3.yml

##------------------------------3.0-test-------------------------------
###  更新代码
#component;pull-code --currentRefId=wms3.0_dev;#{[classpath]}component\config\pull-code-wms3.yml;async
#component;pull-code --currentRefId=wms3.0_test;#{[classpath]}component\config\pull-code-wms3.yml;async
#component;pull-code --currentRefId=wms3.0_prod;#{[classpath]}component\config\pull-code-wms3.yml;async

