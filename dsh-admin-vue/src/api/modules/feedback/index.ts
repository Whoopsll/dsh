import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { Feedback, ResPage } from '@/api/interface/index'

/**
 * @name 意见反馈管理模块
 */
export const FeedbackApi = {
  page: (params: Feedback.FeedbackQuery) => http.post<ResPage<Feedback.FeedbackVO>>(_API + '/feedback/page', params),
  delete: (params: number[]) => http.post(_API + '/feedback/delete', params)
}
