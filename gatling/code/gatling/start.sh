#!/bin/bash

# 打包生成jar文件
mvn clean package

# 获得jar文件的名字
jarFile=`ls target | grep .jar$`

# 后台执行jar文件
nohup java -jar target/${jarFile} > /dev/null 2>&1 &

# 获得任务的pid
echo $! > target/pid.file

# 任务尝试重试的次数
times=0

# 每次任务失败休眠的时间
sleepTime=5

while [[ true ]]; do
    # 访问此url获得spring boot项目启动的状态
	result=`curl -L http://localhost:8080/actuator/health`

	if [[ $? == 7 ]]
	then
	    echo "项目启动中"
		sleep ${sleepTime}
  		((times++))
  		if [[ ${times} -gt 5 ]]
		then
		    echo "项目启动失败"
			break
		fi
	else
		if [[ ${times} -gt 5 ]]
		then
		    echo "获得项目状态失败"
			break
		else
		    # 如果结果包含 "status":"UP"，则项目成功启动
			if [[ ${result} == *"\"status\":\"UP\""* ]]; then
  				echo "项目成功启动"
  				break
  			else
  				sleep ${sleepTime}
  				((times++))
			fi
		fi
	fi
done

if [[ ${times} -gt 5 ]]
then
	echo "服务器没有成功启动，无法进行压力测试"
else
    echo "开始进行压力测试"

    # gatling压力测试命令
	mvn gatling:test

	# 拷贝生成的压测报告到nginx中，待补充
    mv target/gatling/* ./gatling
fi

# 杀死spring boot应用进程
kill $(cat target/pid.file)

# 删除任务生成的文件夹
rm -rf target/