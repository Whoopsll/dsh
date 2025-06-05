import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { ReqPage, ResPage, Audit } from '@/api/interface/index'
import { _api } from '@purge-icons/generated'

/**
 * @name 审核管理模块
 */
export const AuditApi = {
  page: (params: ReqPage) => http.post<ResPage<Audit.AuditVO>>(_API + '/audit/page', params),
  add: (params: any) => http.post(_API + '/audit/save', params),
  remove: (params: number[]) => http.post(_API + '/audit/delete', params),
  edit: (params: Audit.AuditDTO) => http.post(_API + '/audit/modify', params),
  startAuditTask: (params: any) => http.post(_API + '/audit/startAudit', params, { headers: { noLoading: true } }),
  getAuditTask: (params: any) => http.post(_API + '/audit/getAuditTask', { params }, { headers: { noLoading: true } }),
  stopAuditTask: (params: any) => http.post(_API + '/audit/stopAuditTask', { params }),
  downloadReport: (params: any) => http.post(_API + '/audit/download', { params })
}
