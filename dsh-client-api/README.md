# dsh-client-api

## 技术栈
- JDK 17
- Spring Boot 3.2.3
- Maven 3.9.0
- Mysql 8.0
- Redis

## 环境准备
- 安装JDK17
- 安装Maven3.9.0
- 安装Redis
- 安装Mysql8.0
- 安装Minio


## 系统模块介绍
common	公共目录，存放项目中通用的工具类、配置类、常量以及异常处理等内容
common/cache	缓存相关代码，本系统中主要与 redis 相关
common/config	存放项目中常用的配置文件，如：Mybatis 分页配置等
common/constant	基础的常量声明
common/interceptor	存放拦截器相关的配置，拦截器可以仔请求被处理前、处理过程中或处理后拦截请求和响应，比如进行身份的验证
common/exception	声明自定义异常，以及全局异常处理
common/handler	分离项目中通用的业务逻辑
common/result	封装接口统一响应体
controller	存放接口控制器，处理一些 HTTP 请求，返回响应结果
convert	实体类与视图之间的转换与映射
model	数据模型包，包括 do、 dto、 vo
model/entity	项目中涉及的实体类
model/dto	数据传输对象，客户端请求参数的实体封装
model/query	接口请求参数封装
model/vo	封装接口返回的 VO 视图
mapper	存放 MyBatis 的 mapper 接口，定义数据库操作的方法，每个方法对应一个数据库操作
service	存放服务层，处理接口包含的 业务逻辑实现
utils	存放一些工具类
DshClientApiApplication	项目启动主类
resources/log	日志配置，配置日志输出级别，输出格式等
resources/mapper	mapper接口类中对应的xml格式实现
application.yml	系统主配置文件
application-dev.yml	系统在dev运行环境下的配置文件，在主配置文件中可以指定运行环境
application-prod.yml	系统在prod运行环境下的配置文件，在主配置文件中可以指定运行环境
