import axios from 'axios'
import { useAppStoreWithOut } from '@/store/modules/app'
const baseUrl = import.meta.env.VITE_API_URL as string
export const downloadPdf = async () => {
  const appStore = useAppStoreWithOut()

  axios({
    url: baseUrl + 'dsh-admin-api/audit/download',
    method: 'GET',
    responseType: 'blob',
    headers: {
      Authorization: appStore.token
    }
  }).then((res: any) => {
    if (res.data.size > 0) {
      const content = res.data
      const blob = new Blob([content])
      const fileName = `审核报告.pdf`
      if ('download' in document.createElement('a')) {
        // 非IE下载
        const elink = document.createElement('a')
        elink.download = fileName
        elink.style.display = 'none'
        elink.href = URL.createObjectURL(blob)
        document.body.appendChild(elink)
        elink.click()
        URL.revokeObjectURL(elink.href) // 释放URL 对象
        document.body.removeChild(elink)
      }
    }
  })
}

//下载其他审核的报告
export const downloadAuditPdf = async (auditId: any, title: any) => {
  const appStore = useAppStoreWithOut()
  axios({
    url: baseUrl + 'dsh-admin-api/contentAudit/download?auditId=' + auditId,
    method: 'GET',
    responseType: 'blob',
    headers: {
      Authorization: appStore.token
    }
  }).then((res: any) => {
    const contentType = res.headers['content-type']
    console.log('contentType===' + contentType)
    if (contentType.includes('application/json')) {
      const reader = new FileReader()
      reader.onload = function () {
        console.error('下载失败:', reader.result)
      }
      reader.readAsText(res.data)
      return
    }
    if (res.data.size > 0) {
      const content = res.data
      const blob = new Blob([content])
      const fileName = title + `审核报告.pdf`
      if ('download' in document.createElement('a')) {
        // 非IE下载
        const elink = document.createElement('a')
        elink.download = fileName
        elink.style.display = 'none'
        elink.href = URL.createObjectURL(blob)
        document.body.appendChild(elink)
        elink.click()
        URL.revokeObjectURL(elink.href) // 释放URL 对象
        document.body.removeChild(elink)
      }
    }
  })
}
