import { http } from '@/utils/https.js'

const USER_API = '/user'

export const queryUserInfo = () => {
  return http({
    method: 'POST',
    url: USER_API + '/userInfo'
  })
}
export const starCommentList = (query) => {
  return http({
    method: 'POST',
    url: USER_API + '/starCommentList',
    data: query
  })
}
export const myCommentList = (query) => {
  return http({
    method: 'POST',
    url: USER_API + '/myCommentList',
    data: query
  })
}
export const reviewCommentList = (query) => {
  return http({
    method: 'POST',
    url: USER_API + '/reviewCommentList',
    data: query
  })
}
export const feedback = (params) => {
  return http({
    method: 'POST',
    url: USER_API + '/feedback',
    data: params
  })
}
export const questionList = (query) => {
  return http({
    method: 'POST',
    url: USER_API + '/questionList',
    data: query
  })
}
