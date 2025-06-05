import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { User, ResPage } from '@/api/interface/index'

/**
 * @name 用户管理模块
 */
export const UserApi = {
  list: () => http.get(_API + '/user/list'),
  page: (params: User.UserQuery) => http.post<ResPage<User.UserVO>>(_API + '/user/page', params),
  remove: (params: number[]) => http.post(_API + '/user/delete', params)
}
