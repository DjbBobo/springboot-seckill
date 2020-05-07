# 高并发秒杀系统

## 1.开发环境

| JDK  | Maven | MySQL  | Redis  | RabbitMQ | SpringBoot |
| :--: | :---: | :----: | :----: | :------: | :--------: |
| 1.8  | 3.5.2 | latest | latest |  latest  |   2.2.6    |

## 2.使用说明

1.配置好以上开发环境。

2.克隆本项目文件到本地。

3.将**springboot-miaosha**项目文件导入IDE。

5.运行**miaosha.sql**文件，创建相应的表和插入数据。

4.修改**application.yml**中的相关配置信息，如host、port、username...

5.启动**SpringbootMiaoshaApplication**。

6.本项目没有实现前端页面，使用**knife4j**接口文档发送相关请求

> 访问页面:localhost:8080/doc.html



## 3.其他说明

若要批量生成用户，使用**com.bo.util**包下的**UserUtil**以及**DBUtil**工具类。



## 4.项目描述

1.使用分布式Session，实现在多台服务器的情况下，可以**“共享”**Session。

2.使用Redis缓存提高访问速度，帮助MySQL减压。

3.使用消息队列，实现异步处理，避免线程阻塞，提高用户体验。

4.统一结果、统一异常。





