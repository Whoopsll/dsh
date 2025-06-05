import { http } from '@/utils/https.js'

const FORUM_API = '/forum'

/**
 * 论坛分页
 * @param forumQuery 查询体
 * @returns
 */
export const forumPage = (forumQuery) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/page',
    data: forumQuery
  })
}

/**
 * 关注论坛分页
 * @param forumQuery 查询体
 * @returns
 */
export const followedPage = (forumQuery) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/followedPage',
    data: forumQuery
  })
}

/**
 * 论坛详细
 * @param forumId id
 * @returns
 */
export const forumDetail = (param) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/detail?forumId=' + param.forumId,
    data: param
  })
}

/**
 * 关注论坛
 * @param forumId id
 * @returns
 */
export const forumFollowed = (forumId) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/followed?forumId=' + forumId,
  })
}

/**
 * 评论
 * @param commentDTO dto
 * @returns
 */
export const forumCommentAdd = (commentDTO) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/comment/add',
    data: commentDTO
  })
}

/**
 * 评论修改
 * @param commentDTO dto
 * @returns
 */
export const forumCommentModify = (commentDTO) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/comment/modify',
    data: commentDTO
  })
}

/**
 * 评论列表
 * @param forumId id
 * @returns
 */
export const commentList = (forumId) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/comment/list?forumId=' + forumId,
  })
}

/**
 * 评论详情
 * @param commentId id
 * @returns
 */
export const commentDetail = (commentId) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/comment?commentId=' + commentId,
  })
}


/**
 * 评论行为
 * @param CommentActionDTO dto
 * @returns
 */
export const doCommentAction = (commentActionDTO) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/comment/action',
    data: commentActionDTO
  })
}

/**
 * 获取喜欢的书籍
 * @param param
 * @returns
 */
export const getLikedBook = (param) => {
  return http({
    method: 'POST',
    url: FORUM_API + '/like?bookId=' + param.bookId + "&type=" + param.type,
    data: param
  })
}

