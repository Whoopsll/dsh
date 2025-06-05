import http from '@/api'
import { _API } from '@/api/axios/servicePort'
import { Book, ResPage, BookResource } from '@/api/interface/index'
import { _api } from '@purge-icons/generated'

/**
 * @name 图书管理模块
 */
export const BookApi = {
  page: (params: Book.BookQuery) => http.post<ResPage<Book.BookVO>>(_API + '/book/page', params),
  list: () => http.post<ResPage<Book.BookVO>>(_API + '/book/list'),
  add: (params: any) => http.post(_API + '/book/save', params),
  remove: (params: number[]) => http.post(_API + '/book/delete', params),
  stickyBook: (params: any) => http.post(_API + '/book/sticky/book/' + params),
  edit: (params: any) => http.post(_API + '/book/update', params),
  importExchange: (params) =>
    http.post(_API + '/book/exchange/import', params, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),
  exchangePage: (params: Book.BookExchangeQuery) => http.post<ResPage<Book.BookExchange>>(_API + '/book/exchange/page', params),
  removeExchange: (params: number[]) => http.post(_API + `/book/exchange/delete`, params),
  downloadExchange: (bookId: number) =>
    http.post(
      _API + `/book/exchange/download?bookId=${bookId}`,
      {},
      {
        responseType: 'blob'
      }
    ),
  downloadExchangeXlsx: (bookId: number) =>
    http.post(
      _API + `/book/exchange/downloadXlsx?bookId=${bookId}`,
      {},
      {
        responseType: 'blob'
      }
    )
}

export const BookResourceApi = {
  page: (params: BookResource.BookResourceQuery) => http.post<ResPage<BookResource.BookResourceBO>>(_API + '/book/resource/page', params),
  add: (params: any) => http.post(_API + '/book/resource/save', params),
  remove: (params: number[]) => http.post(_API + '/book/resource/delete', params),
  edit: (params: any) => http.post(_API + '/book/resource/update', params),
  enabled: (params: any) => http.post(_API + '/book/resource/enabled', params),
  download: (bookId: number) =>
    http.post(
      _API + `/book/resource/download?bookId=${bookId}`,
      {},
      {
        responseType: 'blob'
      }
    ),
  importResource: (params) =>
    http.post(_API + '/book/resource/import', params, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),
  auditResourcePage: (params: BookResource.BookResourceQuery) => http.post<ResPage<BookResource.AuditResourceBO>>(_API + '/book/resource/auditPage', params)
}
