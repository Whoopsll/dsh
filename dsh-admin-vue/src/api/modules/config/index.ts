import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { ReqPage, ResPage, Config } from '@/api/interface/index'
import { _api } from '@purge-icons/generated'

/**
 * @name 配置模块
 */
export const ConfigApi = {
  page: (params: ReqPage) => http.post<ResPage<Config.ConfigVO>>(_API + '/config/list', params, { headers: { noLoading: true } }),
  remove: (params: Config.ConfigDTO) => http.post(_API + '/config/delete', params),
  edit: (params: Config.ConfigDTO) => http.post(_API + '/config/modify', params),
  getDetailById: (id: number) => http.post(_API + '/config/get/' + id),
  createMenu: (params: any) => http.post(_API + '/wechat/menu/add', params),
  getMenuByConfigId: (params: any) => http.post(_API + '/wechat/menu/customMenu/' + params.configId, params)
}
