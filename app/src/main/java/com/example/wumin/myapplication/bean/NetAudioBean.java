package com.example.wumin.myapplication.bean;

import java.util.List;

/**
 * Created by wumin on 2018/8/13.
 */
public class NetAudioBean {

    /**
     * count : 4852
     * np : 1534088761
     */

    private InfoBean info;
    /**
     * status : 4
     * comment : 5
     * cate : GIF图
     * tags : [{"post_number":128585,"image_list":"http://img.spriteapp.cn/ugc/2017/07/63caa1de660711e7b678842b2b4c75ab.png","forum_sort":0,"forum_status":2,"id":17083,"info":"爆笑Gif，搞笑/奇葩/逗比的\u201c才艺展示\u201d区\n这里会有很多让你喷饭的Gif图等着你哟~让心情起飞，让笑声传递~\n\n板块规则：\n1，不要发布与标签无关的内容，否则会被移除标签哦~\n\n2，不要发布违规内容，注意把握尺度，以免被判违规。\n\n3，标签内内容定期更换置顶，发布更好的内容争取置顶，让更多人看到你的作品吧~\n\n4，如果你的帖子不被显示，你的内容数据不好等各种疑问，请加官方微信（budejieyh）官方在这里等风也等你，随时恭候~\n\n版主申请加微信：L1391446139","name":"Gif专区","colum_set":1,"tail":"名Gif大师","sub_number":24422,"display_level":0}]
     * bookmark : 1
     * text : 猫：秀什么秀，大象腿  起开，看我的才是大长腿
     * gif : {"images":["http://wimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702.gif","http://dimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702.gif"],"width":210,"gif_thumbnail":["http://wimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_a_1.jpg","http://dimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_a_1.jpg"],"download_url":["http://wimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_d.jpg","http://dimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_d.jpg","http://wimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_a_1.jpg","http://dimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_a_1.jpg"],"height":307}
     * up : 107
     * share_url : http://a.f.budejie.com/share/28473384.html?wx.qq.com
     * down : 2
     * forward : 5
     * u : {"header":["http://wimg.spriteapp.cn/profile/large/2018/02/24/5a90448611639_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/02/24/5a90448611639_mini.jpg"],"uid":"17474065","is_vip":false,"is_v":true,"room_url":"","room_name":"","room_role":"","room_icon":"","name":"马小梅"}
     * passtime : 2018-08-13 06:52:01
     * type : gif
     * id : 28473384
     */

    private List<ListBean> list;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class InfoBean {
        private int count;
        private int np;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNp() {
            return np;
        }

        public void setNp(int np) {
            this.np = np;
        }
    }

    public static class ListBean {
        private int status;
        private String comment;
        private String cate;
        private String bookmark;
        private String text;
        /**
         * images : ["http://wimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702.gif","http://dimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702.gif"]
         * width : 210
         * gif_thumbnail : ["http://wimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_a_1.jpg","http://dimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_a_1.jpg"]
         * download_url : ["http://wimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_d.jpg","http://dimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_d.jpg","http://wimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_a_1.jpg","http://dimg.spriteapp.cn/ugc/2018/08/12/5b6fb25211702_a_1.jpg"]
         * height : 307
         */

        private GifBean gif;
        private VideoBean video;
        private ImageBean image;

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public ImageBean getImageBean() {
            return image;
        }

        public void setImageBean(ImageBean image) {
            this.image = image;
        }

        private String up;
        private String share_url;
        private int down;
        private int forward;
        /**
         * header : ["http://wimg.spriteapp.cn/profile/large/2018/02/24/5a90448611639_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/02/24/5a90448611639_mini.jpg"]
         * uid : 17474065
         * is_vip : false
         * is_v : true
         * room_url :
         * room_name :
         * room_role :
         * room_icon :
         * name : 马小梅
         */

        private UBean u;
        private String passtime;
        private String type;
        private String id;
        /**
         * post_number : 128585
         * image_list : http://img.spriteapp.cn/ugc/2017/07/63caa1de660711e7b678842b2b4c75ab.png
         * forum_sort : 0
         * forum_status : 2
         * id : 17083
         * info : 爆笑Gif，搞笑/奇葩/逗比的“才艺展示”区
         这里会有很多让你喷饭的Gif图等着你哟~让心情起飞，让笑声传递~

         板块规则：
         1，不要发布与标签无关的内容，否则会被移除标签哦~

         2，不要发布违规内容，注意把握尺度，以免被判违规。

         3，标签内内容定期更换置顶，发布更好的内容争取置顶，让更多人看到你的作品吧~

         4，如果你的帖子不被显示，你的内容数据不好等各种疑问，请加官方微信（budejieyh）官方在这里等风也等你，随时恭候~

         版主申请加微信：L1391446139
         * name : Gif专区
         * colum_set : 1
         * tail : 名Gif大师
         * sub_number : 24422
         * display_level : 0
         */

        private List<TagsBean> tags;

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

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
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

        public GifBean getGif() {
            return gif;
        }

        public void setGif(GifBean gif) {
            this.gif = gif;
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

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }
public static  class ImageBean{

    /**
     * medium : []
     * big : ["http://wimg.spriteapp.cn/ugc/2018/08/08/5b6a71e21734b_1.jpg","http://dimg.spriteapp.cn/ugc/2018/08/08/5b6a71e21734b_1.jpg"]
     * download_url : ["http://wimg.spriteapp.cn/ugc/2018/08/08/5b6a71e21734b_d.jpg","http://dimg.spriteapp.cn/ugc/2018/08/08/5b6a71e21734b_d.jpg","http://wimg.spriteapp.cn/ugc/2018/08/08/5b6a71e21734b.jpg","http://dimg.spriteapp.cn/ugc/2018/08/08/5b6a71e21734b.jpg"]
     * height : 11883
     * width : 480
     * small : []
     * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/ugc/2018/08/08/5b6a71e21734b.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2018/08/08/5b6a71e21734b.jpg"]
     */

    private int height;
    private int width;
    private List<?> medium;
    private List<String> big;
    private List<String> download_url;
    private List<?> small;
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

    public List<?> getMedium() {
        return medium;
    }

    public void setMedium(List<?> medium) {
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

    public List<?> getSmall() {
        return small;
    }

    public void setSmall(List<?> small) {
        this.small = small;
    }

    public List<String> getThumbnail_small() {
        return thumbnail_small;
    }

    public void setThumbnail_small(List<String> thumbnail_small) {
        this.thumbnail_small = thumbnail_small;
    }
}


public static  class VideoBean{

    /**
     * playfcount : 552
     * height : 544
     * width : 960
     * video : ["http://wvideo.spriteapp.cn/video/2018/0812/5b6fba269a04e_wpd.mp4","http://tvideo.spriteapp.cn/video/2018/0812/5b6fba269a04e_wpd.mp4"]
     * download : ["http://wvideo.spriteapp.cn/video/2018/0812/5b6fba269a04e_wpdm.mp4","http://tvideo.spriteapp.cn/video/2018/0812/5b6fba269a04e_wpdm.mp4"]
     * duration : 60
     * playcount : 3137
     * thumbnail : ["http://wimg.spriteapp.cn/picture/2018/0812/5b6fba265dc5e__b.jpg","http://dimg.spriteapp.cn/picture/2018/0812/5b6fba265dc5e__b.jpg"]
     * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/picture/2018/0812/5b6fba265dc5e__b.jpg","http://dimg.spriteapp.cn/crop/150x150/picture/2018/0812/5b6fba265dc5e__b.jpg"]
     */

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

        public static class UBean {
            private String uid;
            private boolean is_vip;
            private boolean is_v;
            private String room_url;
            private String room_name;
            private String room_role;
            private String room_icon;
            private String name;
            private List<String> header;

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

            public boolean isIs_v() {
                return is_v;
            }

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public String getRoom_url() {
                return room_url;
            }

            public void setRoom_url(String room_url) {
                this.room_url = room_url;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getRoom_role() {
                return room_role;
            }

            public void setRoom_role(String room_role) {
                this.room_role = room_role;
            }

            public String getRoom_icon() {
                return room_icon;
            }

            public void setRoom_icon(String room_icon) {
                this.room_icon = room_icon;
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

        public static class TagsBean {
            private int post_number;
            private String image_list;
            private int forum_sort;
            private int forum_status;
            private int id;
            private String info;
            private String name;
            private int colum_set;
            private String tail;
            private int sub_number;
            private int display_level;

            public int getPost_number() {
                return post_number;
            }

            public void setPost_number(int post_number) {
                this.post_number = post_number;
            }

            public String getImage_list() {
                return image_list;
            }

            public void setImage_list(String image_list) {
                this.image_list = image_list;
            }

            public int getForum_sort() {
                return forum_sort;
            }

            public void setForum_sort(int forum_sort) {
                this.forum_sort = forum_sort;
            }

            public int getForum_status() {
                return forum_status;
            }

            public void setForum_status(int forum_status) {
                this.forum_status = forum_status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getColum_set() {
                return colum_set;
            }

            public void setColum_set(int colum_set) {
                this.colum_set = colum_set;
            }

            public String getTail() {
                return tail;
            }

            public void setTail(String tail) {
                this.tail = tail;
            }

            public int getSub_number() {
                return sub_number;
            }

            public void setSub_number(int sub_number) {
                this.sub_number = sub_number;
            }

            public int getDisplay_level() {
                return display_level;
            }

            public void setDisplay_level(int display_level) {
                this.display_level = display_level;
            }
        }
    }
}
