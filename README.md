# SpringBoot快速入门案例

主要是自己学习将SSM手动整合升级为SpringBoot配置上的变化

### 整体实现的流程如下：

用户输入用户名密码

获取用户输入的用户名以及密码，传递到后台数据库，进行信息查询，如果用户的用户名和密码在数据库中存在，则登陆成功，跳转至登陆成功的界面。反之登陆失败，返回登陆界面，重新登陆

### 整个系统中功能实现的流程如下：

前台发送请求即要实现哪种功能，然后service层传递到mapper层，进行数据库的交互，然后将数据库查询的结果进行一层一层的返回，最后在前台页面展示返回的结果

### 整个系统的项目结构图如下：

```
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─winsun
│  │  │          └─upssm
│  │  │              │  UpssmApplication.java
│  │  │              │  
│  │  │              ├─controller
│  │  │              │      userController.java
│  │  │              │      
│  │  │              ├─entity
│  │  │              │      User.java
│  │  │              │      
│  │  │              ├─mapper
│  │  │              │      userMapper.java
│  │  │              │      
│  │  │              └─service
│  │  │                      userService.java
│  │  │                      userServiceImpl.java
│  │  │                      
│  │  └─resources
│  │      │  application.yml
│  │      │  
│  │      ├─static
│  │      ├─templates
│  │      │      login.html
│  │      │      test.html
│  │      │      
│  │      └─数据库文件
```

## 1.使用IDEA创建SpringBoot项目

- 创建过程参考：https://zhuanlan.zhihu.com/p/50878718

## 2.导入起步依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.winsun</groupId>
    <artifactId>upssm</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>upssm</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>

    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```


## 3.建立数据库

```mysql
CREATE DATABASE ssm;

USE ssm;

CREATE TABLE user(
id INT(10) NOT NULL AUTO_INCREMENT primary key,
username varchar(32) NOT NULL,
password varchar(32) NOT NULL
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO user(username,password)VALUES
('test','123456');

```


## 4.编写相应的类

### 4.1.实体类

```java
package com.winsun.upssm.entity;

import java.io.Serializable;

public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
```


### 4.2.持久层

```java
package com.winsun.upssm.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface userMapper {

    @Select("select username from user where username=#{username} and password=#{password}")
    String login(String username, String password);
}
```


### 4.3.业务层

```java
package com.winsun.upssm.service;

public interface userService {

    String login(String username, String password);
}
```


```java
package com.winsun.upssm.service;

import com.winsun.upssm.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {
    @Autowired
    userMapper userMapper;

    @Override
    public String login(String username, String password) {
        return userMapper.login(username, password);
    }
}
```


### 4.4.控制层

```java
package com.winsun.upssm.controller;

import com.winsun.upssm.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class userController {

    @Autowired
    private userService userService;
    
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String login() {
        return "login";
    }
    
    @RequestMapping(value = "loginPage", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(HttpServletRequest request, HttpSession session) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
    
        String name = userService.login(username, password);
        session.setAttribute("username", name);
    
        if (name == null) {
            return "redirect:/";
        }else {
            return "redirect:/index";
        }
    }
@RequestMapping(value = "index", method = {RequestMethod.GET, RequestMethod.POST})
    public String success(){
        return "/success";
    }
}
```

### 5.1前端页面 login.html

```html
<!DOCTYPE html>
<html>
<head>
    <div>
        <div>登录系统</div>
        <form action="loginPage" method="post">
            <table>
                <tr>
                    <td>用户名：</td>
                    <td><input type="text" name="username" ></input></td>
                </tr>

                <tr>
                    <td>密码：</td>
                    <td><input type="password" name="password" ></input></td>
                </tr>
            </table>
            <input type="submit" name="提交">
        </form>
    </div>
</head>
</html>
```

### 5.2前端页面 success.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <div>
        <h3 th:text="'欢迎您：：' + ${session.username}"></h3>
        <p>
            你好！
        </p>
        <p>登陆成功了</p>
    </div>
</head>
</html>
```

## 6.SpringBoot 配置文件

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ssm?useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 123456
```

## 7.SpringBoot 启动类

```java
package com.winsun.upssm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//扫描接口
@MapperScan("com.winsun.upssm.mapper")
@SpringBootApplication
public class UpssmApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpssmApplication.class, args);
    }
}

```
