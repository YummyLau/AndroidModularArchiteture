package example.componentweibo.data.local.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import example.componentweibo.bean.Pic;
import example.componentweibo.data.local.db.converter.PicConverters;

/**
 * 微博实体
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
@Entity(tableName = StatusEntity.TABLE_NAME,
        primaryKeys = StatusEntity.COLUMN_ID)
public class StatusEntity {

    public static final String TABLE_NAME = "status_table";
    public static final String COLUMN_ID = "status_id";
    public static final String COLUMN_CREATE_AT = "status_create_at";
    public static final String COLUMN_MID = "status_mid";
    public static final String COLUMN_IDSTR = "status_idstr";
    public static final String COLUMN_TEXT = "status_text";
    public static final String COLUMN_SOURCE = "status_source";
    public static final String COLUMN_FAVORITED = "status_favorited";
    public static final String COLUMN_TRUNCATED = "status_truncated";
    public static final String COLUMN_IN_REPLY_TO_STATUS_ID= "status_in_reply_to_status_id";
    public static final String COLUMN_IN_REPLY_TO_USER_ID= "status_in_reply_to_screen_id";
    public static final String COLUMN_IN_REPLY_TO_SCREEN= "status_in_reply_to_screen";
    public static final String COLUMN_REPOSTS_COUNT= "status_reposts_count";
    public static final String COLUMN_COMMENTS_COUNT= "status_comments_count";
    public static final String COLUMN_ATTITUDES_COUNT= "status_attitudes_count";
    public static final String COLUMN_PIC_IDS= "status_pic_ids";
    public static final String COLUMN_AD= "status_ad";
    public static final String COLUMN_PIC_URLS= "status_pic_urls";
    public static final String COLUMN_THUMBNAIL_PIC= "status_thumbnail_pic";
    public static final String COLUMN_BMIDDLE_PIC= "status_bmiddle_pic";
    public static final String COLUMN_ORIGINAL_PIC= "status_original_pic";

    //微博ID
    @ColumnInfo(name = COLUMN_ID)
    @SerializedName("id")
    public long id;

    //微博创建时间
    @ColumnInfo(name = COLUMN_CREATE_AT)
    @SerializedName("created_at")
    public String created_at;

    //微博MID
    @ColumnInfo(name = COLUMN_MID)
    @SerializedName("mid")
    public long mid;

    //字符串型的微博ID
    @ColumnInfo(name = COLUMN_IDSTR)
    @SerializedName("idstr")
    public String idstr;

    //微博信息内容
    @ColumnInfo(name = COLUMN_TEXT)
    @SerializedName("text")
    public String text;

    //微博来源
    @ColumnInfo(name = COLUMN_SOURCE)
    @SerializedName("source")
    public String source;

    //是否已收藏，true：是，false：否
    @ColumnInfo(name = COLUMN_FAVORITED)
    @SerializedName("favorited")
    public boolean favorited;

    //是否被截断，true：是，false：否
    @ColumnInfo(name = COLUMN_TRUNCATED)
    @SerializedName("truncated")
    public boolean truncated;

    //（暂未支持）回复ID
    @ColumnInfo(name = COLUMN_IN_REPLY_TO_STATUS_ID)
    @SerializedName("in_reply_to_status_id")
    public String inReplyToStatusId;

    //（暂未支持）回复人UID
    @ColumnInfo(name = COLUMN_IN_REPLY_TO_USER_ID)
    @SerializedName("in_reply_to_user_id")
    public String inReplyToUserId;

    //（暂未支持）回复人昵称
    @ColumnInfo(name = COLUMN_IN_REPLY_TO_SCREEN)
    @SerializedName("in_reply_to_screen_name")
    public String inReplyToScreenName;

    //地理信息字段
    @Ignore
    @SerializedName("geo")
    public GeoEntity geo;

    //微博作者的用户信息字段
    @Embedded
    @SerializedName("user")
    public UserEntity user;

    //被转发的原微博信息字段，当该微博为转发微博时返回
    @Ignore
    @SerializedName("retweeted_status")
    public StatusEntity retweetedStatus;

    //转发数
    @ColumnInfo(name = COLUMN_REPOSTS_COUNT)
    @SerializedName("reposts_count")
    public int repostsCount;

    //评论数
    @ColumnInfo(name = COLUMN_COMMENTS_COUNT)
    @SerializedName("comments_count")
    public int commentsCount;

    //表态数
    @ColumnInfo(name = COLUMN_ATTITUDES_COUNT)
    @SerializedName("attitudes_count")
    public int attitudesCount;

    //暂未支持
    @SerializedName("mlevel")
    public int mlevel;

//    //微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号
//    @SerializedName("visible")
//    public boolean visible;

    //微博配图ID。多图时返回多图ID，用来拼接图片url。用返回字段thumbnail_pic的地址配上该返回字段的图片ID，即可得到多个图片url。
    @ColumnInfo(name = COLUMN_PIC_IDS)
    @SerializedName("pic_ids")
    public String pic_ids;

    //微博流内的推广微博ID
    @ColumnInfo(name = COLUMN_AD)
    @SerializedName("ad")
    public String ad;

    @ColumnInfo(name = COLUMN_PIC_URLS)
    @SerializedName("pic_urls")
    @TypeConverters(PicConverters.class)
    public List<Pic> pics;

    @ColumnInfo(name = COLUMN_THUMBNAIL_PIC)
    @SerializedName("thumbnail_pic")
    public String thumbnailPic;

    //中等尺寸图片地址，没有时不返回此字段
    @ColumnInfo(name = COLUMN_BMIDDLE_PIC)
    @SerializedName("bmiddle_pic")
    public String bmiddlePic;

    //原始图片地址，没有时不返回此字段
    @ColumnInfo(name = COLUMN_ORIGINAL_PIC)
    @SerializedName("original_pic")
    public String original_pic;
}
