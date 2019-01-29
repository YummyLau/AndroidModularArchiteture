Http（80） Https（443）访问过程

。。略握手流程

。。ssl加密过程

1. client_hello - 明文传输
    * 支持的协议版本，从低到高依次 SSLv2 SSLv3 TLSv1 TLSv1.1 TLSv1.2，当前基本不再使用低于 TLSv1 的版本
    * 支持的加密套件 cipher suites 列表
        * 认证算法 Au (身份验证)
        * 密钥交换算法 KeyExchange(密钥协商)
        * 对称加密算法 Enc (信息加密)
        * 信息摘要 Mac(完整性校验)
    * 支持的压缩算法 compression methods 列表，用于后续的信息压缩传输
    * 随机数 random_c





2. server_hello - 明文传输
    * 确定协议版本 version
    * 确定加密套件 cipher suite
    * 确定压缩算法 compression method
    * 随机数 random_s

3. server_certificate - 明文传输
数字证书，包括：CA证书{发布机构CA，证书有效期，证书所有者，公钥，加密算法，Hash算法} + 数字签名
    数字签名：把CA证书做一次Hash计算生成摘要（128位），摘要通过私钥进行加密，之所以要再使用私钥加密是因为防止不怀好意的人也可以修改信息内容的同时也修改hash值，从而让它们可以相匹配。

4. sever_hello_done - 明文传输
    * 通知客户端 server_hello 信息发送结束






5. 校验证书
    * 读取证书所有者有效期等信息进行校验，查找内置的收信人证书发布机构CA与证书CA相对比，校验是否是合法机构颁发；
        证书链的可信性 trusted certificate path，发行服务器证书的CA是否可靠
        证书是否吊销 revocation，有两类方式离线 CRL 与在线 OCSP，不同的客户端行为会不同
        有效期 expiry date，证书是否在有效时间范围
        域名 domain，核查证书域名是否与当前的访问域名匹配，匹配规则后续分析
    * 提取CA的公钥对 "数字证书" 末尾的数字签名进行解密，得到原始证书 hash 值 a
    * 按照CA证书中的指纹算法计算原始证书得到 hash 值 b
    * 对比 a 与 b 是否相同




6. client_key_exchange - 非对称加密传输
    * 合法性验证通过之后，客户端计算产生随机数字 Pre-master，并用证书公钥加密，发送给服务器.
    * 根据两个明文随机数 random_C 和 random_S 与自己计算产生的 Pre-master，计算得到协商密钥; enc_key=Fuc(random_C, random_S, Pre-Master)

7. change_cipher_spec - 非对称加密传输
    * 客户端通知服务器后续的通信都采用协商的通信密钥和加密算法进行加密通信

8. encrypted_handshake_message - 非对称加密传输
    * 结合之前所有通信参数的 hash 值与其它相关信息生成一段数据，采用协商密钥 session secret 与算法进行加密，然后发送给服务器用于数据与握手验证





9. 服务端确定通讯规则 - 对称加密传输
    * 服务器用私钥解密加密的 Pre-master 数据，基于之前交换的两个明文随机数 random_C 和 random_S，计算得到协商密钥:enc_key=Fuc(random_C, random_S, Pre-Master);
    * 计算之前所有接收信息的 hash 值，然后解密客户端发送的 encrypted_handshake_message，验证数据和密钥正确性

10. change_cipher_spec - 对称加密传输
    * 验证通过之后，服务器同样发送 change_cipher_spec 以告知客户端后续的通信都采用协商的密钥与算法进行加密通信;

11. encrypted_handshake_message - 对称加密传输
    * 服务器也结合所有当前的通信参数信息生成一段数据并采用协商密钥 session secret 与算法加密并发送到客户端;






12. 握手结束
    * 客户端计算所有接收信息的 hash 值，并采用协商密钥解密 encrypted_handshake_message，验证服务器发送的数据和密钥，验证通过则握手完成;


思考的点：
1。 如果一开始使用对称加密啊，由于不同的客户端服务端数量庞大，维护成本高，安全级别不同，容易泄漏。
2。 如果一开始使用非堆成加密，公钥是公开的，如果服务端被返回被截获，则可以使用公钥解密。

https://hypercarol.com/https-tls-session/

经过tcp握手后，进行tls握手

TSL：
加密
身份验证
消息完整性校验


证书机制/证书验证
在TLS中，我们需要证书来保证你所访问的服务器是真实的，可信的。
看这张图我们来讨论下证书的验证过程。

攻击手段
1）一个合法有效的SSL证书误签发给了假冒者
2）破解SSL证书签发CA的私钥
3）SSL证书签发CA的私钥泄露
4）破解SSL证书的私钥

非对称加密算法用于在握手过程中加密生成的密码 非对称加密算法：RSA，DSA/DSS
对称加密算法用于对真正传输的数据进行加密 对称加密算法：AES，RC4，3DES
HASH算法用于验证数据的完整性 HASH算法：MD5，SHA1，SHA256
TLS握手过程中如果有任何错误，都会使加密连接断开，从而阻止了隐私信息的传输。
哈希算法
将任意长度的信息转换为较短的固定长度的值，通常其长度要比信息小得多，且算法不可逆。
例如：MD5、SHA-1、SHA-2、SHA-256 等
签名就是在信息的后面再加上一段内容（信息经过hash后的值），可以证明信息没有被修改过。hash值一般都会加密后（也就是签名）再和信息一起发送，以保证这个hash值不被修改。


会话缓存握手过程
TTP 建立 TCP 链接需要进行三次握手，HTTPS 在 TCP 之上又加上了 TLS 的握手过程，TLS 握手就需要消耗两个 RTT（Round-Trip Time，往返时间）,也就是说单建立链接的过程是相当耗时的。如果每次请求建立链接都要进行这样繁琐的握手过程，那显然是不能忍的，所以就提出了对 TLS 已经协商好的密钥重用。有两种方法:会话标识session ID 和会话记录 session ticket

