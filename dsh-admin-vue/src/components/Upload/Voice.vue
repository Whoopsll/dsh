<template>
  <div class="audio-uplaod-bar">
    <div class="upload-box">
      <el-upload
        action="#"
        :id="uuid"
        :class="['upload', self_disabled ? 'disabled' : '', drag ? 'no-border' : '']"
        :multiple="false"
        :disabled="self_disabled"
        :show-file-list="false"
        :http-request="handleHttpUpload"
        :before-upload="beforeUpload"
        :on-success="uploadSuccess"
        :on-error="uploadError"
        :drag="drag"
        :accept="fileType.join(',')"
      >
        <template v-if="voiceUrl">
          <audio controls="controls" :src="voiceUrl" class="upload-voice" style="width: 100%"></audio>
        </template>
        <template v-else>
          <div class="upload-empty">
            <slot name="empty">
              <el-icon><Plus /></el-icon>
            </slot>
          </div>
        </template>
      </el-upload>
      <div class="el-upload__tip">
        <slot name="tip"></slot>
      </div>
    </div>
    <div @click.stop>
      <el-button type="danger" @click="deleteVoice" class="mx-4">删除文件</el-button>
    </div>
  </div>
</template>

<script setup lang="ts" name="UploadImg">
import { ref, computed, inject } from 'vue'
import { uploadVoice } from '@/api/modules/upload'
import { generateUUID } from '@/utils/util'
import { ElNotification, formContextKey, formItemContextKey } from 'element-plus'
import type { UploadProps, UploadRequestOptions } from 'element-plus'

type FileTypes =
  | 'audio/mpeg' // MP3文件类型
  | 'audio/wav' // WAV文件类型
  | 'audio/ogg' // OGG文件类型

interface UploadFileProps {
  voiceUrl: string // 图片地址 ==> 必传
  api?: (params: any) => Promise<any> // 上传图片的 api 方法，一般项目上传都是同一个 api 方法，在组件里直接引入即可 ==> 非必传
  drag?: boolean // 是否支持拖拽上传 ==> 非必传（默认为 true）
  disabled?: boolean // 是否禁用上传组件 ==> 非必传（默认为 false）
  fileSize?: number // 图片大小限制 ==> 非必传（默认为 5M）
  fileType?: FileTypes[] // 图片类型限制 ==> 非必传（默认为 ["image/jpeg", "image/png", "image/gif"]）
  height?: string // 组件高度 ==> 非必传（默认为 150px）
  width?: string // 组件宽度 ==> 非必传（默认为 150px）
  borderRadius?: string // 组件边框圆角 ==> 非必传（默认为 8px）
}

// 接受父组件参数
const props = withDefaults(defineProps<UploadFileProps>(), {
  voiceUrl: '',
  drag: true,
  disabled: false,
  fileSize: 5,
  fileType: () => ['audio/mpeg', 'audio/wav', 'audio/ogg'],
  height: '150px',
  width: '150px',
  borderRadius: '8px'
})

// 生成组件唯一id
const uuid = ref('id-' + generateUUID())

// 查看图片
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
  (e: 'update:voiceUrl', value: string): void
  (e: 'check-validate'): void
}
const emit = defineEmits<UploadEmits>()
const handleHttpUpload = async (options: UploadRequestOptions) => {
  let formData = new FormData()
  formData.append('file', options.file)
  try {
    const api = props.api ?? uploadVoice
    console.log(api)
    const { data } = await api(formData)
    emit('update:voiceUrl', data.urlList[0])
    // 调用 el-form 内部的校验方法（可自动校验）
    formItemContext?.prop && formContext?.validateField([formItemContext.prop as string])
    emit('check-validate')
  } catch (error) {
    options.onError(error as any)
  }
}

/**
 * @description 删除图片
 * */
const deleteVoice = () => {
  emit('update:voiceUrl', '')
}

/**
 * @description 文件上传之前判断
 * @param rawFile 上传的文件
 * */
const beforeUpload: UploadProps['beforeUpload'] = (rawFile) => {
  console.log(rawFile)
  const voiceSize = rawFile.size / 1024 / 1024 < props.fileSize
  const fileType = props.fileType
  if (!fileType.includes(rawFile.type as FileTypes))
    ElNotification({
      title: '温馨提示',
      message: '上传文件不符合所需的格式！',
      type: 'warning'
    })
  if (!voiceSize)
    ElNotification({
      title: '温馨提示',
      message: `上传文件大小不能超过 ${props.fileSize}M！`,
      type: 'warning'
    })
  return fileType.includes(rawFile.type as FileTypes) && voiceSize
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
.audio-uplaod-bar {
  display: flex;
  justify-content: center;
  align-items: center;
}
.is-error {
  .upload {
    :deep(.el-upload),
    :deep(.el-upload-dragger) {
      border: 1px dashed var(--el-color-danger) !important;

      &:hover {
        border-color: var(--el-color-primary) !important;
      }
    }
  }
}

:deep(.disabled) {
  .el-upload,
  .el-upload-dragger {
    cursor: not-allowed !important;
    background: var(--el-disabled-bg-color);
    border: 1px dashed var(--el-border-color-darker) !important;

    &:hover {
      border: 1px dashed var(--el-border-color-darker) !important;
    }
  }
}

.upload-box {
  .no-border {
    :deep(.el-upload) {
      border: none !important;
    }
  }

  :deep(.upload) {
    .el-upload {
      position: relative;
      display: flex;
      align-items: center;
      justify-content: center;
      width: v-bind(width);
      height: v-bind(height);
      overflow: hidden;
      border: 1px dashed var(--el-border-color-darker);
      border-radius: v-bind(borderRadius);
      transition: var(--el-transition-duration-fast);

      &:hover {
        border-color: var(--el-color-primary);

        .upload-handle {
          opacity: 1;
        }
      }

      .el-upload-dragger {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 100%;
        padding: 0;
        overflow: hidden;
        background-color: transparent;
        border: 1px dashed var(--el-border-color-darker);
        border-radius: v-bind(borderRadius);

        &:hover {
          border: 1px dashed var(--el-color-primary);
        }
      }

      .el-upload-dragger.is-dragover {
        background-color: var(--el-color-primary-light-9);
        border: 2px dashed var(--el-color-primary) !important;
      }

      .upload-voice {
        width: 100%;
        height: 100%;
        object-fit: contain;
      }

      .upload-empty {
        position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        line-height: 30px;
        color: var(--el-color-info);

        .el-icon {
          font-size: 28px;
          color: var(--el-text-color-secondary);
        }
      }

      .upload-handle {
        position: absolute;
        top: 0;
        right: 0;
        display: flex;
        width: 100%;
        height: 100%;
        cursor: pointer;
        background: rgb(0 0 0 / 60%);
        opacity: 0;
        box-sizing: border-box;
        transition: var(--el-transition-duration-fast);
        align-items: center;
        justify-content: center;

        .handle-icon {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          padding: 0 6%;
          color: aliceblue;

          .el-icon {
            margin-bottom: 40%;
            font-size: 130%;
            line-height: 130%;
          }

          span {
            font-size: 85%;
            line-height: 85%;
          }
        }
      }
    }
  }

  .el-upload__tip {
    line-height: 18px;
    text-align: center;
  }
}
</style>
