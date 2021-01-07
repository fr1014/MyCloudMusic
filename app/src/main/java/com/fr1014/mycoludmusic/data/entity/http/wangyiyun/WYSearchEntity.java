package com.fr1014.mycoludmusic.data.entity.http.wangyiyun;

import java.util.List;

/**
 * 创建时间:2020/9/10
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class WYSearchEntity {

    private SearchBean result;
    private int code;

    public SearchBean getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public static class SearchBean {
        private boolean hasMore;
        private int songCount;
        private List<SongsBean> songs;

        public boolean isHasMore() {
            return hasMore;
        }

        public int getSongCount() {
            return songCount;
        }

        public List<SongsBean> getSongs() {
            return songs;
        }

        public static class SongsBean {
            private long id;
            private String name;
            private AlbumBean album;
            private int duration;
            private int copyrightId;
            private int status;
            private int rtype;
            private int ftype;
            private int mvid;
            private int fee;
            private Object rUrl;
            private long mark;
            private List<ArtistsBean> artists;
            private List<?> alias;

            public long getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public AlbumBean getAlbum() {
                return album;
            }

            public int getDuration() {
                return duration;
            }

            public int getCopyrightId() {
                return copyrightId;
            }

            public int getStatus() {
                return status;
            }

            public int getRtype() {
                return rtype;
            }

            public int getFtype() {
                return ftype;
            }

            public int getMvid() {
                return mvid;
            }

            public int getFee() {
                return fee;
            }

            public Object getrUrl() {
                return rUrl;
            }

            public long getMark() {
                return mark;
            }

            public List<ArtistsBean> getArtists() {
                return artists;
            }

            public List<?> getAlias() {
                return alias;
            }

            public static class AlbumBean {
                private long id;
                private String name;
                private ArtistBean artist;
                private long publishTime;
                private int size;
                private int copyrightId;
                private int status;
                private long picId;
                private long mark;

                public long getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }

                public ArtistBean getArtist() {
                    return artist;
                }

                public long getPublishTime() {
                    return publishTime;
                }

                public int getSize() {
                    return size;
                }

                public int getCopyrightId() {
                    return copyrightId;
                }

                public int getStatus() {
                    return status;
                }

                public long getPicId() {
                    return picId;
                }

                public long getMark() {
                    return mark;
                }

                public static class ArtistBean {
                    private int id;
                    private String name;
                    private Object picUrl;
                    private int albumSize;
                    private int picId;
                    private String img1v1Url;
                    private int img1v1;
                    private Object trans;
                    private List<?> alias;

                    public int getId() {
                        return id;
                    }

                    public String getName() {
                        return name;
                    }

                    public Object getPicUrl() {
                        return picUrl;
                    }

                    public int getAlbumSize() {
                        return albumSize;
                    }

                    public int getPicId() {
                        return picId;
                    }

                    public String getImg1v1Url() {
                        return img1v1Url;
                    }

                    public int getImg1v1() {
                        return img1v1;
                    }

                    public Object getTrans() {
                        return trans;
                    }

                    public List<?> getAlias() {
                        return alias;
                    }
                }
            }

            public static class ArtistsBean {
                private int id;
                private String name;
                private Object picUrl;
                private int albumSize;
                private int picId;
                private String img1v1Url;
                private int img1v1;
                private Object trans;
                private List<?> alias;

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }

                public Object getPicUrl() {
                    return picUrl;
                }

                public int getAlbumSize() {
                    return albumSize;
                }

                public int getPicId() {
                    return picId;
                }

                public String getImg1v1Url() {
                    return img1v1Url;
                }

                public int getImg1v1() {
                    return img1v1;
                }

                public Object getTrans() {
                    return trans;
                }

                public List<?> getAlias() {
                    return alias;
                }
            }
        }
    }
}
