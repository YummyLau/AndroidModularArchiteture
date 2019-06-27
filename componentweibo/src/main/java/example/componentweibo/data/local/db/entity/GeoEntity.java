package example.componentweibo.data.local.db.entity;


import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * 地理信息
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/26.
 */
@Entity(tableName = "geo_table")
public class GeoEntity {

    //经度坐标
    @SerializedName("longitude")
    public String longitude;

    //维度坐标
    @SerializedName("latitude")
    public String latitude;

    //所在城市的城市代码
    @SerializedName("city")
    public String city;

    //所在省份的省份代码
    @SerializedName("province")
    public String province;

    //所在城市的城市名称
    @SerializedName("city_name")
    public String cityName;

    //所在省份的省份名称
    @SerializedName("province_name")
    public String provinceName;

    //所在的实际地址，可以为空
    @SerializedName("address")
    public String address;

    //地址的汉语拼音，不是所有情况都会返回该字段
    @SerializedName("pinyin")
    public String pinyin;

    //更多信息，不是所有情况都会返回该字段
    @SerializedName("more")
    public String more;
}
