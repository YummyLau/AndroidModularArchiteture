package com.effective.android.component.blog.bean

import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.blog.Constants

open class BlogArticle : Article, IMediaItem {

    constructor()

    constructor(article: Article) {
        apkLink = article.apkLink
        audit = article.audit
        author = article.author
        chapterId = article.chapterId
        chapterName = article.chapterName
        collect = article.collect
        courseId = article.courseId
        desc = article.desc
        envelopePic = article.envelopePic
        fresh = article.fresh
        id = article.id
        link = article.link
        niceDate = article.niceDate
        niceShareDate = article.niceShareDate
        origin = article.origin
        prefix = article.prefix
        projectLink = article.projectLink
        publishTime = article.publishTime
        shareData = article.shareData
        shareUser = article.shareUser
        superChapterId = article.superChapterId
        superChapterName = article.superChapterName
        tags = article.tags
        title = article.title
        type = article.type
        userId = article.userId
        visible = article.visible
        zan = article.zan
    }

    override fun getType(): Int = Constants.articleType

    override fun getViewType(): Int = Constants.articleType

    override fun replace(item: IMediaItem) {
        if (item is Article) {
            apkLink = item.apkLink
            audit = item.audit
            author = item.author
            chapterId = item.chapterId
            chapterName = item.chapterName
            collect = item.collect
            courseId = item.courseId
            desc = item.desc
            envelopePic = item.envelopePic
            fresh = item.fresh
            id = item.id
            link = item.link
            niceDate = item.niceDate
            niceShareDate = item.niceShareDate
            origin = item.origin
            prefix = item.prefix
            projectLink = item.projectLink
            publishTime = item.publishTime
            shareData = item.shareData
            shareUser = item.shareUser
            superChapterId = item.superChapterId
            superChapterName = item.superChapterName
            tags = item.tags
            title = item.title
            type = item.type
            userId = item.userId
            visible = item.visible
            zan = item.zan
        }
    }

    override fun isContentSame(item: IMediaItem): Boolean = isIdSame(item)

    override fun isIdSame(item: IMediaItem): Boolean {
        return item is Article && item.id == id
    }
}