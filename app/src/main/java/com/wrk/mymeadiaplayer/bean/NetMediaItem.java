package com.wrk.mymeadiaplayer.bean;

import java.util.List;

/**
 * Created by MrbigW on 2016/10/12.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 数据BEAN
 * -------------------=.=------------------------
 */

public class NetMediaItem {




    /**
     * status : 4
     * comment : 19
     * top_comments : [{"voicetime":0,"status":0,"cmt_type":"text","precid":0,"content":"没事，你会有女朋友的","like_count":11,"u":{"header":["http://wimg.spriteapp.cn/profile/large/2016/09/10/57d38263c03ca_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/09/10/57d38263c03ca_mini.jpg"],"sex":"m","uid":"19301490","name":"水上亭"},"preuid":0,"passtime":"2016-10-08 13:12:11","voiceuri":"","id":65688916},{"voicetime":0,"status":0,"cmt_type":"text","precid":0,"content":"找个杆子都比找马蓉那婊子好，爸爸们，你们说对吗","like_count":8,"u":{"header":["http://qzapp.qlogo.cn/qzapp/100336987/D942E43353A5C0007631094F19580C35/100","http://qzapp.qlogo.cn/qzapp/100336987/D942E43353A5C0007631094F19580C35/100"],"sex":"m","uid":"6693349","name":"悲伤\u2018格式化"},"preuid":0,"passtime":"2016-10-12 20:43:23","voiceuri":"","id":66065986}]
     * tags : [{"id":1,"name":"搞笑"},{"id":62,"name":"内涵"},{"id":56,"name":"创意"}]
     * bookmark : 4
     * text : “妈，我过得挺好的，已经有对象了！”
     * image : {"medium":["http://ww2.sinaimg.cn/bmiddle/005P1ePojw1f8l1vn8cw2j30go0u0wgw.jpg"],"big":["http://ww2.sinaimg.cn/large/005P1ePojw1f8l1vn8cw2j30go0u0wgw.jpg","http://wimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c_1.jpg","http://dimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c_1.jpg"],"download_url":["http://wimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c_d.jpg","http://dimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c_d.jpg","http://wimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c.jpg","http://dimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c.jpg"],"height":1672,"width":929,"small":["http://ww2.sinaimg.cn/mw240/005P1ePojw1f8l1vn8cw2j30go0u0wgw.jpg"],"thumbnail_small":["http://wimg.spriteapp.cn/crop/150x150/ugc/2016/10/08/57f7d04eef85c.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2016/10/08/57f7d04eef85c.jpg"]}
     * up : 308
     * share_url : http://a.f.budejie.com/share/21185730.html?wx.qq.com
     * down : 74
     * forward : 41
     * u : {"header":["http://wimg.spriteapp.cn/profile/large/2016/10/04/57f312518b075_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/10/04/57f312518b075_mini.jpg"],"is_v":true,"uid":"14641899","is_vip":false,"name":"3_Plus_N"}
     * passtime : 2016-10-12 20:38:01
     * type : image
     * id : 21185730
     */

    private int status;
    private String comment;
    private String bookmark;
    private String text;
    /**
     * medium : ["http://ww2.sinaimg.cn/bmiddle/005P1ePojw1f8l1vn8cw2j30go0u0wgw.jpg"]
     * big : ["http://ww2.sinaimg.cn/large/005P1ePojw1f8l1vn8cw2j30go0u0wgw.jpg","http://wimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c_1.jpg","http://dimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c_1.jpg"]
     * download_url : ["http://wimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c_d.jpg","http://dimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c_d.jpg","http://wimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c.jpg","http://dimg.spriteapp.cn/ugc/2016/10/08/57f7d04eef85c.jpg"]
     * height : 1672
     * width : 929
     * small : ["http://ww2.sinaimg.cn/mw240/005P1ePojw1f8l1vn8cw2j30go0u0wgw.jpg"]
     * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/ugc/2016/10/08/57f7d04eef85c.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2016/10/08/57f7d04eef85c.jpg"]
     */

    private ImageBean image;
    private String up;
    private String share_url;
    private int down;
    private int forward;
    /**
     * header : ["http://wimg.spriteapp.cn/profile/large/2016/10/04/57f312518b075_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/10/04/57f312518b075_mini.jpg"]
     * is_v : true
     * uid : 14641899
     * is_vip : false
     * name : 3_Plus_N
     */

    private UBean u;
    private String passtime;
    private String type;
    private String id;
    /**
     * voicetime : 0
     * status : 0
     * cmt_type : text
     * precid : 0
     * content : 没事，你会有女朋友的
     * like_count : 11
     * u : {"header":["http://wimg.spriteapp.cn/profile/large/2016/09/10/57d38263c03ca_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/09/10/57d38263c03ca_mini.jpg"],"sex":"m","uid":"19301490","name":"水上亭"}
     * preuid : 0
     * passtime : 2016-10-08 13:12:11
     * voiceuri :
     * id : 65688916
     */

    private List<TopCommentsBean> top_comments;
    /**
     * id : 1
     * name : 搞笑
     */

    private List<TagsBean> tags;
    /**
     * playfcount : 9285
     * height : 480
     * width : 854
     * video : ["http://wvideo.spriteapp.cn/video/2016/1009/57f9fc36bd42e_wpd.mp4","http://dvideo.spriteapp.cn/video/2016/1009/57f9fc36bd42e_wpd.mp4"]
     * download : ["http://wvideo.spriteapp.cn/video/2016/1009/57f9fc36bd42e_wpd.mp4","http://dvideo.spriteapp.cn/video/2016/1009/57f9fc36bd42e_wpd.mp4"]
     * duration : 75
     * playcount : 33172
     * thumbnail : ["http://wimg.spriteapp.cn/picture/2016/1009/57f9fc3681fed__b_98.jpg","http://dimg.spriteapp.cn/picture/2016/1009/57f9fc3681fed__b_98.jpg"]
     * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/picture/2016/1009/57f9fc3681fed__b_98.jpg","http://dimg.spriteapp.cn/crop/150x150/picture/2016/1009/57f9fc3681fed__b_98.jpg"]
     */

    private VideoBean video;
    /**
     * images : ["http://wimg.spriteapp.cn/ugc/2016/10/11/57fcfa34f006e.gif","http://dimg.spriteapp.cn/ugc/2016/10/11/57fcfa34f006e.gif"]
     * width : 292
     * gif_thumbnail : ["http://wimg.spriteapp.cn/ugc/2016/10/11/57fcfa34f006e_a_1.jpg","http://dimg.spriteapp.cn/ugc/2016/10/11/57fcfa34f006e_a_1.jpg"]
     * download_url : ["http://wimg.spriteapp.cn/ugc/2016/10/11/57fcfa34f006e_d.jpg","http://dimg.spriteapp.cn/ugc/2016/10/11/57fcfa34f006e_d.jpg","http://wimg.spriteapp.cn/ugc/2016/10/11/57fcfa34f006e_a_1.jpg","http://dimg.spriteapp.cn/ugc/2016/10/11/57fcfa34f006e_a_1.jpg"]
     * height : 156
     */

    private GifBean gif;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getForward() {
        return forward;
    }

    public void setForward(int forward) {
        this.forward = forward;
    }

    public UBean getU() {
        return u;
    }

    public void setU(UBean u) {
        this.u = u;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TopCommentsBean> getTop_comments() {
        return top_comments;
    }

    public void setTop_comments(List<TopCommentsBean> top_comments) {
        this.top_comments = top_comments;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public VideoBean getVideo() {
        return video;
    }

    public void setVideo(VideoBean video) {
        this.video = video;
    }

    public GifBean getGif() {
        return gif;
    }

    public void setGif(GifBean gif) {
        this.gif = gif;
    }

    public static class ImageBean {
        private int height;
        private int width;
        private List<String> medium;
        private List<String> big;
        private List<String> download_url;
        private List<String> small;
        private List<String> thumbnail_small;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public List<String> getMedium() {
            return medium;
        }

        public void setMedium(List<String> medium) {
            this.medium = medium;
        }

        public List<String> getBig() {
            return big;
        }

        public void setBig(List<String> big) {
            this.big = big;
        }

        public List<String> getDownload_url() {
            return download_url;
        }

        public void setDownload_url(List<String> download_url) {
            this.download_url = download_url;
        }

        public List<String> getSmall() {
            return small;
        }

        public void setSmall(List<String> small) {
            this.small = small;
        }

        public List<String> getThumbnail_small() {
            return thumbnail_small;
        }

        public void setThumbnail_small(List<String> thumbnail_small) {
            this.thumbnail_small = thumbnail_small;
        }
    }

    public static class UBean {
        private boolean is_v;
        private String uid;
        private boolean is_vip;
        private String name;
        private List<String> header;

        public boolean isIs_v() {
            return is_v;
        }

        public void setIs_v(boolean is_v) {
            this.is_v = is_v;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public boolean isIs_vip() {
            return is_vip;
        }

        public void setIs_vip(boolean is_vip) {
            this.is_vip = is_vip;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getHeader() {
            return header;
        }

        public void setHeader(List<String> header) {
            this.header = header;
        }
    }

    public static class TopCommentsBean {
        private int voicetime;
        private int status;
        private String cmt_type;
        private int precid;
        private String content;
        private int like_count;
        /**
         * header : ["http://wimg.spriteapp.cn/profile/large/2016/09/10/57d38263c03ca_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/09/10/57d38263c03ca_mini.jpg"]
         * sex : m
         * uid : 19301490
         * name : 水上亭
         */

        private UBean u;
        private int preuid;
        private String passtime;
        private String voiceuri;
        private int id;

        public int getVoicetime() {
            return voicetime;
        }

        public void setVoicetime(int voicetime) {
            this.voicetime = voicetime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCmt_type() {
            return cmt_type;
        }

        public void setCmt_type(String cmt_type) {
            this.cmt_type = cmt_type;
        }

        public int getPrecid() {
            return precid;
        }

        public void setPrecid(int precid) {
            this.precid = precid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public UBean getU() {
            return u;
        }

        public void setU(UBean u) {
            this.u = u;
        }

        public int getPreuid() {
            return preuid;
        }

        public void setPreuid(int preuid) {
            this.preuid = preuid;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public String getVoiceuri() {
            return voiceuri;
        }

        public void setVoiceuri(String voiceuri) {
            this.voiceuri = voiceuri;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class UBean {
            private String sex;
            private String uid;
            private String name;
            private List<String> header;

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getHeader() {
                return header;
            }

            public void setHeader(List<String> header) {
                this.header = header;
            }
        }
    }

    public static class TagsBean {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class VideoBean {
        private int playfcount;
        private int height;
        private int width;
        private int duration;
        private int playcount;
        private List<String> video;
        private List<String> download;
        private List<String> thumbnail;
        private List<String> thumbnail_small;

        public int getPlayfcount() {
            return playfcount;
        }

        public void setPlayfcount(int playfcount) {
            this.playfcount = playfcount;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getPlaycount() {
            return playcount;
        }

        public void setPlaycount(int playcount) {
            this.playcount = playcount;
        }

        public List<String> getVideo() {
            return video;
        }

        public void setVideo(List<String> video) {
            this.video = video;
        }

        public List<String> getDownload() {
            return download;
        }

        public void setDownload(List<String> download) {
            this.download = download;
        }

        public List<String> getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(List<String> thumbnail) {
            this.thumbnail = thumbnail;
        }

        public List<String> getThumbnail_small() {
            return thumbnail_small;
        }

        public void setThumbnail_small(List<String> thumbnail_small) {
            this.thumbnail_small = thumbnail_small;
        }
    }

    public static class GifBean {
        private int width;
        private int height;
        private List<String> images;
        private List<String> gif_thumbnail;
        private List<String> download_url;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getGif_thumbnail() {
            return gif_thumbnail;
        }

        public void setGif_thumbnail(List<String> gif_thumbnail) {
            this.gif_thumbnail = gif_thumbnail;
        }

        public List<String> getDownload_url() {
            return download_url;
        }

        public void setDownload_url(List<String> download_url) {
            this.download_url = download_url;
        }
    }
}
