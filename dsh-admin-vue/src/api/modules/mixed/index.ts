import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { Forum, ResPage, Comment } from '@/api/interface/index'

/**
 * @name 论坛融合管理模块
 */
export const MixedApi = {
  page: (params: Forum.ForumQuery) => http.post<ResPage<Forum.ForumVO>>(_API + '/forum/page', params),
  add: (params: any) => http.post(_API + '/forum/save', params),
  remove: (params: number[]) => http.post(_API + '/forum/delete', params),
  edit: (params: any) => http.post(_API + '/forum/update', params),
  stickyForum: (params: any) => http.post(_API + '/forum/sticky/' + params)
}
/**
 * @name 论坛帖子模块
 */
export const CommentApi = {
  list: (params: number) => http.post<Comment.CommentItemVO[]>(_API + '/forum/comment/list?forumId=' + params),
  remove: (params: number) => http.post(_API + '/forum/comment/delete?commentId=' + params),
  enabled: (params: number) => http.post(_API + '/forum/comment/enabled?commentId=' + params)
}
