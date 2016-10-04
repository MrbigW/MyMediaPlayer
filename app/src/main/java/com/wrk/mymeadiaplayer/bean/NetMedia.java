package com.wrk.mymeadiaplayer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/3.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class NetMedia implements Serializable {

    /**
     * id : 62737
     * movieName : 《世界之外》预告片
     * coverImg : http://img5.mtime.cn/mg/2016/09/30/151637.47054246.jpg
     * movieId : 220809
     * url : http://vfx.mtime.cn/Video/2016/09/30/mp4/160930094030475199_480.mp4
     * hightUrl : http://vfx.mtime.cn/Video/2016/09/30/mp4/160930094030475199.mp4
     * videoTitle : 世界之外 预告片
     * videoLength : 160
     * rating : -1
     * type : ["冒险","剧情","爱情","科幻"]
     * summary : 安德游戏男主变身火星boy
     */

    private int id;
    private String movieName;
    private String coverImg;
    private int movieId;
    private String url;
    private String hightUrl;
    private String videoTitle;
    private int videoLength;
    private String summary;
    private List<String> type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHightUrl() {
        return hightUrl;
    }

    public void setHightUrl(String hightUrl) {
        this.hightUrl = hightUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(int videoLength) {
        this.videoLength = videoLength;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NetMedia{" +
                "id=" + id +
                ", movieName='" + movieName + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", movieId=" + movieId +
                ", url='" + url + '\'' +
                ", hightUrl='" + hightUrl + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoLength=" + videoLength +
                ", summary='" + summary + '\'' +
                ", type=" + type +
                '}';
    }
}
