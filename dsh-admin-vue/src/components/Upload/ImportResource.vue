<template>
  <div class="upload-box">
    <el-upload
      action="#"
      :id="uuid"
      :multiple="false"
      :disabled="self_disabled"
      :show-file-list="false"
      :http-request="handleHttpUpload"
      :before-upload="beforeUpload"
      :on-success="uploadSuccess"
      :on-error="uploadError"
      :accept="fileType.join(',')"
    >
      <slot name="empty">
        <el-icon><Plus /></el-icon>
        <!-- <span>请上传图片</span> -->
      </slot>
    </el-upload>
  </div>
</template>

<script setup lang="ts" name="UploadImg">
import { ref, computed, inject } from 'vue'
import { generateUUID } from '@/utils/util'
import { ElNotification, formContextKey, formItemContextKey } from 'element-plus'
import type { UploadProps, UploadRequestOptions } from 'element-plus'

// excel 文件
type FileTypes = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'

interface UploadFileProps {
  code: string // 图片地址 ==> 必传
  type: number //资源类型
  api: (params: any) => Promise<any> // 上传图片的 api 方法，一般项目上传都是同一个 api 方法，在组件里直接引入即可 ==> 非必传
  drag?: boolean // 是否支持拖拽上传 ==> 非必传（默认为 true）
  disabled?: boolean // 是否禁用上传组件 ==> 非必传（默认为 false）
  fileSize?: number // 图片大小限制 ==> 非必传（默认为 5M）
  fileType?: FileTypes[] // 图片类型限制 ==> 非必传（默认为 ["image/jpeg", "image/png", "image/gif"]）
  height?: string // 组件高度 ==> 非必传（默认为 150px）
  width?: string // 组件宽度 ==> 非必传（默认为 150px）
  borderRadius?: string // 组件边框圆角 ==> 非必传（默认为 8px）
  bookId?: number
}

// 接受父组件参数
const props = withDefaults(defineProps<UploadFileProps>(), {
  code: '',
  drag: true,
  disabled: false,
  fileSize: 1,
  fileType: () => ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'],
  height: '150px',
  width: '150px',
  borderRadius: '8px',
  bookId: 0
})

// 生成组件唯一id
const uuid = ref('id-' + generateUUID())

// 获取 el-form 组件上下文
const formContext = inject(formContextKey, void 0)
// 获取 el-form-item 组件上下文
const formItemContext = inject(formItemContextKey, void 0)
// 判断是否禁用上传和删除
const self_disabled = computed(() => {
  return props.disabled || formContext?.disabled
})

/**
 * @description 图片上传
 * @param options 上传的文件
 * */
interface UploadEmits {
  (e: 'update:code', value: string): void
  (e: 'check-validate'): void
}
const emit = defineEmits<UploadEmits>()
const handleHttpUpload = async (options: UploadRequestOptions) => {
  let formData = new FormData()
  formData.append('file', options.file)
  formData.append('bookId', props.bookId)
  formData.append('type', props.type)
  try {
    const { code } = await props.api(formData)
    console.log(code)
    emit('update:code', code)
    // 调用 el-form 内部的校验方法（可自动校验）
    formItemContext?.prop && formContext?.validateField([formItemContext.prop as string])
    emit('check-validate')
  } catch (error) {
    options.onError(error as any)
  }
}

/**
 * @description 文件上传之前判断
 * @param rawFile 上传的文件
 * */
const beforeUpload: UploadProps['beforeUpload'] = (rawFile) => {
  console.log(rawFile)
  const imgSize = rawFile.size / 1024 / 1024 < props.fileSize
  const imgType = props.fileType
  if (!imgType.includes(rawFile.type as FileTypes))
    ElNotification({
      title: '温馨提示',
      message: '上传文件不符合所需的格式！',
      type: 'warning'
    })
  if (!imgSize)
    ElNotification({
      title: '温馨提示',
      message: `上传文件大小不能超过 ${props.fileSize}M！`,
      type: 'warning'
    })
  return imgType.includes(rawFile.type as FileTypes) && imgSize
}

// 图片上传成功提示
const uploadSuccess = () => {
  ElNotification({
    title: '温馨提示',
    message: '上传成功！',
    type: 'success'
  })
}

// 图片上传错误提示
const uploadError = () => {
  ElNotification({
    title: '温馨提示',
    message: '上传失败，请您重新上传！',
    type: 'error'
  })
}
</script>
<style scoped lang="less">
.upload-box {
  .no-border {
    :deep(.el-upload) {
      border: none !important;
    }
  }

  :deep(.el-upload-dragger) {
    .el-upload-dragger {
      display: flex;
      align-items: center;
      justify-content: center;
      overflow: hidden;
      padding: 0 !important;
      background-color: transparent;
    }
  }
}
</style>
