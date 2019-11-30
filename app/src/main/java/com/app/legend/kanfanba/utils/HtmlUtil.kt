package com.app.legend.kanfanba.utils

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.bean.Video
import org.json.JSONObject
import org.jsoup.Jsoup


/**
 * 解析html
 */
class HtmlUtil {


    companion object{


        /**
         * 解析最新更新的列表
         */
        @WorkerThread
        public fun getNewestList(html:String):MutableList<Video>{


            val document = Jsoup.parse(html)

            val list:MutableList<Video> =ArrayList()

            val articles=document.getElementsByTag("article")//获取所有的article标签，循环解析内部并封装

            for (article in articles){

                var book=""
                var big=""

                //先获取图片

                val imgs=article.getElementsByTag("img")
                if (imgs.isNotEmpty()){//不是空的


                    val img=imgs[0]//获取第一个

                    val imgSets=img.attr("data-srcset")

                    val split=imgSets.split(",")//以,分割，选择最后一张

                    if (split.isNotEmpty()){
                        book=split[0]

                        if (split.size>=2) {

                            big = split[1]
                        }

                        big=big.substring(0,big.length-5)

                        book=book.substring(0,book.length-5)

//                        if (!book.endsWith(".jpg")||!book.endsWith("png")){
//
//                            book=img.attr("src")
//
//                        }

//                        Log.d("book--->>>",book)

                    }
                }

                //获取tag，也是分类

                var tag=""

                val ass=article.getElementsByClass("category-item")//获取a标签

                if (ass.isNotEmpty()){

                    val a=ass.last()

                    tag=a.text()

                }

                //获取链接

//                var url=""

                var title=""

                val h3s=article.getElementsByTag("h3")
                if (h3s.isNotEmpty()){

                    val h3=h3s.last()

                    val a3=h3.getElementsByTag("a")

                    if (a3.isNotEmpty()){

                        val aa=a3.last()

//                        url=aa.attr("href")

                        title=aa.text()
                    }
                }

                //获取更新时间

                var time=""

                val times=article.getElementsByTag("time")

                if (times.isNotEmpty()){

                    val t=times.last()

                    time=t.text()
                }

                //获取post_id

                val cs=article.getElementsByTag("a")

                var id=0

                if (cs.isNotEmpty()){

                    val p=cs[0]

                    id=p.attr("data-post-id").toInt()

                }


                val video=Video(title,tag,"",time,book,"",id,big,-1)

                list.add(video)

            }

            return list

        }

        /**
         * 解析首页，获取security，用于获取视频数据
         */

        public fun parseIndex(index:String):String{

            val document=Jsoup.parse(index)

            val scripts=document.getElementsByTag("script")

            var security=""

            for (s in scripts){

                val c=s.data()//获取script标签下的内容

                if (c.contains("security")){//包含security

                    val i=c.indexOf("security")+11

                    val end=i+10

                    security=c.substring(i,end)

                    Log.d("security--->>>",security)

                    break

                }
            }

            return security

        }


        public fun parseVideoJson(json:String):String{

            val jsonObject=JSONObject(json)

            var r=""

            if (jsonObject.has("single_video_url")){

                r=jsonObject.getString("single_video_url")

            }

            return r

        }


        /**
         * 获取剧集列表
         */
        public fun getEpisodeList(html: String):MutableList<Episode>{

            val list:MutableList<Episode> =ArrayList()

            val document=Jsoup.parse(html)

            val articles=document.getElementsByTag("article")

            for (a in articles){

                val aa=a.getElementsByTag("a")

                var book=""

                var title=""

                var url=""

                if (aa.isNotEmpty()){

                    val l=aa[1]

                    title=l.text()

                    url=l.attr("href")

                }

                val imgs=a.getElementsByTag("img")

                var big=""

                if (imgs.isNotEmpty()){

                    book=imgs[0].attr("data-src")

                    val bb=imgs[0].attr("data-srcset")

                    val sl=bb.split(",")

                    if (sl.isNotEmpty()){

//                        val sl1=sl.reversed()

                        if (sl.size>=2) {

                            big = sl[1]
                        }else{

                            big=sl[0]
                        }

                        if (big.contains("jpg")){

                            val b=big.indexOf("jpg")

                            big= big.substring(0,b+3)

//                            big=big.substring(0,big.lastIndexOf("-"))
//                            big="$big.jpg"

                        }else if (big.contains("png")){

                            val b=big.indexOf("png")

                            big=big.substring(0,b+3)

//                            big=big.substring(0,big.lastIndexOf("-"))
//                            big="$big.png"

                        }

                    }



                }

                var date=""

                val times=a.getElementsByTag("time")

                if (times.isNotEmpty()){

                    date=times[0].text()

                }

                val episode=Episode(title,date,0,"","",book,url,big)

                list.add(episode)

            }


            return list


        }

        /**
         * 获取一个剧集里的所有视频信息
         */
        public fun getEpisodeVideos(html: String):MutableList<Video>{

            val videos:MutableList<Video> =ArrayList()

            val document=Jsoup.parse(html)

            val divs=document.getElementsByClass("blog-items")

            if (divs.isNotEmpty()){

                val articles=divs[0].getElementsByTag("article")

                for (a in articles){

                    //先获取封面

                    var book=""

                    var big=""

                    val imgs=a.getElementsByTag("img")

                    if (imgs.isNotEmpty()){

                        book=imgs[0].attr("data-src")

                        if (!book.startsWith("http")){
                            book= "https:$book"

//                            Log.d("http--->>",book)

                        }

                        val gg=imgs[0].attr("data-srcset")

                        val pp=gg.split(",")

                        if (pp.isNotEmpty()) {

                            if (pp.size>=2) {

                                big = pp[1]
                            }else{

                                big=pp[0]
                            }



                            if (big.contains("jpg")){

                                val b=big.indexOf("jpg")
                                big=big.substring(0,b+3)

//                                big=big.substring(0,big.lastIndexOf("-"))
//                                big="$big.jpg"


                            }else if(big.contains("png")){

                                val b=big.indexOf("png")
                                big=big.substring(0,b+3)

//                                big=big.substring(0,big.lastIndexOf("-"))
//                                big="$big.png"
                            }

                            if (!big.startsWith("http")){

                                big="https:$big"
                            }



                        }

                    }

                    //获取title

                    var title=""

                    var id=0

                    val aa=a.getElementsByTag("a")

                    if (aa.isNotEmpty()){

                        title=aa[0].attr("title")
                        id=aa[0].attr("data-post-id").toInt()

                    }

                    var time=""

                    val tt=a.getElementsByTag("time")

                    if (tt.isNotEmpty()){

                        time=tt[0].text()

                    }

                    val v=Video(title,"","",time,book,"",id,big,-1)

                    videos.add(v)
                }
            }

            return videos
        }



        public fun getMovies(html: String):MutableList<Video>{

            val movieList:MutableList<Video> =ArrayList()
            val document=Jsoup.parse(html)

            val articles=document.getElementsByTag("article")

            for (a in articles){

                var book=""
                var big=""

                var id=0

                var title=""

                val aa=a.getElementsByTag("a")

                if (aa.isNotEmpty()){

                    val sa=aa[0]

                    id=sa.attr("data-post-id").toInt()
                    title=sa.attr("title")


                }

                val imgs=a.getElementsByTag("img")
                if (imgs.isNotEmpty()){

                    val img=imgs[0]
                    book=img.attr("data-src")
                }

                val v=Video(title,"","","",book,"",id,"",-1)

                movieList.add(v)

            }

            return movieList


        }


        fun parseSearchVideo(html: String):MutableList<Video>{

            val list:MutableList<Video> =ArrayList()

            val document=Jsoup.parse(html)

            val articles=document.getElementsByTag("article")

            for (article in articles){

                if (article.classNames().contains("category-117")){

                    var id=0

                    var title=""

                    val aa=article.getElementsByTag("a")

                    if (aa.isNotEmpty()){

                        id=aa[0].attr("data-post-id").toInt()
                        title=aa[0].attr("title")

                    }

                    var book=""

                    val imgs=article.getElementsByTag("img")

                    if (imgs.isNotEmpty()){

                        book=imgs[0].attr("data-src")

                    }

                    var date=""

                    val times=article.getElementsByTag("time")

                    if (times.isNotEmpty()){

                        date=times[0].text()

                    }

                    val v=Video(title,"","",date,book,"",id,book,-1)

                    list.add(v)

                }

            }

            return list

        }


        fun parseSearchEpisode(html: String):MutableList<Episode>{

            val episodeList:MutableList<Episode> =ArrayList()

            val d=Jsoup.parse(html)

            val articles=d.getElementsByTag("article")

            for (article in articles){

                if (article.classNames().contains("type-vid_playlist")){

                    var title=""
                    var book=""
                    var url=""
                    var date=""

                    val aa=article.getElementsByTag("a")

                    if (aa.isNotEmpty()){

                        title=aa[0].attr("title")

                        if (aa.size>=2){

                            url=aa[1].attr("href")

                        }

                    }


                    val imgs=article.getElementsByTag("img")
                    if (imgs.isNotEmpty()){

                        book=imgs[0].attr("data-src")

                    }

                    val times=article.getElementsByTag("time")

                    if (times.isNotEmpty()){

                        date=times[0].text()

                    }

                    val e=Episode(title,date,0,"","",book,url,book)

                    episodeList.add(e)

                }

            }

            return episodeList

        }

    }

}