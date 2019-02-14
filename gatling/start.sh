#!/bin/bash

currentLoc=`pwd`
springBootLoc=${currentLoc}/springboot
gatlingLoc=${currentLoc}/stress-gatling

echo "开始运行spring boot项目"
# 进入spring boot项目所在的目录
cd ${springBootLoc}
# 删除可能存在的target文件夹
rm -rf ./target/

# 打包生成jar文件
mvn clean package

# 获得jar文件的名字
jarFile=`ls target | grep .jar$`

# 启动spring boot项目并把启动日志写入到log.log文件中
java -jar target/${jarFile}  >target/log.log &
# 获得任务的pid
echo $! > target/pid.file

# 任务尝试重试的次数
times=0
maxTimes=5

# 每次任务失败休眠的时间
sleepTime=5

while [[ -f target/log.log ]]
do
    echo "检索log文件"
    result=`cat target/log.log | grep "Started SpringbootApplication"`
    if [[ "$result" != "" ]]
    then
        echo "spring boot项目成功启动"
        break
    else
        ((times++))
  		if [[ ${times} -gt ${maxTimes} ]]
		then
		    echo "项目启动失败"
			break
		fi
		sleep ${sleepTime}
    fi
done

if [[ ${times} -gt ${maxTimes} ]]
then
	echo "服务器没有成功启动，无法进行压力测试"
else
    echo "开始进行压力测试"

    # 进入gatling项目所在的目录
    cd  ${gatlingLoc}

    # gatling压力测试命令
	mvn gatling:test

	# 先删除nginx目录下的所有文件
#	rm -rf /home/zhaolei/Desktop/nginx/*
	# 拷贝生成的压测报告到nginx中
#    mv target/gatling/* /home/zhaolei/Desktop/nginx/
fi

echo "结束spring boot项目"
# 杀死spring boot应用进程
kill $(cat ${springBootLoc}/target/pid.file)

#echo "删除spring boot和gatling压力测试生成的文件"
# 删除任务生成的文件夹
#rm -rf ${springBootLoc}/target/
#rm -rf ${gatlingLoc}/target/