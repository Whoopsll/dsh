import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { ReqPage, ResPage, Audit } from '@/api/interface/index'
import { _api } from '@purge-icons/generated'

/**
 * @name 内容审核管理模块
 */
export const AuditApi = {
  page: (params: ReqPage) => http.post<ResPage<Audit.AuditVO>>(_API + '/contentAudit/page', params),
  add: (params: any) => http.post(_API + '/contentAudit/save', params),
  remove: (params: number[]) => http.post(_API + '/contentAudit/del', params),
  getAuditDetail: (params: any) => http.post(_API + '/contentAudit/detail?taskId=' + params)
}
