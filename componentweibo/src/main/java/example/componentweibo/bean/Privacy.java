package example.componentweibo.bean;


/**
 * 隐私设置
 Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class Privacy {

    //是否可以评论我的微博，0：所有人、1：关注的人、2：可信用户
    @SerializedName("comment")
    public int comment;

    //是否开启地理信息，0：不开启、1：开启
    @SerializedName("geo")
    public int geo;

    //是否可以给我发私信，0：所有人、1：我关注的人、2：可信用户
    @SerializedName("message")
    public int message;

    //是否可以通过真名搜索到我，0：不可以、1：可以
    @SerializedName("realname")
    public int realname;

    //勋章是否可见，0：不可见、1：可见
    @SerializedName("badge")
    public int badge;

    //是否可以通过手机号码搜索到我，0：不可以、1：可以
    @SerializedName("mobile")
    public int mobile;

    //是否开启webim， 0：不开启、1：开启
    @SerializedName("webim")
    public int webim;
}
