import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { Publisher, ResPage } from '@/api/interface/index'

/**
 * @name 出版社管理模块
 */
export const PublisherApi = {
  page: (params: Publisher.PublisherQuery) => http.post<ResPage<Publisher.PublisherVO>>(_API + '/publisher/page', params),
  list: () => http.get<Publisher.PublisherVO[]>(_API + '/publisher/list'),
  add: (params: any) => http.post(_API + '/publisher/save', params),
  remove: (params: number[]) => http.post(_API + '/publisher/delete', params),
  edit: (params: any) => http.post(_API + '/publisher/update', params)
}
