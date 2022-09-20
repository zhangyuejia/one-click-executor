git clone http://192.169.2.234/zhanglj001/umc-portal.git
git remote add upstream http://192.169.2.234/umc/umc-portal.git$#{[baseDir]}\umc-portal

git clone http://192.169.2.234/zhanglj001/umc-portal-boot.git
git remote add upstream http://192.169.2.234/umc/umc-portal-boot.git$#{[baseDir]}\umc-portal-boot

git clone http://192.169.2.234/zhanglj001/umc-portal-utils.git
git remote add upstream http://192.169.2.234/umc/umc-portal-utils.git$#{[baseDir]}\umc-portal-utils

git clone http://192.169.2.234/zhanglj001/umc-portal-common.git
git remote add upstream http://192.169.2.234/umc/umc-portal-common.git$#{[baseDir]}\umc-portal-common

git clone http://192.169.2.234/zhanglj001/umc-portal-business.git
git remote add upstream http://192.169.2.234/umc/umc-portal-business.git$#{[baseDir]}\umc-portal-business

git clone http://192.169.2.234/zhanglj001/umc-portal-dependencies.git
git remote add upstream http://192.169.2.234/umc/umc-portal-dependencies.git$#{[baseDir]}\umc-portal-dependencies

git clone http://192.169.2.234/zhanglj001/umc-portal-system.git
git remote add upstream http://192.169.2.234/umc/umc-portal-system.git$#{[baseDir]}\umc-portal-system

git clone http://192.169.2.234/zhanglj001/umc-portal-audit.git
git remote add upstream http://192.169.2.234/umc/umc-portal-audit.git$#{[baseDir]}\umc-portal-audit

git clone http://192.169.2.234/zhanglj001/umc-portal-communication.git
git remote add upstream http://192.169.2.234/umc/umc-portal-communication.git$#{[baseDir]}\umc-portal-communication

# cmd /c mklink /j #{[baseDir]}\umc-portal\business\src\main\java\com\montnets\umc\portal\audit #{[baseDir]}\umc-portal-audit\src\main\java\com\montnets\umc\portal\audit