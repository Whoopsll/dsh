<template>
  <Dialog
    style="background-color: #f5f6f7"
    :model-value="dialogVisible"
    :title="dialogProps.title"
    :fullscreen="dialogProps.fullscreen"
    :width="'80vw'"
    :cancel-dialog="cancelDialog"
  >
    <div style="display: flex">
      <div style="width: 30%; display: flex; justify-content: center">
        <div style="width: 70%; margin-right: 0px">
          <!-- 第一行 -->
          <div style="background-color: #fdfdfd; border-radius: 10px; padding-top: 10px">
            <img src="@/assets/icons/phone_review_top.png" style="height: 70px" />
            <div style="height: 300px"> </div>
            <!-- 最底部 -->
            <div class="bottom">
              <img src="@/assets/icons/icon_keyboard.png" alt="" />
              <div class="bottom-left">
                <!-- <div style="flex: 2; display: flex; align-items: center; justify-content: center"> -->
                <div v-for="(menu, index) in menusData" :key="index" :class="currentSelectedId == menu.key ? 'menu-item-selected' : 'menu-item'">
                  <!-- 添加子集弹框 -->
                  <div
                    v-show="currentMainMenuIndex == index"
                    style="position: absolute; bottom: 70px; border: 1px solid #f0f0f1; background-color: #fdfdfd; width: 100%; text-align: center; border-radius: 5px"
                  >
                    <div
                      v-for="(item, index1) in menu.sub_button"
                      :key="index1"
                      :class="currentSelectedId == item.key ? 'child-menu-item-selected' : 'child-menu-item'"
                      @click="handleSubMenuClick(item)"
                      style="position: relative"
                    >
                      <!-- 子菜单右侧弹框 -->
                      <div v-show="currentSelectedId == item.key" class="subMenu-operation-btn" @click="handleSubMenuDelete(menu, item)">
                        <img src="@/assets/icons/icon_delete.png" style="width: 16px; height: 18px" alt="" />
                      </div>
                      <span>{{ item.name }}</span></div
                    >
                    <!-- 添加按钮 -->
                    <div class="child-menu-item" @click="handleSubMenuAddClick(menu)">
                      <img src="@/assets/icons/icon_add.png" alt="" style="width: 14px; height: 14px" /> <span>添加</span></div
                    >
                  </div>
                  <!-- 底部菜单 -->
                  <div style="height: 30px; width: 1px; background-color: #f0f0f1"></div>
                  <span style="display: block; width: 100%; width: 100%; text-align: center" @click="handleMenuClick(index, menu)"> {{ menusData[index].name }}</span>
                  <div @click="handleMenuDelete(menu)" class="sub-icon-delete" v-show="currentSelectedId == menu.key">
                    <img src="@/assets/icons/icon_delete.png" style="width: 16px; height: 18px" alt="" />
                  </div>
                </div>
                <!-- </div> -->
                <div @click="handleMenuAdd()" v-if="menusData.length < 3" class="menu-add-btn" :class="menusData.length == 0 ? 'menu-add-btn-selected' : 'menu-add-btn-border'">
                  <img v-show="menusData.length > 0" src="@/assets/icons/icon_add.png" alt="" style="width: 18px; height: 18px" />
                  <img v-show="menusData.length == 0" src="@/assets/icons/icon_add_selected.png" alt="" style="width: 18px; height: 18px" />
                  <span v-show="menusData.length == 0">添加菜单</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div style="width: 70%; background-color: #fdfdfd; padding: 20px 25px; border-radius: 10px">
        <el-form
          ref="ruleFormRef"
          label-position="left"
          :label-width="'100px'"
          :rules="rules"
          :model="currentMenuData"
          :disabled="dialogProps.isView"
          :hide-required-asterisk="dialogProps.isView"
        >
          <div style="margin-bottom: 30px">
            <span style="font-weight: bold; font-size: 16px">菜单信息</span>
          </div>
          <el-form-item label="名称" prop="name">
            <div style="width: 100%">
              <el-input v-model="currentMenuData!.name" style="width: 55%" placeholder="菜单名称" clearable></el-input>
              <label style="color: #b1b2b3; display: block">仅支持中英文和数字，字数不超过4个汉字或8个字母</label>
            </div>
          </el-form-item>
          <el-form-item label="消息类型" prop="type">
            <el-radio-group v-model="currentMenuData!.type">
              <el-radio value="click">发送消息</el-radio>
              <el-radio value="view">跳转网页</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item :label="currentMenuData.type == 'click' ? '消息内容' : '网页链接'" prop="name">
            <el-input v-if="currentMenuData.type == 'view'" style="width: 55%" v-model="currentMenuData.urlContent" placeholder="公众号链接" clearable></el-input>
            <div
              v-if="currentMenuData.type === 'click' && currentMenuData.replyType != 0 && (currentMenuData.pictureUrl === '' || currentMenuData.pictureUrl === null)"
              style="display: flex; background-color: #f5f6f7; border-radius: 5px; padding: 0 5px"
            >
              <div class="icon" @click="currentMenuData.replyType = 0"><img style="height: 16px; width: 16px" src="@/assets/icons/icon_text.png" /><span>文本</span></div>
              <div class="icon" @click="handleUploadPicture()"><img style="height: 20px; width: 20px" src="@/assets/icons/icon_picture.png" /><span>图片</span></div>
            </div>
            <!-- 文本输入框 -->
            <div style="display: flex" v-if="currentMenuData.replyType == 0 && currentMenuData.type == 'click'">
              <div class="textarea">
                <textarea maxlength="600" v-model="currentMenuData.textContent" rows="5" class="textarea" />
                <div class="textarea-tip">
                  <img src="@/assets/icons/icon_face.png" />
                  <span>还可以输入{{ 600 - currentMenuData.textContent.length }}字，按下Enter键换行</span>
                </div>
              </div>
              <div class="icon-delete" @click="handleTextDelete()">
                <img src="@/assets/icons/icon_delete.png" />
              </div>
            </div>
            <!-- 图片上传 -->
            <el-upload
              ref="uploadRef"
              v-show="currentMenuData.pictureUrl !== '' && currentMenuData.pictureUrl != null && currentMenuData.type == 'click'"
              v-model:file-list="fileList"
              :headers="uploadHeaders"
              :action="uploadUrl + 'dsh-admin-api/wechat/uploadResource/' + dialogProps.row.id"
              list-type="picture-card"
              :limit="1"
              :on-preview="handlePictureCardPreview"
              :on-success="handlePictureCardSuccess"
              :on-remove="handleRemove"
            >
              <template #empty>
                <el-icon><Avatar /></el-icon>
                <span>请上传图片</span>
              </template>
              <template #tip> 图片大小不能超过 5M </template>
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </el-form>
        <div class="sub-btn">
          <span @click="handleSubmitBtn()">保存并发布</span>
        </div>
      </div>
    </div>
    <template #footer>
      <slot name="footer">
        <el-button @click="cancelDialog">取消</el-button>
        <el-button type="primary" v-show="!dialogProps.isView" @click="handleSubmit">确定</el-button>
      </slot>
    </template>
  </Dialog>

  <el-dialog v-model="previewVisible">
    <img w-full :src="currentMenuData.pictureUrl" alt="Preview Image" />
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
// import UploadImg from '@/components/Upload/Img.vue'
import { ElMessage, FormInstance, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useAppStoreWithOut } from '@/store/modules/app'
import { Dialog } from '@/components/Dialog'
import { ConfigApi } from '@/api/modules/config'

interface DialogProps {
  title: string
  isView: boolean
  fullscreen?: boolean
  row: any
  publisherId: number
  labelWidth?: number
  maxHeight?: number | string
  api?: (params: any) => Promise<any>
  getTableList?: () => Promise<any>
}
// const textType = ref(-1) //发送的文本类型，0:文本，1:图片
const currentSelectedId = ref('')
const currentMainMenuIndex = ref(-1)
const uploadUrl = ref('')
const currentMenuData = ref({
  type: ''
})
//记录上传的图片
const fileList = ref([])
//记录media_id
const menusData = ref([])
const uploadHeaders = ref({})
const dialogVisible = ref(false)
const dialogProps = ref<DialogProps>({
  isView: false,
  title: '',
  row: {},
  labelWidth: 160,
  publisherId: 0,
  fullscreen: false,
  maxHeight: '500px'
})

const uploadRef = ref()
// 处理点击图片上传事件
const handleUploadPicture = () => {
  currentMenuData.value.replyType = 1
  uploadRef.value.$el.querySelector('input[type="file"]').click()
}
//处理主菜单删除事件
const handleMenuDelete = (menu: any) => {
  // 删除菜单信息
  const index = menusData.value.findIndex((item) => item.key === menu.key)
  menusData.value.splice(index, 1)
}
//处理子菜单删除事件
const handleSubMenuDelete = (menu: any, itemValue: any) => {
  // 删除子菜单信息
  const index = menu.sub_button.findIndex((item) => item.key === itemValue.key)
  menu.sub_button.splice(index, 1)
}
//处理删除按钮事件
const handleTextDelete = () => {
  currentMenuData.value.replyType = -1
  textContent.value = ''
}
//处理添加父级菜单事件
const handleMenuAdd = () => {
  let key = generateUniqueId()
  menusData.value.push({ type: 'click', name: '菜单' + (menusData.value.length + 1), replyType: -1, key: key, sub_button: [], pictureUrl: '', textContent: '' })
  currentMenuData.value = menusData.value[menusData.value.length - 1]
  currentMainMenuIndex.value = menusData.value.length - 1
  currentSelectedId.value = key
}
// 处理子类添加事件
const handleSubMenuAddClick = (menu: any) => {
  if (menu.sub_button.length == 0) {
    ElMessageBox.confirm('添加子菜单，底部菜单事件会被禁用，确认添加吗？', '温馨提示！', {
      cancelButtonText: '取消',
      confirmButtonText: '确认',
      type: 'warning'
    })
      .then(() => {
        let key = generateUniqueId()
        menu.sub_button.unshift({ type: 'click', name: '子菜单' + (menu.sub_button.length + 1), replyType: -1, key: key, pictureUrl: '', textContent: '' })
        currentMenuData.value = menu.sub_button[menu.sub_button.length - 1]
        currentSelectedId.value = key
      })
      .catch(() => {})
    return
  }
  if (menu.sub_button.length >= 5) {
    ElMessage({
      type: 'warning',
      message: '最多只能添加5个子菜单'
    })
    return
  }
  let key = generateUniqueId()
  menu.sub_button.unshift({ type: 'click', name: '子菜单' + (menu.sub_button.length + 1), replyType: -1, key: key, pictureUrl: '', textContent: '' })
  currentMenuData.value = menu.sub_button[0]
  currentSelectedId.value = key
  console.log(currentMenuData.value)
}
//处理子类点击事件
const handleSubMenuClick = (item: any) => {
  currentSelectedId.value = item.key
  currentMenuData.value = item
  if (currentMenuData.value.replyType === 1) {
    //图片类型
    fileList.value = [
      {
        name: '',
        url: currentMenuData.value.pictureUrl
      }
    ]
  }
}
//处理点击菜单事件
const handleMenuClick = (index: number, menu: any) => {
  currentSelectedId.value = menu.key
  currentMenuData.value = menu
  // if (menu.sub_button.length > 0) {
  //   //如果有子类，则默认选中第一个子类
  //   currentMenuData.value = menu.sub_button[0]
  //   currentSelectedId.value = currentMenuData.value.key
  // } else {
  //   currentSelectedId.value = menu.key
  //   currentMenuData.value = menu
  // }
  currentMainMenuIndex.value = index
  if (currentMenuData.value.replyType == 1) {
    //图片类型
    fileList.value = [
      {
        name: '',
        url: currentMenuData.value.pictureUrl
      }
    ]
  }
}
// 点击提交按钮
const handleSubmitBtn = () => {
  var datas = JSON.parse(JSON.stringify(menusData.value)) // 深拷贝数据
  var requestBody = [] // 接口请求参数，存放菜单信息
  datas.forEach((item) => {
    // 构建父类data，存入数据库
    var data = {
      keyId: item.key,
      type: item.type,
      name: item.name,
      url: item.urlContent,
      parentKey: '',
      appId: dialogProps.value.row.account,
      replyContent: item.replyType === 0 ? item.textContent : item.pictureUrl,
      mediaId: item.replyType === 0 ? '' : item.mediaId
    }
    if (item.type === 'view' || item.type === 'VIEW') {
      //如果是网页类型，构建url
      item.url = item.urlContent
      delete item.urlContent
    }
    requestBody.push(data)
    // 如果有 children 且不为空，则删除 type 属性
    if (item.sub_button && item.sub_button.length > 0) {
      //遍历子菜单
      item.sub_button.forEach((subItem) => {
        var data = {
          keyId: subItem.key,
          type: subItem.type,
          name: subItem.name,
          url: subItem.urlContent,
          parentKey: item.key,
          replyType: subItem.replyType,
          appId: dialogProps.value.row.account,
          replyContent: subItem.replyType === 0 ? subItem.textContent : subItem.pictureUrl,
          mediaId: subItem.replyType === 0 ? '' : subItem.mediaId
        }
        if (subItem.type === 'view' || subItem.type === 'VIEW') {
          //如果是网页类型，构建url
          subItem.url = subItem.urlContent
          delete subItem.urlContent
        }
        // 构建item对象
        requestBody.push(data)
        delete subItem.pictureUrl // 删除子类的 pictureUrl 属性
        delete subItem.textContent // 删除子类的 textContent 属性
        delete subItem.replyType
        delete subItem.parentKey
      })
      delete item.type // 删除父类的 type 属性
    }

    // 构建item对象
    delete item.pictureUrl // 删除父类的 pictureUrl 属性
    delete item.textContent // 删除父类的 textContent 属性
    delete item.replyType
    delete item.parentKey
  })
  const param = {
    button: datas
  }
  //构建最终的请求参数 params
  const requestParams = {
    datas: requestBody,
    menuBody: JSON.stringify(param),
    configId: dialogProps.value.row.id
  }
  //发送请求
  ConfigApi.createMenu(requestParams).then((res) => {
    console.log('回掉内容')
    console.log(res)
    if (res.code == 0) {
      ElMessage.success({ message: '菜单发布成功！' })
    } else {
      ElMessage.error({ message: '菜单发布失败！' })
    }
  })
}

// 获取自定义菜单列表
const getCustomMenuList = () => {
  ConfigApi.getMenuByConfigId({ configId: dialogProps.value.row.id }).then((res) => {
    if (res.code == 0) {
      menusData.value = res.data
      if (menusData.value.length > 0) {
        if (menusData.value[0].sub_button.length > 0) {
          //默认第一个子菜单
          currentMenuData.value = menusData.value[0].sub_button[0]
          currentMainMenuIndex.value = 0
        } else {
          currentMenuData.value = menusData.value[0]
        }
        currentSelectedId.value = currentMenuData.value.key
        if (currentMenuData.value.type === 'click' || currentMenuData.value.type === 'CLICK') {
          // 发送信息跳转
          if (currentMenuData.value.replyType === 1) {
            //图片类型
            fileList.value = [
              {
                name: '',
                url: currentMenuData.value.pictureUrl
              }
            ]
          }
        }
        if (currentMenuData.value.type === 'view' || currentMenuData.value.type === 'VIEW') {
          //网页跳转
          currentMenuData.value.urlContent = currentMenuData.value.url
        }
      }
    } else {
      ElMessage.error({ message: '获取菜单失败！' })
    }
  })
}
// 生成唯一id
const generateUniqueId = () => {
  // 使用当前时间戳 + 随机数来生成唯一字符串
  const timestamp = Date.now().toString(36) // 时间戳转换为36进制字符串
  const randomStr = Math.random().toString(36).substring(2, 10) // 随机数转换为36进制并截取部分
  return (timestamp + randomStr).substring(0, 16) // 拼接并截取16位
}
// 接收父组件传过来的参数
const acceptParams = (params: DialogProps): void => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
  menusData.value = []
  currentMenuData.value = {
    name: '',
    type: ''
  }
  uploadUrl.value = import.meta.env.VITE_API_URL as string
  uploadHeaders.value = {
    Authorization: useAppStoreWithOut().token
  }
  // 获取菜单列表
  getCustomMenuList()
}

defineExpose({
  acceptParams
})
//图片上传成功
const handlePictureCardSuccess: UploadProps['onSuccess'] = (res) => {
  console.log(res)
  if (res.code == 0) {
    //上传成功
    currentMenuData.value.pictureUrl = res.data.url
    currentMenuData.value.mediaId = res.data.mediaId
    // mediaId.value = res.data.mediaId
    console.log(currentMenuData.value)
  }
  // currentMenuData.value.pictureUrl = res.data.url
}
// 上传图片
const previewVisible = ref(false)
const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
  console.log(uploadFile, uploadFiles)
  currentMenuData.value.pictureUrl = ''
}

const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
  currentMenuData.value.pictureUrl = uploadFile.url!
  previewVisible.value = true
}
const rules = reactive({
  // name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  // account: [{ required: true, message: '请输入微信公众号key', trigger: 'blur' }],
  // password: [{ required: true, message: '请输入微信公众号Secret', trigger: 'blur' }],
  // logo: [{ required: true, message: '请上传logo', trigger: 'blur' }]
  //   link: [
  //     {
  //       required: true,
  //       message: '链接不能为空',
  //       trigger: 'blur'
  //     },
  //     {
  //       validator: (rule, value, callback) => {
  //         const urlPattern = /^(https?:\/\/)([a-zA-Z0-9.-]+)(\.[a-zA-Z]{2,})(:[0-9]{1,5})?(\/.*)?$/
  //         if (!urlPattern.test(value)) {
  //           callback(new Error('请输入合法的链接'))
  //         } else {
  //           callback()
  //         }
  //       },
  //       trigger: 'blur'
  //     }
  //   ]
})
const ruleFormRef = ref<FormInstance>()
const handleSubmit = () => {
  ruleFormRef.value!.validate(async (valid) => {
    if (!valid) return
    try {
      dialogProps.value.row.publisherId = dialogProps.value.publisherId
      await dialogProps.value.api!(dialogProps.value.row)
      ElMessage.success({ message: `${dialogProps.value.title}成功！` })
      dialogProps.value.getTableList!()
      dialogVisible.value = false
      ruleFormRef.value!.resetFields()
      cancelDialog(true)
    } catch (error) {
      console.log(error)
    }
  })
}
const cancelDialog = (isClean?: boolean) => {
  dialogVisible.value = false
  let condition = ['查看', '编辑']
  if (condition.includes(dialogProps.value.title) || isClean) {
    dialogProps.value.row = {}
    ruleFormRef.value!.resetFields()
  }
}
</script>

<style scoped lang="less">
.icon {
  display: flex;
  align-items: center;
  margin-right: 10px;
  cursor: pointer;

  img {
    margin: 0 5px;
  }
}

.icon-delete {
  display: flex;
  justify-content: center;
  width: 35px;
  height: 35px;
  align-items: center;
  border-radius: 50%;
  cursor: pointer;
  background-color: #f6f6f6;

  img {
    width: 18px;
    height: 20px;
  }
}

.icon-delete:hover {
  background-color: #a8a9aa;
}

.sub-icon-delete {
  display: flex;
  justify-content: center;
  width: 35px;
  height: 35px;
  border-radius: 50%;
  align-items: center;
  border-radius: 20px;
  cursor: pointer;
  background-color: #fdfdfd;
  position: absolute;
  top: 70px;
}

.textarea {
  border: 1px solid #d7dae2;
  padding: 5px;
  margin-right: 10px;
  width: 350px;
  border-radius: 5px;

  textarea {
    width: 100%;
    outline: none;
    border: none;
    resize: none;
  }

  .textarea-tip {
    display: flex;
    align-items: center;
    color: #a8a9aa;
    justify-content: center;
    font-size: 14px;

    img {
      height: 20px;
      width: 20px;
      margin-right: 10px;
    }
  }
}
.bottom {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  border-top: 1px solid #f0f0f1;

  img {
    height: 24px;
    width: 24px;
    margin: 0 10px;
  }

  .bottom-left {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: center;

    .menu-item {
      flex: 1;
      cursor: pointer;
      position: relative;
      display: flex;
      height: 60px;
      justify-content: center;
      align-items: center;
      font-size: 16px;
    }

    .menu-item-selected {
      flex: 1;
      cursor: pointer;
      position: relative;
      display: flex;
      height: 60px;
      justify-content: center;
      align-items: center;
      font-size: 16px;
      border: 1px solid #05ba5b;
      color: #05ba5b;
    }

    .child-menu-item {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 10px 0;
      color: #797b7e;
      font-size: 14px;
      border-bottom: 1px solid #f0f0f1;
    }

    .child-menu-item-selected {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 10px 0;
      font-size: 14px;
      border: 1px solid #05ba5b;
      color: #05ba5b;
    }

    .subMenu-operation-btn {
      position: absolute;
      right: -50px;
      width: 35px;
      height: 35px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      background-color: #fdfdfd;
      box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
    }

    .menu-add-btn {
      font-size: 32px;
      flex: 1;
      display: flex;
      cursor: pointer;
      height: 60px;
      align-items: center;
      justify-content: center;

      span {
        font-size: 16px;
      }
    }

    .menu-add-btn-selected {
      color: #05ba5b;
      border: 1px dashed #05ba5b;
    }

    .menu-add-btn-border {
      height: 30px;
      border-left: 1px solid #f0f0f1;
    }
  }
}

.sub-btn {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;

  span {
    cursor: pointer;
    width: 120px;
    height: 35px;
    display: block;
    text-align: center;
    color: white;
    line-height: 35px;
    background-color: #1abaf7;
    border-radius: 5px;
  }
}
</style>
