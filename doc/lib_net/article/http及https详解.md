**阅读本文你能收获到**

* 了解互联网上的资源是如何表示, 如果被定位
* 了解 http 协议 的报文结构及常见方法, 返回码
* 了解数字签名, 签名认真, 数字证书的使用以及一些常见安全攻防场景
* 了解 https 加密流程, 涉及 tcp 握手, 加密原理
* 解释你可能产生的疑惑, 加深对文章篇幅的理解

**目录索引**

—— [资源定位](#1)     
—— [HTTP协议](#2)   
—— [HTTPS协议](#3)  
———— [TCP连接的建立和关闭](#3_1)   
———— [证书](#3_2)   
———— [SSL/TLS握手流程](#3_3)   
—— [解释一些疑惑](#3)   


在了解 HTTP 协议之前, 先了解下在万维网上资源是如何被定位的。

<h3 id="1">资源定位</h3>

对于世界上任意存在的文件, 如果希望有一种规则可以用来表示他们, 那么这种规则就是 URI。

* 统一资源标识符 URI

	URI是 Uniform Resource Identifier 的缩写, 顾名思义就是唯一资源标识符号。
	
	* Uniform, 规定统一的格式用于便捷处理多种不同类型的资源, 比如使用 HTTP 协议处理报文等
	* Resource, 表示可识别的任意资源, 包括单一资源或者资源的集合体
	* Identifier, 表示可识别的对象
	
	那么URI是怎么运作的呢？ 其实 URL 只是一种句法结构, URL 是其子集, 通过 URL 例子来更具象地了解它的运作。

* 统一资源定位符 URL

	统一资源定位符是对可以从互联网上得到的资源的位置和访问方法的一种简洁的表示，是互联网上标准资源的地址。互联网上的每个文件都有一个唯一的URL，它包含的信息指出文件的位置以及浏览器应该怎么处理它。

	URL 在日常网页浏览中极其常见.
		
	举个例子 `http://user:pass@www.baidu.com:8080/dir/index.html?uid=yummylau#hash2` 为一个有效的 url
	
	针对例子对 url 进行划分解析
	
	* http:// 协议类型, 除了http还有ftp, telnet, urn等等。也可以使用data: 或者javascript：等指定数据或者脚本, 必须设置
	* user:pass 登陆信息, 指定用户名和密码作为从服务器获取资源的必要登陆信息, 可选设置
	* www.baidu.com 服务器地址, 也可以使用IP地址, 必须设置
	* :8080 服务器端口号, 不输入时服务器有默认端口好, 可选设置
	* dir/index.html 带有层次的文件路径, 指定服务器上的文件路径来获取服务器上的特定资源
	* ?uid=yummylau 查看自选, 查询用户id等于yummylau的资源, 可选设置
	* #hash2 片段标识符, hash值, 可作为锚点也可以作为路由, 可选设置

明白了 url 是如何表示互联网上的资源之后, 可以正式进入 HTTP 章节。

<h3 id="2">HTTP协议</h3>

HTTP协议（Hyper Text Transfer Protocol）, 超文本传输协议, 用于从万维网服务器传输超文本到本地浏览器的传送协议。

**http协议特点**

* 基于请求/响应 （模式）
* 面向事务（请求）
* 面向文本（报文内容）
* 无连接, 无状态

HTTP 基于请求响应的模型, 每一个请求都算一个事务, 无论是响应还是请求, 报文中的内容都是文本内容。 早期的 HTTP 请求限制每次连接只处理一个请求, 客户端收到服务端的应答之后便断开了连接, 这便是无连接。 为了解决重复建立连接低效的问题, 引入 Keep-Alive 用于客户端对服务器端的连接持续保留, 避免重新建立连接。对于每一个请求都是相对独立的, Keep-Alive也无法改变请求的结果, 对每一个事务的处理都没有记忆的能力, 并不会因为曾经请求过再次请求就不会重启一个请求, 这就是无状态。

了解http协议, 需要重点掌握协议的报文格式, 根据请求/响应分为 *请求报文* 和 *响应报文*

**请求报文**

<img src="../pic/http_1.png" width = "400" height = "150" alt="图片名称" align=center />


* 开始行

	上述图为请求报文头部, 开始行中 `方法`, `URL`, `版本` 之间需要用空格隔开

	* 方法, 包括 GET, POST, HEAD, PUT, DELETE, OPTIONS, TRACE, CONNECT
	* URL, 为目标资源在万网网中的表示形式
	* 版本, 为http协议版本

	比如 `GET /index.html HTTP/1.1` 表示使用 HTTP/1.1 版本的协议获取目标主机上路经为 /index.html 的资源。 

* 常见头部请求头/实体头
	* Accept 表示浏览器接受的数据类型, 如 `Accept: text/html,image/*`
	* Accept-Charset 表示浏览器接受的编码格式, 如 `Accept-Charset: ISO-8859-1`
	* Accept-Edcoding 表示浏览器接受的数据压缩格式, 如 `ccept-Encoding: gzip,compress`
	* Accept-Datetime 表示浏览器接受的版本时间, 如 `Accept-Datetime: Thu, 31 May 2007 20:35:00 GMT`
	* Accept-Language 表示浏览器接受的语言, 如 `Accept-Language: en-us,zh`
	* Authorization 表示设置HTTP身份验证的凭证, 如 `Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==`
	* Cache-Control 设置请求响应链上所有的缓存机制必须遵守的指令, 如 `Cache-Control: no-cache`
	* Content-Length 设置请求体的字节长度, 如 `Content-Length: 348`
	* Content-MD5 设置基于MD5算法对请求体内容进行Base64二进制编码, 如 `Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ==`
	* Content-Type 设置请求体的MIME类型（适用POST和PUT请求）, 如 `Content-Type: application/x-www-form-urlencoded`
	* Host 表示当前请求访问的目标地址（主机:端口）, 如 `Host: www.google.com:8080`
	* If-Match 设置客户端的ETag,当时客户端ETag和服务器生成的ETag一致才执行，适用于更新自从上次更新之后没有改变的资源, 如 `If-Match: "737060cd8c284d8af7ad3082f209582d`
	* If-Modified-Since 设置更新时间，从更新时间到服务端接受请求这段时间内如果资源没有改变，允许服务端返回304 Not Modified, 如 `If-Modified-Since: Sat, 29 Oct 1994 19:43:31 GMT`
	* If-None-Match 设置客户端ETag，如果和服务端接受请求生成的ETage相同，允许服务端返回304 Not Modified, 如 `If-None-Match: "737060cd8c284d8af7ad3082f209582d"`
	* If-Range 设置客户端ETag，如果和服务端接受请求生成的ETage相同，返回缺失的实体部分；否则返回整个新的实体, 如 `If-Range: "737060cd8c284d8af7ad3082f209582d"`
	* If-Unmodified-Since 设置更新时间，只有从更新时间到服务端接受请求这段时间内实体没有改变，服务端才会发送响应, 如 `If-Unmodified-Since: Sat, 29 Oct 1994 19:43:31 GMT`
	* Referer  表示当前请求来自于哪里, 如 `Referer: http://www.google.com/android/index.jsp`
	* User-Agent 表示浏览器类型, 如 `User-Agent: Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0)`
	* Cookie 表示浏览器保存的cookie信息, 如 `Cookie: name=eric`
	* Connection 表示浏览器跟服务器连接状态, 如 `Connection: close/Keep-Alive`
	* Date 表示请求发出的时间, 如 `Date: Tue, 11 Jul 2000 18:23:51 GMT`
	* Range 表示只请求实体的一部分，指定范围, 如 `Range: bytes=500-999`
	* From 表示发出请求的用户的Email
	* Expect 标识客户端需要的特殊浏览器行为, 如 `Expect: 100-continue`
	* Forwarded 披露客户端通过http代理连接web服务的源信息, 如 `Forwarded: for=192.0.2.60;proto=http;by=203.0.113.43`
	* Max-Forwards 限制代理或网关转发消息的次数, 如 `Max-Forwards: 10`
	* Origin 标识跨域资源请求（请求服务端设置Access-Control-Allow-Origin响应字段), 如 `Origin: http://www.example-social-network.com`
	* Proxy-Authorization 为连接代理授权认证信息, 如 `Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==`
	* TE 设置用户代理期望接受的传输编码格式，和响应头中的Transfer-Encoding字段一样, 如 `TE: trailers, deflate`
	
实体主体通常不用, POST 场景下可能用于提交数据。 

* 请求方法

	| 方法        | 方法描述          |
	|:----:      | :----:     |
	| GET        | 请求获取服务器资源, 返回报文主体   |
	| POST        | 向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST请求可能会导致新的资源的建立和/或已有资源的修改  |  
	| HEAD        | 用于确认URI的有效性及资源更新的日期时间等, 和GET一样, 只不过不返回主体  | 
	| PUT        | 从客户端向服务器传送的数据取代指定的文档的内容 | 
	| DELETE        | 请求服务器删除指定的页面  | 
	| CONNECT        | HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器。要求使用隧道协议连接代理 | 
	| OPTIONS        | 允许客户端查看服务器的性能, 询问支持的方法  | 
	| TRACE        | 回显服务器收到的请求, 主要用于测试或诊断, 追踪路径  | 


**响应报文**

<img src="../pic/http_2.png" width = "400" height = "150" alt="图片名称" align=center />

* 开始行

	上述图为响应报文头部, 开始行中 `版本`, `状态码`, `短语` 之间需要用空格隔开

	* 版本, 为http协议版本
	* 状态码, 表示响应的行为结果
	* 短语, 为状态码进一步描述

	比如 `HTTP/1.1 200 OK ` 表示服务端成功处理请求并返回 200 状态码

	
* 头部响应头/实体头
	* Location 表示重定向的地址，该头和302的状态码一起使用, 如 `Location: http://www.google.com/android/index.jsp`
	* Server 表示服务器的类型, 如 `Server:apache tomcat`
	* Content-Encoding 表示服务器发送给浏览器的数据压缩类型, 如 `Content-Encoding: gzip`
	* Content-Length 表示服务器发送给浏览器的数据长度, 如 `Content-Length: 80`
	* Content-Language 表示服务器支持的语言, 如 `Content-Language: zh-cn`
	* Content-Type 表示服务器发送给浏览器的数据类型及内容编码, 如 `Content-Type: text/html; charset=GB2312`
	* Last-Modified  表示服务器资源的最后修改时间, 如 `Last-Modified: Tue, 11 Jul 2019 20:19:31 GMT`
	* Refresh 表示定时刷新, 如 `Refresh: 1;url=http://www.google.com`
	* Content-Disposition 表示告诉浏览器以下载方式打开资源, 用于文件下载, 如 `Content-Disposition: attachment; filename=something.zip`
	* Transfer-Encoding 表示浏览器跟服务器连接状态, 如 `Connection: close/Keep-Alive`
	* Date 表示请求发出的时间, 如 `Date: Tue, 11 Jul 2000 18:23:51 GMT`
	* Set-Cookie 表示服务器发送给浏览器的cookie信息, 如 `Set-Cookie:SS=Q0=5Lb_nQ; path=/search`
	* Expires 通知浏览器缓存操作, 如 `Expires: -1` 
	* Connection 表示服务器和浏览器的连接状态, 如 `Connection: close/Keep-Alive`
	* Transfer-Encoding 表示服务端使用分块传输, 如 `Transfer-Encoding: chunked`
	* Cache-Control 表示客户端和服务端的缓存行为, 请求头字段也可以设置, 如 `Cache-Control: no-cache`
	* Pragma 表示编译报文时的指令操作, 如 `Pragma: no-cache`
	* Accept-Ranges 表示服务器支持http中的Range功能, 常用于断点续传, 如 `Range: bytes=0-100`

* 响应状态码

	| 状态吗        | 信息      		|短语原因          |
	|:----:      | :----:     		|:----:   |
	| 1xx        | Informational(信息性状态码)   | 接收的请求正在处理|
	| 100        | Continue  		| 客户端应当继续发送请求|
	| 101        | Switching Protocols | 服务器已经理解了客户端的请求，并将通过Upgrade 消息头通知客户端采用不同的协议来完成这个请求|
	| 102        | Processing 		| 由WebDAV（RFC 2518）扩展的状态码，代表处理将被继续执行|
	| 2XX        | Success(成功状态码) | 请求正常处理完毕|
	| 200        | OK 				| 从客户端发来的请求在服务端正常处理|
	| 204        | No Content 		| 服务端接收的请求已成功处理，但在返回的响应报文中不含实体的主体部分，另外也不允许返回任何实体的主体|
	| 206        | Partial Content  | 客户端进行了范围请求， 而服务器成功执行了这部分请求，响应报文中包含由Content—Range 指定的范围的实体内容|
	| 3XX        | Redirection(重定向状态码)  | 需要进行附加操作已完成请求|
	| 301        | Moved Permanently  | 永久性重定向|
	| 302        | Found 			| 临时性重定向|
	| 303        | See Other  		| 由于对应的资源存在着另一个uri，应使用GET方法定向获取请求的资源|
	| 304        | Not Modified  	| 客户端发送附带条件的请求，也就是资源找到了，但是没有符合条件请求|
	| 307        | Tempoeary Redirect | 临时重定向，和302很相似，但是会根据游览器不同导致出现不同问题|
	| 4XX        | Client Error(客户端错误状态码)  | 服务器无法处理请求|
	| 400        | Bad Request  	| 请求报文中存在语法错误：游览器会像200 OK 一样对待改状态码|
	| 401        | Unauthorized  	| 发送的请求需要通过http认证(BASIC认证、DIGEST认证)的认证信息，如果前面已经请求过一次，证明认证失败|
	| 403        | Forbidden 		| 请求资源的访问被服务器拒绝|
	| 404        | Not Found  		| 服务器无法找到请求的资源|
	| 5XX        | Server Error(服务端错误状态码)  | 服务器处理请求出错|
	| 500        | Internal Server Error  | 服务器端知悉请求时发生错误|
	| 503        | Service Unavailable | 服务器暂时处于超负载或正在进行停机维护，现在无法处理请求|


<h3 id="3">HTTPS协议</h3>

HTTPS为了实现HTTP安全通, 在HTTP协议基础下加入SSL层，HTTPS的安全基础是SSL，因此加密的详细内容就需要SSL。

**HTTPS协议的主要作用**

* 建立一个信息安全通道，来保证数据传输的安全
* 确认网站的真实性

**何为 SSL ？** 

SSL(Secure Sockets Layer 安全套接层),及其继任者传输层安全（Transport Layer Security，TLS）是为网络通信提供安全及数据完整性的一种安全协议。TLS与SSL在传输层对网络连接进行加密, 在网络模型中其实并不存在 SSL, 严格来说 SSL 协议应该在 TCP/IP 协议及各应用层协议之间, 为数据通讯提供安全支持。由于经历过几个版本升级, 旧的SSL协议基本已经被废除, 其继任者为 TLS协议。所以一般都称为 SSL/TLS。 在可靠的传输协议如 TCP 之上为高层协议提供 `数据封装`, `压缩`, `加密` 等基础功能。  而建立安全通过是需要经过 *SSL协议握手流程* , 该流程基于 *TCP三次握手* 建立连接基础之上。 

<h4 id="3_1">TCP连接的建立和关闭</h4>

**三次握手建立连接**

1. 客户端发送 SYN = 1, seq = x (CLOSED -> SYN-SENT)
2. 服务端发送 SYN=1, ACK=1, ack=x+1, seq=y（CLOSED LISTEN -> SYN-RCVD）
3. 客户端发送 ACK=1, seq=x+1, ack=y+1 （SYN-SENT -> ESTAB-LISHED)

为什么需要最后一次确认？如果发送方发送第一个请求没有丢失在网络上长时间滞留，发送的第二个请求被接收方连接，等到后面的释放后某个时间第一个请求才到达。此时如果没有进行确认，那么就会由新的连接建立。 

**四次握手关闭连接** 

服务端关闭TCP链接，如果客户端增加Connection：keep-alive表示客户端和服务端继续保持连接

1. 客户端发送 FIN=1, seq=u （ESTAB-LISHED -> FIN-WAIT-1）
2. 服务端发送 ACK=1, ack=u+1, seq=v （ESTAB-LISHED -> COLSE-WAIT）

经过这两个流程之后, 客户端状态发生改变(FIN-WAIT-1 -> FIN-WAIT-2), 此时 TCP 处理半关闭状态, 客户端已经没有数据需要发送到服务端, 但是服务端如果存在有数据需要发送, 则客户端依然需要接收。

3. 服务端发送 FIN=1, ACK=1, ack=u+1, seq=w（COLSE-WAIT -> LAST-ACK)
4. 客户端发送 ACK=1, seq=u+1, ack=w+1（FIN-WAIT-2 -> TIME-WAIT)

此时客户端等待2MSL时间 TIME-WAIT才结束，进入CLOSED，服务端收到确认后直接CLOSED 为什么需要2MSL时间？

* 为了保证客户端发送的最后一个ACK报文段能到达，如果服务端没有在2MSL时间内收到请求，则会重传
* 为了在2MSL时间内让本连接持续的时间内所产生的所有报文段从网络中消失，使得下一次新的连接中不会出现这种旧的连接请求报文段

<h4 id="3_2">证书</h4>

在了解 SSL 协议握手流程之前, 先来了解证书相关的概念

* 数字证书 

	一种文件的名称，好比一个机构或人的签名，能够证明这个机构或人的真实性
* 数字签名

	附加在报文上的特殊加密校验码，所谓校验和。一般策略是是对报文消息先进行哈希，得到固定长度的哈希值，然后在对哈希值进行签名

* Certificate Authority（证书颁发机构)

	CA会下发给Server一个证书，该证书是可被信任的，Server发送数据会Client的时候，Client通过校验该证书来检验Server是否合法有效
* Certificate(证书), 证书的内容可以在 mac 应用程序-实用工具中查看
    * 颁发机构的名称
        * 主题名称 ， 比如 GeoTrustGlobal CA
        * 签发者信息，比如序列号，版本，签名算法（带RSA加密的SHA-1）
    * 过期事件
    * 公共密钥信息
        * 算法及参数
        * 公开密钥，比如256字节：DA CC 18 63 ...
        * 指数，比如65537
        * 密钥大小，比如2048位
        * 密钥使用，比如 加密, 验证, 包装, 派生
        * 签名，比如256字节：35 E3 29 6A ...，使用指纹算法算出来的编码
    * 一些扩展信息，比如密钥使用，基础约束，主题密钥标识符，授权密钥标识符
    * 指纹（数字指纹，消息摘要）
        * SHA-256（指纹算法） FF 85 6A 2D 25 1D CD ..
        * SHA-1（指纹算法） DE 28 F4 A4 FF E5 ...
        * 实际上指纹和指纹算法都被经过机构CA私钥加密过。客户端需要把证书的明文内容通过指纹算法(SHA-256/SHA-1)编码得到指纹并用机构自己的CA自己的私钥加密，然后和证书一起发布，用于证明这个证书是CA发布的且没有被修改

如果自己搭建的服务器, 并支持 HTTPS 可以自己生成 CA 证书, 绕过了第三方机构的认证, 所以访问者应该懂得这内部的风险。下面讲一下如何生成 CA

使用 `openssl` 进行 CA 根证书

* 生成 CA private key 

	`openssl genrsa -out ca.key 2048`

* 生成 CSR 

	`openssl req -new -key ca.key -out ca.csr`
* 生成 CA 根证书 

	`openssl x509 -req -days 365 -in ca.csr -signkey ca.key -out ca.crt`

还需要生成用户证书

* 生成 private key 

	```
	openssl genrsa -aes128 -out user.key 2048
	openssl rsa -text -in user.key
 	```
* 生成 public key 

	```
	openssl rsa -in user.key -pubout -out user-public.key
	openssl req -text -pubin user-public.key -noout
 	```
* 生成证书 

	```
	openssl req -new -key user.key -out user.csr
	openssl req -text -in user.csr -noout
	openssl ca -in user.csr -out user.crt -cert ca.crt -keyfile ca.key -config openssl.cnf
 	```	
 	
最后能看到生成的证书内容

```
Certificate Request:
     Data:
         Version: 0 (0x0)
         Subject: C=us, ST=us, L=us, O=us, OU=us, CN=us/emailAddress=us
         Subject Public Key Info:
             Public Key Algorithm: rsaEncryption
                 Public-Key: (2048 bit)
                 Modulus:
                     00:b9:8f:be:ac:92:f4:bc:0b:a9:69:2d:17:d7:ed:
                     8e:13:ea:85:2d:c0:50:d5:de:1e:23:2c:a8:37:d6:
                     a3:28:2e:93:92:69:b2:1a:04:36:d9:6d:94:4d:57:
                     24:f4:5b:5d:bc:b4:55:31:4b:81:6a:9c:6f:43:1b:
                     58:45:16:ce:e5:fd:42:2a:7e:fb:e5:69:47:38:f7:
                     40:32:31:2d:67:f6:02:af:65:ac:87:0e:54:85:71:
                     c3:5d:2a:40:15:00:75:52:0a:ba:4a:08:57:66:98:
                     75:7d:2b:2d:5c:a9:be:96:2d:76:11:2e:14:5c:92:
                     05:e0:21:43:73:6d:4a:ac:0a:a5:e5:4d:72:33:a8:
                     5a:5f:67:7c:86:d1:95:76:15:4a:e1:94:f5:d4:3b:
                     4b:9f:70:c8:86:1f:cb:24:48:4d:da:be:07:bc:e4:
                     35:4d:86:c6:86:d1:ee:52:05:c6:72:bd:34:b3:a6:
                     a0:a0:70:37:a9:78:c1:f5:30:23:04:b2:3f:3e:cd:
                     49:f1:7a:93:43:10:8e:6d:34:8e:9a:de:d1:15:65:
                     6d:df:2c:00:74:dc:f1:71:70:21:6a:c9:b3:af:9a:
                     f9:08:68:f3:5e:ea:ba:f5:39:49:c2:fd:b9:5c:18:
                     26:ad:15:8c:76:ca:93:54:48:36:7c:4d:f9:cc:d9:
                     be:df
                 Exponent: 65537 (0x10001)
         Attributes:
             a0:00
     Signature Algorithm: sha256WithRSAEncryption
          62:23:50:77:33:39:9a:0a:7f:f6:2e:6c:65:18:8b:b9:ef:0d:
          ee:07:14:39:8e:9f:ce:1c:a4:f3:06:3f:dc:9e:b9:f9:8a:48:
          11:44:3b:23:ec:01:d3:de:fd:13:d4:5d:d5:f9:40:26:d4:7d:
          d4:c9:d5:49:0d:37:0e:05:c7:6c:2d:97:7d:5e:39:f6:ea:e6:
          e8:b5:5d:1e:7c:ba:57:ce:dd:13:4f:62:64:2a:49:ca:5a:4f:
          4d:eb:53:68:4a:49:46:7f:4e:16:b4:51:80:7f:e3:88:e4:bb:
          61:9e:87:a6:55:75:58:d3:84:6f:5a:c5:1d:ab:9f:87:be:fc:
          77:ed:ac:f1:f4:de:77:96:ae:f6:3a:19:e7:a2:95:1f:1c:44:
          65:bb:d2:6e:af:8b:b4:42:24:c9:7c:e0:6e:57:26:1d:f4:6e:
          e9:1d:b1:6f:85:9c:c3:12:d4:f0:20:35:38:4d:ce:00:45:eb:
          6e:16:51:dd:39:c5:4f:28:45:04:07:d3:e7:d1:3f:be:86:7e:
          51:53:1e:f0:0a:83:4b:2d:66:fb:a9:3f:e3:ec:32:81:08:6c:
          6e:1e:24:3c:df:5a:22:ef:75:b2:a7:53:f7:31:93:ec:69:59:
          5a:5f:be:0f:c1:12:7e:25:86:95:a9:f4:43:67:b9:ed:ad:5c:
          6e:51:9f:9e
          1f:cd:63:d4

```
**如何进行数字签名**

<img src="../pic/signing.jpg" width = "300" height = "400" alt="图片名称" align=center />

上图来自于互联网, 整个流程成大致分为一下几个步骤

1. Data（数据电文）先经过哈希算法计算得到数据摘要 A
2. 使用签名私钥对数字摘要做加密, 得到数字签名
3. 将数据电文, 数字签名发送到接收方

**如何进行签名认证**

<img src="../pic/verification.jpg" width = "300" height = "400" alt="图片名称" align=center />

上图来自于互联网, 整个流程成大致分为一下几个步骤

1. 取出数据电文, 经过哈希算法得到数据摘要 B
2. 取出签名, 经过公钥揭秘得到数字摘要 A
3. 如果 A = B, 则默认签名是有效的

> 如果中间拦截了发送方的数据, 把电文和签名都改了怎么办？
> 因为生成签名需要私钥, 而接收方的公钥时解不开拦截者的私钥, 所以拦截者得得到发送方的私钥才行, 这就涉及到私钥怎么保护啦。还有一个情况就是直接替换接收方的公钥, 用拦截者的公钥换掉原来的公钥, 这可怎么办呢 ？这能通过数字证书来解决。

接下来看数字证书如何解决公钥被替换的问题！

**数字证书解决公钥被替换**

1. 发送方在CA为公钥做认证, CA用自己的私钥对 “A的公钥, 附加一些相关信息” 做加密生成“数字证书”
2. 所以 `如何进行数字签名` 中第三步骤添加把 “数字证书” 也发送给接收方
3. 接收方收到用 CA 公钥解开这份证书, 得到 A的公钥, 然后用 A的公钥去计算对比数字摘要就行了。


<h4 id="3_3">SSL/TLS握手流程</h4>

在了解握手流程之前, 先看一张整体的流程图（图片来自于互联网）。 
<img src="../pic/ssl_1.png" width = "600" height = "400" alt="图片名称" align=center />

在 TCP 完成三次握手建立连接之后, HTTPS 开始加密认证握手流程。
 
整个构成抽象成 4 个主要流程

1. **client_hello** 
	* 传输方式: 明文传输
	* 申明支持的协议版本，从低到高依次 SSLv2 SSLv3 TLSv1 TLSv1.1 TLSv1.2，当前基本不再使用低于 TLSv1 的版本
	* 申明支持的加密套件 cipher suites 列表
		* 认证算法 Au (身份验证)
		* 密钥交换算法 KeyExchange(密钥协商)
		* 对称加密算法 Enc (信息加密)
    	* 信息摘要 Mac(完整性校验)
    * 申明支持的压缩算法 compression methods 列表，用于后续的信息压缩传输
    * 生成 随机数 random_c


2. **server_hello** ->  **certificate** -> **sever_hello_done**
	1. **server_hello**  
		* 传输方式: 明文传输
	   * 确定协议版本 version
	   * 确定加密套件 cipher suite
	   * 确定压缩算法 compression method
	   * 生成随机数 random_s
	2. **certificate**
		* 传输方式: 明文传输
		* 数字证书: CA证书{发布机构CA，证书有效期，证书所有者，公钥，加密算法，Hash算法} + 数字签名
		* 数字签名：把CA证书做一次Hash计算生成摘要（128位），摘要通过私钥进行加密，之所以要再使用私钥加密是因为防止不怀好意的人也可以修改信息内容的同时也修改hash值，从而让它们可以相匹配
	3. **sever_hello_done**
		* 传输方式: 明文传输
		* 通知客户端 server_hello 信息发送结束

3. **client_key_exchange** -> **change_cipher_spec** -> **encrypted_handshake_message**
	1. **client_key_exchange**
		1. 校验证书
			* 读取证书所有者有效期等信息进行校验，查找内置的收信人证书发布机构CA与证书CA相对比，校验是否是合法机构颁发；
		        证书链的可信性 trusted certificate path，发行服务器证书的CA是否可靠
		        证书是否吊销 revocation，有两类方式离线 CRL 与在线 OCSP，不同的客户端行为会不同
		        有效期 expiry date，证书是否在有效时间范围
		        域名 domain，核查证书域名是否与当前的访问域名匹配，匹配规则后续分析
		    * 提取CA的公钥对 "数字证书" 末尾的数字签名进行解密，得到原始证书 hash 值 a
		    * 按照CA证书中的指纹算法计算原始证书得到 hash 值 b
		    * 对比 a 与 b 是否相同   
		2. 在检验通过之后产生随机数 Pre-master, 并用证书公钥加密，发送给服务器
		3. 根据两个明文随机数 random_C 和 random_S 与自己计算产生的 Pre-master，计算得到协商密钥; enc_key=Fuc(random_C, random_S, Pre-Master)
	2. **change_cipher_spec**
		* 客户端通知服务器后续的通信都采用协商的通信密钥和加密算法进行加密通信
	3. **encrypted_handshake_message**
		* 结合之前所有通信参数的 hash 值与其它相关信息生成一段数据，采用协商密钥 session secret 与算法进行加密，然后发送给服务器用于数据与握手验证

4. **change_cipher_spec** -> **finish**
	1. **change_cipher_spec**
		1. 服务器用私钥解密加密的 Pre-master 数据，基于之前交换的两个明文随机数 random_C 和 random_S，计算得到协商密钥:enc_key=Fuc(random_C, random_S, Pre-Master)
		2. 计算之前所有接收信息的 hash 值，然后解密客户端发送的 encrypted_handshake_message，验证数据和密钥正确性
		3. 验证通过之后，服务器同样发送 change_cipher_spec 以告知客户端后续的通信都采用协商的密钥与算法进行加密通信
		4. 服务器也结合所有当前的通信参数信息生成一段数据并采用协商密钥 session secret 与算法加密并发送到客户端
	2. **finish**
		* 客户端计算所有接收信息的 hash 值，并采用协商密钥解密 encrypted_handshake_message，验证服务器发送的数据和密钥，验证通过则握手完成



了解上述加密流程之后, 总结下 HTTP 和 HTTPS 的区别

* https协议需要ca申请证书, 用于校验服务器的安全性, 而 http协议不需要校验
* http是超文本传输协议，信息是明文传输，https则是具有安全性的ssl加密传输协议
* http连接简单无状态, HTTPS协议是由SSL+HTTP协议构建的可进行加密传输、身份认证的网络协议，更为安全。


<h3 id="4">解释一些疑惑</h3>
* 客户端如何校验服务端返回的证书是否合法呢？ 
	1. 首先应用程序读取证书中的Issuer(发布机构)为"xx CA", 然后会在操作系统中受信任的发布机构的证书中去找"xx CA"的证书, 如果找不到, 那说明证书可能有问题, 程序会给出一个错误信息。
	2. 如果在系统中找到了"xx CA"的证书, 那么应用程序就会从证书中取出"xx CA"的公钥, 然后对的证书里面的数字签名。 用这个公钥进行解密得到指纹, 然后使用这个指纹算法计算证书的指纹, 将这个计算的指纹与放在证书中的指纹对比, 如果一致, 说明证书肯定没有被修改过并且证书是"xx CA" 发布的。
	3. 只需要校验这个证书所有者是否是服务端, 如果是则使用证书公共密钥信息中的公开密钥与服务端通讯。

* 为何经Hash算法计算摘要之后，还需要用私钥进行加密?
	1. 因为避免拦截者把明文报文和摘要都进行修改，所以需要用私钥再对摘要进行加密，只有对应的公钥才能正确得到摘要信息

* 客户端第一次发送随机串要求对方用私钥加密验证身份时，服务端如何确认该随机串来自客户端？
	1. 	服务端选择加密被Hash过的随机串再返回，降低被破解的几率，而客户端也需要校验解开之后的Hash与原字符串计算的Hash是否相同来确认对方是否是服务器 

* 为什么需要涉及一个认证+非对称加密+对称加密通讯, 而不一开始使用对称加密或者非对称加密？
	1. 	如果一开始使用对称加密啊，由于不同的客户端服务端数量庞大，维护成本高，安全级别不同，容易泄漏。
	2. 如果一开始使用非堆成加密，公钥是公开的，如果服务端被返回被截获，则可以使用公钥解密。

* 针对上述TLS流程如何进行攻击？
	1. 把1个合法有效的SSL证书误签发给了假冒者
	2. 破解SSL证书签发CA的私钥
	3. SSL证书签发CA的私钥泄露
	4. 破解SSL证书的私钥 

* 什么是对称/非对称加密, 什么是散列算法。
	1. 对称密码算法，是指加密和解密使用相同的密钥，典型的有DES、RC5、IDEA（分组加密），RC4（序列加密）
	2. 非对称密码算法：又称为公钥加密算法，是指加密和解密使用不同的密钥（公开的公钥用于加密，私有的私钥用于解密）。比如A发送，B接收，A想确保消息只有B看到，需要B生成一对公私钥，并拿到B的公钥。于是A用这个公钥加密消息，B收到密文后用自己的与之匹配的私钥解密即可。反过来也可以用私钥加密公钥解密。也就是说对于给定的公钥有且只有与之匹配的私钥可以解密，对于给定的私钥，有且只有与之匹配的公钥可以解密。典型的算法有RSA，DSA，DH；
	3. 散列算法：散列变换是指把文件内容通过某种公开的算法，变成固定长度的值（散列值），这个过程可以使用密钥也可以不使用。这种散列变换是不可逆的，也就是说不能从散列值变成原文。因此，散列变换通常用于验证原文是否被篡改。典型的算法有：MD5，SHA，Base64，CRC等。在散列算法（也称摘要算法）中，有两个概念，强无碰撞和弱无碰撞。弱无碰撞是对给定的消息x，就是对你想伪造的明文，进行运算得出相同的摘要信息。也就是说你可以控制明文的内容。强无碰撞是指能找到相同的摘要信息，但伪造的明文是什么并不知道。

* SSL/TLS流程涉及加密算法及校验算法, 常见的加密和校验有哪些？
	1. 非对称加密算法用于在握手过程中加密生成的密码 非对称加密算法：RSA，DSA/DSS
	2. 对称加密算法用于对真正传输的数据进行加密 对称加密算法：AES，RC4，3DES
	3. HASH算法用于验证数据的完整性 HASH算法：MD5，SHA1，SHA256  

	TLS握手过程中如果有任何错误，都会使加密连接断开，从而阻止了隐私信息的传输

* 如何产生一个签名, 怎么确保发送的签名没有被修改过。
	1. 使用哈希算法, 将任意长度的信息转换为较短的固定长度的值，通常其长度要比信息小得多，且算法不可逆。例如：MD5、SHA-1、SHA-2、SHA-256等等
	2. 签名就是在信息的后面再加上一段内容（信息经过hash后的值），可以证明信息没有被修改过。hash值一般都会加密后（也就是签名）再和信息一起发送，以保证这个hash值不被修改。

* 每次建立 TLS 时都会需要 TCP 三次握手, 如何优化这个流程？
	1. 使用会话缓存握手流程。 建立 TCP 连接需要进行三次握手, HTTPS 在 TCP 之上又加上了 TLS 的握手过, TLS 握手就需要消耗两个 RTT（Round-Trip Time，往返时间), 也就是说单建立链接的过程是相当耗时的。 如果每次请求建立链接都要进行这样繁琐的握手过程，那显然是不能忍的, 所以就提出了对 TLS 已经协商好的密钥重用。 有两种方法:会话标识session ID 和会话记录 session ticket.


篇幅知识来源于互联网文章及wiki知识总结, 如有错误望纠正。

欢迎随时探讨！
