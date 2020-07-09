# 一、乐优商城介绍：

根据国内热门商城一些功能，通过自己所学习的微服务及一些热门框架所作的一套商城。

选择使用Spring Cloud技术栈构建项目。

# 二、使用架构：

(1)整个系统采用前后端分离的开发模式，

(2)前端基于Vue技术栈进行开发，后端通过axiosj进行交互。

(3)通过nginx实现对后台服务的反向代理和负载均衡。

(4)后端采用Spring Cloud技术栈构建为微服务，对外提供Restful风格接口。

(5)zuul作为整个微服务入口，实现请求路由，负载均衡，限流，权限控制等功能。



# 三、开发技术栈：

## 前端技术：

- 基础的HTML、CSS、JavaScript（基于ES6标准）
- JQuery
- Vue.js 
- 前端构建工具：WebPack
- 前端安装包工具：NPM
- Vue脚手架：Vue-cli
- Vue路由：vue-router
- ajax框架：axios
- 基于Vue的富文本框架：quill-editor

## 后端技术：

- 基础的SpringMVC、Spring 5.0和MyBatis3
- Spring Boot 2.0.1版本
- Spring Cloud（ Finchley.RC1）
- Redis-4.0
- RabbitMQ-3.4
- Elasticsearch-5.6.8
- nginx-1.10.2：
- FastDFS - 5.0.8
- Thymeleaf

# 三、功能介绍：

## 1、后台管理页面：

面向公司内部人员，实现对商城日常业务的管理。

(1)商品管理：包括商品分类，品牌，商品规格等信息的管理。

(2)销售管理：包括订单统计，订单退款处理，促销活动生成等。

(3)用户管理：包括用户控制，冻结，解锁等。

(4)权限管理：整个网站的权限控制，采用JWT鉴权方案，对用户及API进行权限控制

(5)统计，各种数据的统计分析展示



## 2、前台门户页面：

面向买家，实现商品的展示。

它所包括的后台功能：

### (1)商品微服务：

商品及商品分类、品牌、库存等的服务

* 商品分类管理
* 商品品牌管理
* 商品规格参数管理：因为规格的可变性，采用竖表设计，分为规格和规格组表
* 商品管理，难点是SPIJ和SKU的设计，以及SKU的动态属性
* 库存管理，库存加减采用乐观锁方案，另外定时对库存进行判断，库存不足可通知簽

### (2)搜索微服务：

实现搜索功能

* 采用elsticsearch完成商品的全文检索功能
* 难点是搜索的过滤条件生成
* 集群

### (3)订单微服务：

实现订单相关业务

+ 订单的表设计，状态记录

+ 创建订单需要同时减库存，跨服务业务，需要注意事务处理、流程

+ 查询订单提交的商品信息

  ​       计算订单总价（计算商品总价、计算运费、计算促销金额）

  ​        写入订单、订单详情、订单状态

​	       减库存，远程同步调用商品微服务，实现减库存

+ 如果采用异步减库存，可能需要引入分布式事务

### (4)购物车微服务：

实现购物车相关功能

+ 离线购物车．主要使用localstorage保存到客户端
+ 在线购物车:   使用Redis实现

### (5)用户微服务：

用户的登录注册、用户信息管理等业务

+ 用户注册
+ 注册数据校验
+ 查询用户信息
+ 收货地址管理
+ 用户积分管理等

### (6)认证微服务：

用户权限及服务权限认证

* 权限管理CURD


* 登录token生成
* 登录token认证
* 服务间token生成

### (7)短信微服务：

完成短信的发送

+ 对接阿里平台，通过MSQ实现异步短信发送

(8)注册中心：Eureka





# 四、实现难点：



## 1、跨域问题

跨域是指跨域名的访问，以下情况都属于跨域：

| 跨域原因说明       | 示例                                   |
| ------------------ | -------------------------------------- |
| 域名不同           | `www.jd.com` 与 `www.taobao.com`       |
| 域名相同，端口不同 | `www.jd.com:8080` 与 `www.jd.com:8081` |
| 二级域名不同       | `item.jd.com` 与 `miaosha.jd.com`      |

为什么有跨域问题？

跨域不一定会有跨域问题。

因为跨域问题是浏览器对于ajax请求的一种安全限制：**一个页面发起的ajax请求，只能是于当前页同域名的路径**，这能有效的阻止跨站攻击。

因此：**跨域问题 是针对ajax的一种限制**。

但是这却给我们的开发带来了不变，而且在实际生成环境中，肯定会有很多台服务器之间交互，地址和端口都可能不同，怎么办？

### 解决方案：

- nginx反向代理

  思路是：利用nginx反向代理把跨域为不跨域，支持各种请求方式

  缺点：需要在nginx进行额外配置，语义不清晰

- CORS

  规范化的跨域请求解决方案，安全可靠。

  优势：

  - 在服务端进行控制是否允许跨域，可自定义规则
  - 支持各种请求方式

  缺点：

  - 会产生额外的请求

我们这里会采用cors的跨域方案

CORS是一个W3C标准，全称是"跨域资源共享"（Cross-origin resource sharing）。

它允许浏览器向跨源服务器，发出[`XMLHttpRequest`](http://www.ruanyifeng.com/blog/2012/09/xmlhttprequest_level_2.html)请求，从而克服了AJAX只能[同源](http://www.ruanyifeng.com/blog/2016/04/same-origin-policy.html)使用的限制。

CORS需要浏览器和服务器同时支持。目前，所有浏览器都支持该功能，IE浏览器不能低于IE10。

- 浏览器端：

  目前，所有浏览器都支持该功能（IE10以下不行）。整个CORS通信过程，都是浏览器自动完成，不需要用户参与。

- 服务端：

  CORS通信与AJAX没有任何差别，因此你不需要改变以前的业务逻辑。只不过，浏览器会在请求中携带一些头信息，我们需要以此判断是否运行其跨域，然后在响应头中加入一些信息即可。这一般通过过滤器完成即可Origin中会指出当前请求属于哪个域（协议+域名+端口）。服务会根据这个值决定是否允许其跨域。

  如果服务器允许跨域，需要在返回的响应头中携带下面信息：

  ```http
  Access-Control-Allow-Origin: http://manage.leyou.com
  Access-Control-Allow-Credentials: true
  Content-Type: text/html; charset=utf-8
  ```

  - Access-Control-Allow-Origin：可接受的域，是一个具体域名或者*，代表任意
  - Access-Control-Allow-Credentials：是否允许携带cookie，默认情况下，cors不会携带cookie，除非这个值是true

  注意：

  如果跨域请求要想操作cookie，需要满足3个条件：

  - 服务的响应头中需要携带Access-Control-Allow-Credentials并且为true。
  - 浏览器发起ajax需要指定withCredentials 为true
  - 响应头中的Access-Control-Allow-Origin一定不能为*，必须是指定的域名

  在`ly-api-gateway`中编写一个配置类，并且注册CorsFilter：

  @Configuration
  public class GlobalCorsConfig {
      @Bean
      public CorsFilter corsFilter() {
          //1.添加CORS配置信息
          CorsConfiguration config = new CorsConfiguration();
          //1) 允许的域,不要写*，否则cookie就无法使用了
          config.addAllowedOrigin("http://manage.leyou.com");
          //2) 是否发送Cookie信息
          config.setAllowCredentials(true);
          //3) 允许的请求方式
          config.addAllowedMethod("OPTIONS");
          config.addAllowedMethod("HEAD");
          config.addAllowedMethod("GET");
          config.addAllowedMethod("PUT");
          config.addAllowedMethod("POST");
          config.addAllowedMethod("DELETE");
          config.addAllowedMethod("PATCH");
          // 4）允许的头信息
          config.addAllowedHeader("*");

          //2.添加映射路径，我们拦截一切请求
          UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
          configSource.registerCorsConfiguration("/**", config);
      
          //3.返回新的CorsFilter.
          return new CorsFilter(configSource);
      }
  }

## 2、关于商品有共同属性，具体特性，该如何设计数据库表？

SPU：Standard Product Unit （标准产品单位） ，一组具有共同属性的商品集

SKU：Stock Keeping Unit（库存量单位），SPU商品集因具体特性不同而细分的每个商品

商品的规格参数应该是与分类绑定的。**每一个分类都有统一的规格参数模板，但不同商品其参数值可能不同**。

也就是说，我们没必要单独对SKU的特有属性进行设计，它可以看做是规格参数中的一部分。这样规格参数中的属性可以标记成两部分：

- 所有sku共享的规格属性（称为全局属性）
- 每个sku不同的规格属性（称为特有属性）

我们可以创建specification表 里面存放json数据，然后由前端进行json解析

CREATE TABLE `tb_specification` (
  `category_id` bigint(20) NOT NULL COMMENT '规格模板所属商品分类id',
  `specifications` varchar(3000) NOT NULL DEFAULT '' COMMENT '规格参数模板，json格式',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规格参数模板，json格式。';



- 规格参数分组，每组有多个参数
- 参数的 `k`代表属性名称，没有值，具体的SPU才能确定值
- 参数会有不同的属性：是否可搜索，是否是全局、是否是数值，这些都用boolean值进行标记：
  - SPU下的多个SKU共享的参数称为全局属性，用`global`标记
  - SPU下的多个SKU特有的参数称为特有属性
  - 如果参数是数值类型，用`numerical`标记，并且指定单位`unit`
  - 如果参数可搜索，用`searchable`标记





## 3、分布式环境下，共享数据如何存储？如何对用户进行授权鉴权?



微服务集群中的每个服务，对外提供的都是Rest风格的接口。而Rest风格的一个最重要的规范就是：服务的无状态性，即：

·       服务端不保存任何客户端请求者信息

·       客户端的每次请求必须具备自描述信息，通过这些信息识别客户端身份

带来的好处是什么呢？

·       客户端请求不依赖服务端的信息，任何多次请求不需要必须访问到同一台服务

·       服务端的集群和状态对客户端透明

·       服务端可以任意的迁移和伸缩

·       减小服务端存储压力



### (1)如何实现无状态?

##### 无状态登录的流程：

当客户端第一次请求服务时，服务端对用户进行信息认证（登录）****

  认证通过，将用户信息进行加密形成token，返回给客户端，作为登录凭证

以后每次请求，客户端都携带认证的*token*

服务端对*token进行解密，判断是否有效



### (2)结合非对称+JWT进行鉴权



 1）我们首先利用RSA生成公钥和私钥。私钥保存在授权中心，公钥保存在Zuul和各个值得信任的微服务

  2）用户请求登录

  3）授权中心校验，通过后用私钥对JWT进行签名加密

  4）返回jwt给用户

  5）用户携带JWT访问

  6）Zuul直接通过公钥解密JWT（**除了白名单中的用户**），进行验证，验证通过则放行        

7）请求到达微服务，**如果zuul的过滤器已经配置公钥解析，其他需要token的微服务不需要进行其他操作**







