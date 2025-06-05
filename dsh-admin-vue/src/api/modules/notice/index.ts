import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { Notice, ResPage } from '@/api/interface/index'

/**
 * @name 公告管理模块
 */
export const NoticeApi = {
  page: (params: Notice.NoticeQuery) => http.post<ResPage<Notice.NoticeVO>>(_API + '/notice/page', params),
  pageByForum: (params: Notice.NoticeQuery) => http.post<ResPage<Notice.ForumNoticeVO>>(_API + '/notice/pageByForum', params),
  add: (params: any) => http.post(_API + '/notice/save', params),
  remove: (params: number[]) => http.post(_API + '/notice/delete', params),
  edit: (params: any) => http.post(_API + '/notice/update', params)
}
