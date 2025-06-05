import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { Category, ResPage } from '@/api/interface/index'

/**
 * @name 分类管理模块
 */
export const CategoryApi = {
  page: (params: Category.CategoryQuery) => http.post<ResPage<Category.CategoryVO>>(_API + '/category/page', params),
  list: () => http.get<Category.CategoryVO[]>(_API + '/category/list'),
  add: (params: any) => http.post(_API + '/category/save', params),
  remove: (params: number[]) => http.post(_API + '/category/delete', params),
  edit: (params: any) => http.post(_API + '/category/update', params)
}
