package example.componentlib.net;

import android.util.SparseArray;


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class WeiboHttpStatusGetter {

    public static final SparseArray<WeiboHttpStatus> CODE_SPARSE_ARRAY = new SparseArray();

    static {
        //系统级别错误代码
        CODE_SPARSE_ARRAY.append(10001, new WeiboHttpStatus(10001, "System error", "系统错误"));
        CODE_SPARSE_ARRAY.append(10002, new WeiboHttpStatus(10002, "Service unavailable", "服务暂停"));
        CODE_SPARSE_ARRAY.append(10003, new WeiboHttpStatus(10003, "Remote service error", "远程服务错误"));
        CODE_SPARSE_ARRAY.append(10004, new WeiboHttpStatus(10004, "IP limit", "IP限制不能请求该资源"));
        CODE_SPARSE_ARRAY.append(10005, new WeiboHttpStatus(10005, "Permission denied, need a high level appkey", "该资源需要appkey拥有授权"));
        CODE_SPARSE_ARRAY.append(10006, new WeiboHttpStatus(10006, "Source paramter (appkey) is missing", "缺少source (appkey) 参数"));
        CODE_SPARSE_ARRAY.append(10007, new WeiboHttpStatus(10007, "Unsupport mediatype (%s)", "不支持的MediaType (%s)"));
        CODE_SPARSE_ARRAY.append(10008, new WeiboHttpStatus(10008, "Param error, see doc for more info", "参数错误，请参考API文档"));
        CODE_SPARSE_ARRAY.append(10009, new WeiboHttpStatus(10009, "Too many pending tasks, system is busy", "任务过多，系统繁忙"));
        CODE_SPARSE_ARRAY.append(10010, new WeiboHttpStatus(10010, "Job expired", "任务超时"));
        CODE_SPARSE_ARRAY.append(10011, new WeiboHttpStatus(10011, "RPC error", "RPC错误"));
        CODE_SPARSE_ARRAY.append(10012, new WeiboHttpStatus(10012, "Illegal request", "非法请求"));
        CODE_SPARSE_ARRAY.append(10013, new WeiboHttpStatus(10013, "Invalid weibo user", "不合法的微博用户"));
        CODE_SPARSE_ARRAY.append(10014, new WeiboHttpStatus(10014, "Insufficient app permissions", "应用的接口访问权限受限"));
        CODE_SPARSE_ARRAY.append(10016, new WeiboHttpStatus(10016, "Miss required parameter (%s) , see doc for more info", "缺失必选参数 (%s)，请参考API文档"));
        CODE_SPARSE_ARRAY.append(10017, new WeiboHttpStatus(10017, "Parameter (%s)'s value invalid, expect (%s) , but get (%s) , see doc for more info", "参数值非法，需为 (%s)，实际为 (%s)，请参考API文档"));
        CODE_SPARSE_ARRAY.append(10018, new WeiboHttpStatus(10018, "Request body length over limit", "请求长度超过限制"));
        CODE_SPARSE_ARRAY.append(10020, new WeiboHttpStatus(10020, "Request api not found", "接口不存在"));
        CODE_SPARSE_ARRAY.append(10021, new WeiboHttpStatus(10021, "HTTP method is not suported for this request", "请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式"));
        CODE_SPARSE_ARRAY.append(10022, new WeiboHttpStatus(10022, "IP requests out of rate limit", "IP请求频次超过上限"));
        CODE_SPARSE_ARRAY.append(10023, new WeiboHttpStatus(10023, "User requests out of rate limit", "用户请求频次超过上限"));
        CODE_SPARSE_ARRAY.append(10024, new WeiboHttpStatus(10024, "User requests for (%s) out of rate limit", "用户请求特殊接口 (%s) 频次超过上限"));

        //服务级别错误代码
        CODE_SPARSE_ARRAY.append(20001, new WeiboHttpStatus(20001, "IDs is null", "\tIDs参数为空"));
        CODE_SPARSE_ARRAY.append(20002, new WeiboHttpStatus(20002, "Uid parameter is null", "Uid参数为空"));
        CODE_SPARSE_ARRAY.append(20003, new WeiboHttpStatus(20003, "User does not exists", "用户不存在"));
        CODE_SPARSE_ARRAY.append(20005, new WeiboHttpStatus(20005, "Unsupported image type, only suport JPG, GIF, PNG", "\t不支持的图片类型，仅仅支持JPG、GIF、PNG"));
        CODE_SPARSE_ARRAY.append(20006, new WeiboHttpStatus(20006, "\tImage size too large", "图片太大"));
        CODE_SPARSE_ARRAY.append(20007, new WeiboHttpStatus(20007, "\tDoes multipart has image", "\t请确保使用multpart上传图片"));
        CODE_SPARSE_ARRAY.append(20008, new WeiboHttpStatus(20008, "Content is null", "内容为空"));
        CODE_SPARSE_ARRAY.append(20009, new WeiboHttpStatus(20009, "\tIDs is too many", "\tIDs参数太长了"));
        CODE_SPARSE_ARRAY.append(20012, new WeiboHttpStatus(20012, "\tText too long, please input text less than 140 characters", "输入文字太长，请确认不超过140个字符"));
        CODE_SPARSE_ARRAY.append(20013, new WeiboHttpStatus(20013, "\tText too long, please input text less than 300 characters", "输入文字太长，请确认不超过300个字符"));
        CODE_SPARSE_ARRAY.append(20014, new WeiboHttpStatus(20014, "\tParam is error, please try again", "安全检查参数有误，请再调用一次"));
        CODE_SPARSE_ARRAY.append(20015, new WeiboHttpStatus(20015, "Account or ip or app is illgal, can not continue", "\t账号、IP或应用非法，暂时无法完成此操作"));
        CODE_SPARSE_ARRAY.append(20016, new WeiboHttpStatus(20016, "Out of limit", "发布内容过于频繁"));
        CODE_SPARSE_ARRAY.append(20017, new WeiboHttpStatus(20017, "\tRepeat content", "\t提交相似的信息"));
        CODE_SPARSE_ARRAY.append(20018, new WeiboHttpStatus(20018, "Contain illegal website", "包含非法网址"));
        CODE_SPARSE_ARRAY.append(20019, new WeiboHttpStatus(20019, "\tRepeat conetnt", "提交相同的信息"));
        CODE_SPARSE_ARRAY.append(20020, new WeiboHttpStatus(20020, "Contain advertising", "\t包含广告信息"));
        CODE_SPARSE_ARRAY.append(20021, new WeiboHttpStatus(20021, "Content is illegal", "包含非法内容"));
        CODE_SPARSE_ARRAY.append(20022, new WeiboHttpStatus(20022, "\tYour ip's behave in a comic boisterous or unruly manner", "\t此IP地址上的行为异常"));
        CODE_SPARSE_ARRAY.append(20031, new WeiboHttpStatus(20031, "\tTest and verify", "需要验证码"));
        CODE_SPARSE_ARRAY.append(20032, new WeiboHttpStatus(20032, "\tUpdate success, while server slow now, please wait 1-2 minutes", "\t发布成功，目前服务器可能会有延迟，请耐心等待1-2分钟"));

        CODE_SPARSE_ARRAY.append(20101, new WeiboHttpStatus(20101, "Target weibo does not exist", "\t不存在的微博"));
        CODE_SPARSE_ARRAY.append(20102, new WeiboHttpStatus(20102, "\tNot your own weibo", "\t不是你发布的微博"));
        CODE_SPARSE_ARRAY.append(20103, new WeiboHttpStatus(20103, "\tCan't repost yourself weibo", "\t不能转发自己的微博"));
        CODE_SPARSE_ARRAY.append(20104, new WeiboHttpStatus(20104, "\tIllegal weibo", "不合法的微博"));
        CODE_SPARSE_ARRAY.append(20109, new WeiboHttpStatus(20109, "Weibo id is null", "微博ID为空"));
        CODE_SPARSE_ARRAY.append(20111, new WeiboHttpStatus(20111, "Repeated weibo text", "不能发布相同的微博"));

        CODE_SPARSE_ARRAY.append(20201, new WeiboHttpStatus(20201, "\tTarget weibo comment does not exist", "不存在的微博评论"));
        CODE_SPARSE_ARRAY.append(20202, new WeiboHttpStatus(20202, "Illegal comment", "\t不合法的评论"));
        CODE_SPARSE_ARRAY.append(20203, new WeiboHttpStatus(20203, "Not your own comment", "\t不是你发布的评论"));
        CODE_SPARSE_ARRAY.append(20204, new WeiboHttpStatus(20204, "\tComment id is null", "\t评论ID为空"));

        CODE_SPARSE_ARRAY.append(20301, new WeiboHttpStatus(20301, "\tCan't send direct message to user who is not your follower", "不能给不是你粉丝的人发私信"));
        CODE_SPARSE_ARRAY.append(20302, new WeiboHttpStatus(20302, "Illegal direct message", "\t不合法的私信"));
        CODE_SPARSE_ARRAY.append(20303, new WeiboHttpStatus(20303, "\tNot your own direct message", "不是属于你的私信"));
        CODE_SPARSE_ARRAY.append(20305, new WeiboHttpStatus(20305, "Direct message does not exist", "\t不存在的私信"));
        CODE_SPARSE_ARRAY.append(20306, new WeiboHttpStatus(20306, "\tRepeated direct message text", "不能发布相同的私信"));
        CODE_SPARSE_ARRAY.append(20307, new WeiboHttpStatus(20307, "\tIllegal direct message id", "非法的私信ID"));

        CODE_SPARSE_ARRAY.append(20401, new WeiboHttpStatus(20401, "\tDomain not exist", "域名不存在"));
        CODE_SPARSE_ARRAY.append(20402, new WeiboHttpStatus(20402, "Wrong verifier", "Verifier错误"));

        CODE_SPARSE_ARRAY.append(20501, new WeiboHttpStatus(20501, "Source_user or target_user does not exists", "参数source_user或者target_user的用户不存在"));
        CODE_SPARSE_ARRAY.append(20502, new WeiboHttpStatus(20502, "Please input right target user id or screen_name", "\t必须输入目标用户id或者screen_name"));
        CODE_SPARSE_ARRAY.append(20503, new WeiboHttpStatus(20503, "\tNeed you follow user_id", "参数user_id必须是你关注的用户"));
        CODE_SPARSE_ARRAY.append(20504, new WeiboHttpStatus(20504, "\tCan not follow yourself", "你不能关注自己"));
        CODE_SPARSE_ARRAY.append(20505, new WeiboHttpStatus(20505, "Social graph updates out of rate limit", "加关注请求超过上限"));
        CODE_SPARSE_ARRAY.append(20506, new WeiboHttpStatus(20506, "Already followed", "已经关注此用户"));
        CODE_SPARSE_ARRAY.append(20507, new WeiboHttpStatus(20507, "Verification code is needed", "\t需要输入验证码"));
        CODE_SPARSE_ARRAY.append(20508, new WeiboHttpStatus(20508, "According to user privacy settings,you can not do this", "根据对方的设置，你不能进行此操作"));
        CODE_SPARSE_ARRAY.append(20509, new WeiboHttpStatus(20509, "Private friend count is out of limit", "\t悄悄关注个数到达上限"));
        CODE_SPARSE_ARRAY.append(20510, new WeiboHttpStatus(20510, "\tNot private friend", "不是悄悄关注人"));
        CODE_SPARSE_ARRAY.append(20511, new WeiboHttpStatus(20511, "Already followed privately", "\t已经悄悄关注此用户"));
        CODE_SPARSE_ARRAY.append(20512, new WeiboHttpStatus(20512, "Please delete the user from you blacklist before you follow the user", "你已经把此用户加入黑名单，加关注前请先解除"));
        CODE_SPARSE_ARRAY.append(20513, new WeiboHttpStatus(20513, "\tFriend count is out of limit!", "\t你的关注人数已达上限"));
        CODE_SPARSE_ARRAY.append(20521, new WeiboHttpStatus(20521, "\tHi Superman, you have concerned a lot of people, have a think of how to make other people concern about you! ! If you have any questions, please contact Sina customer service: 400 690 0000", "hi 超人，你今天已经关注很多喽，接下来的时间想想如何让大家都来关注你吧！如有问题，请联系新浪客服：400 690 0000"));
        CODE_SPARSE_ARRAY.append(20522, new WeiboHttpStatus(20522, "Not followed", "还未关注此用户"));
        CODE_SPARSE_ARRAY.append(20523, new WeiboHttpStatus(20523, "\tNot followers", "还不是粉丝"));
        CODE_SPARSE_ARRAY.append(20524, new WeiboHttpStatus(20524, "Hi Superman, you have cancelled concerning a lot of people, have a think of how to make other people concern about you! ! If you have any questions, please contact Sina customer service: 400 690 0000", "hi 超人，你今天已经取消关注很多喽，接下来的时间想想如何让大家都来关注你吧！如有问题，请联系新浪客服：400 690 0000"));

        CODE_SPARSE_ARRAY.append(20601, new WeiboHttpStatus(20601, "List name too long, please input text less than 10 characters", "列表名太长，请确保输入的文本不超过10个字符"));
        CODE_SPARSE_ARRAY.append(20602, new WeiboHttpStatus(20602, "List description too long, please input text less than 70 characters", "列表描叙太长，请确保输入的文本不超过70个字符"));
        CODE_SPARSE_ARRAY.append(20603, new WeiboHttpStatus(20603, "List does not exists", "列表不存在"));
        CODE_SPARSE_ARRAY.append(20604, new WeiboHttpStatus(20604, "Only the owner has the authority", "\t不是列表的所属者"));
        CODE_SPARSE_ARRAY.append(20605, new WeiboHttpStatus(20605, "\tIllegal list name or list description", "列表名或描叙不合法"));
        CODE_SPARSE_ARRAY.append(20606, new WeiboHttpStatus(20606, "\tObject already exists", "记录已存在"));
        CODE_SPARSE_ARRAY.append(20607, new WeiboHttpStatus(20607, "\tDB error, please contact the administator", "数据库错误，请联系系统管理员"));
        CODE_SPARSE_ARRAY.append(20608, new WeiboHttpStatus(20608, "\tList name duplicate", "列表名冲突"));
        CODE_SPARSE_ARRAY.append(20610, new WeiboHttpStatus(20610, "\tDoes not support private list", "目前不支持私有分组"));
        CODE_SPARSE_ARRAY.append(20611, new WeiboHttpStatus(20611, "\tCreate list error", "创建列表失败"));
        CODE_SPARSE_ARRAY.append(20612, new WeiboHttpStatus(20612, "\tOnly support private list", "目前只支持私有分组"));
        CODE_SPARSE_ARRAY.append(20613, new WeiboHttpStatus(20613, "\tYou hava subscriber too many lists", "\t订阅列表达到上限"));
        CODE_SPARSE_ARRAY.append(20614, new WeiboHttpStatus(20614, "Too many lists, see doc for more info", "\t创建列表达到上限，请参考API文档"));
        CODE_SPARSE_ARRAY.append(20615, new WeiboHttpStatus(20615, "\tToo many members, see doc for more info", "\t列表成员上限，请参考API文档"));

        CODE_SPARSE_ARRAY.append(20701, new WeiboHttpStatus(20701, "Repeated tag text", "\t不能提交相同的收藏标签"));
        CODE_SPARSE_ARRAY.append(20702, new WeiboHttpStatus(20702, "Tags is too many", "\t最多两个收藏标签"));
        CODE_SPARSE_ARRAY.append(20703, new WeiboHttpStatus(20703, "\tIllegal tag name", "收藏标签名不合法"));

        CODE_SPARSE_ARRAY.append(20801, new WeiboHttpStatus(20801, "\tTrend_name is null", "参数trend_name是空值"));
        CODE_SPARSE_ARRAY.append(20802, new WeiboHttpStatus(20802, "\tTrend_id is null", "\t参数trend_id是空值"));

        CODE_SPARSE_ARRAY.append(20901, new WeiboHttpStatus(20901, "Error: in blacklist", "错误:已经添加了黑名单"));
        CODE_SPARSE_ARRAY.append(20902, new WeiboHttpStatus(20902, "Error: Blacklist limit has been reached.", "\t错误:已达到黑名单上限"));
        CODE_SPARSE_ARRAY.append(20903, new WeiboHttpStatus(20903, "Error: System administrators can not be added to the blacklist.", "\t错误:不能添加系统管理员为黑名单"));
        CODE_SPARSE_ARRAY.append(20904, new WeiboHttpStatus(20904, "\tError: Can not add yourself to the blacklist.", "错误:不能添加自己为黑名单"));
        CODE_SPARSE_ARRAY.append(20905, new WeiboHttpStatus(20905, "Error: not in blacklist", "错误:不在黑名单中"));

        CODE_SPARSE_ARRAY.append(21001, new WeiboHttpStatus(21001, "\tTags parameter is null", "标签参数为空"));
        CODE_SPARSE_ARRAY.append(21002, new WeiboHttpStatus(21002, "Tags name too long", "\t标签名太长，请确保每个标签名不超过14个字符"));

        CODE_SPARSE_ARRAY.append(21101, new WeiboHttpStatus(21101, "Domain parameter is error", "参数domain错误"));
        CODE_SPARSE_ARRAY.append(21102, new WeiboHttpStatus(21102, "The phone number has been used", "\t该手机号已经被使用"));
        CODE_SPARSE_ARRAY.append(21103, new WeiboHttpStatus(21103, "\tThe account has bean bind phone", "\t该用户已经绑定手机"));
        CODE_SPARSE_ARRAY.append(21104, new WeiboHttpStatus(21104, "Wrong verifier", "\tVerifier错误"));

        CODE_SPARSE_ARRAY.append(21301, new WeiboHttpStatus(21301, "Auth faild", "认证失败"));
        CODE_SPARSE_ARRAY.append(21302, new WeiboHttpStatus(21302, "Username or password error", "用户名或密码不正确"));
        CODE_SPARSE_ARRAY.append(21303, new WeiboHttpStatus(21303, "Username and pwd auth out of rate limit", "用户名密码认证超过请求限制"));
        CODE_SPARSE_ARRAY.append(21304, new WeiboHttpStatus(21304, "Version rejected", "版本号错误"));
        CODE_SPARSE_ARRAY.append(21305, new WeiboHttpStatus(21305, "Parameter absent", "缺少必要的参数"));
        CODE_SPARSE_ARRAY.append(21306, new WeiboHttpStatus(21306, "Parameter rejected", "OAuth参数被拒绝"));
        CODE_SPARSE_ARRAY.append(21307, new WeiboHttpStatus(21307, "Timestamp refused", "时间戳不正确"));
        CODE_SPARSE_ARRAY.append(21308, new WeiboHttpStatus(21308, "Nonce used", "参数nonce已经被使用"));
        CODE_SPARSE_ARRAY.append(21309, new WeiboHttpStatus(21309, "Signature method rejected", "签名算法不支持"));
        CODE_SPARSE_ARRAY.append(21310, new WeiboHttpStatus(21310, "Signature invalid", "签名值不合法"));
        CODE_SPARSE_ARRAY.append(21311, new WeiboHttpStatus(21311, "Consumer key unknown", "参数consumer_key不存在"));
        CODE_SPARSE_ARRAY.append(21312, new WeiboHttpStatus(21312, "Consumer key refused", "参数consumer_key不合法"));
        CODE_SPARSE_ARRAY.append(21313, new WeiboHttpStatus(21313, "Miss consumer key", "参数consumer_key缺失"));
        CODE_SPARSE_ARRAY.append(21314, new WeiboHttpStatus(21314, "Token used", "Token已经被使用"));
        CODE_SPARSE_ARRAY.append(21315, new WeiboHttpStatus(21315, "Token expired", "Token已经过期"));
        CODE_SPARSE_ARRAY.append(21316, new WeiboHttpStatus(21316, "oken revoked", "Token不合法"));
        CODE_SPARSE_ARRAY.append(21317, new WeiboHttpStatus(21317, "Token rejected", "Token不合法"));
        CODE_SPARSE_ARRAY.append(21318, new WeiboHttpStatus(21318, "Verifier fail", "Pin码认证失败"));
        CODE_SPARSE_ARRAY.append(21319, new WeiboHttpStatus(21319, "Accessor was revoked", "授权关系已经被解除"));
        CODE_SPARSE_ARRAY.append(21320, new WeiboHttpStatus(21320, "OAuth2 must use https", "使用OAuth2必须使用https"));
        CODE_SPARSE_ARRAY.append(21321, new WeiboHttpStatus(21321, "Applications over the unaudited use restrictions", "未审核的应用使用人数超过限制"));
        CODE_SPARSE_ARRAY.append(21327, new WeiboHttpStatus(21327, "Expired token", "token过期"));
        CODE_SPARSE_ARRAY.append(21335, new WeiboHttpStatus(21335, "Request uid's value must be the current user", "uid参数仅允许传入当前授权用户uid"));

        CODE_SPARSE_ARRAY.append(21501, new WeiboHttpStatus(21501, "Urls is null", "参数urls是空的"));
        CODE_SPARSE_ARRAY.append(21502, new WeiboHttpStatus(21502, "Urls is too many", "参数urls太多了"));
        CODE_SPARSE_ARRAY.append(21503, new WeiboHttpStatus(21503, "IP is null", "IP是空值"));
        CODE_SPARSE_ARRAY.append(21504, new WeiboHttpStatus(21504, "Url is null", "参数url是空值"));

        CODE_SPARSE_ARRAY.append(21601, new WeiboHttpStatus(21601, "Manage notice error, need auth", "需要系统管理员的权限"));
        CODE_SPARSE_ARRAY.append(21602, new WeiboHttpStatus(21602, "Contains forbid world", "含有敏感词"));
        CODE_SPARSE_ARRAY.append(21603, new WeiboHttpStatus(21603, "Applications send notice over the restrictions", "通知发送达到限制"));

        CODE_SPARSE_ARRAY.append(21701, new WeiboHttpStatus(21701, "Manage remind error, need auth", "提醒失败，需要权限"));
        CODE_SPARSE_ARRAY.append(21702, new WeiboHttpStatus(21702, "Invalid category", "无效分类"));
        CODE_SPARSE_ARRAY.append(21703, new WeiboHttpStatus(21703, "Invalid status", "无效状态码"));

        CODE_SPARSE_ARRAY.append(21901, new WeiboHttpStatus(21901, "Geo code input error", "地理信息输入错误"));
    }
}
