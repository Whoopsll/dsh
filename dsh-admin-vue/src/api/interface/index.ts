// * 请求响应参数(不包含data)
export interface Result {
  code: string
  msg: string
}

// * 请求响应参数(包含data)
export interface ResultData<T = any> extends Result {
  data: T
}

// * 分页响应参数
export interface ResPage<T> {
  list: T[]
  page: number
  limit: number
  total: number
}

// * 分页请求参数
export interface ReqPage {
  page: number
  limit: number
}

// * 登录模块
export namespace Login {
  export interface ReqLoginForm {
    username: string
    password: string
    // key: string
    // captcha: string
  }
  export interface ResLogin {
    access_token: string
  }
  // export interface ResLoginCaptcha {
  //   key: string
  //   image: string
  // }
}

// * 管理员管理模块
export namespace SysManager {
  export interface ReqGetManagerParams extends ReqPage {
    username?: string
  }
  export interface ResManagerList {
    pkId: number
    username: string
    avatar: string
    status: number
    createTime: string
    publisherIdList?: number[]
    publisherList?: string[]
  }
  export interface ReqEditManagerParams {
    pkId?: number
    username: string
    avatar: string
    status: number
    roleId: number
    publisherIdList?: number[]
    password?: string
  }

  export interface ReqEditPasswordParams {
    password?: string
    code?: string
  }
}

// * 角色模块
export namespace SysRole {
  export interface ReqGetRoleParams extends ReqPage {
    name?: string
  }
  export interface ResRoleList {
    pkId: number
    name: string
    remark: string
    menuIds: number[]
    createTime: string
  }
  export interface ReqEditRoleParams {
    pkId?: number
    name: string
    remark: string
    menuIds: number[]
  }
}

// * 菜单模块
export namespace SysMenu {
  export interface ReqLoginForm {
    username: string
    password: string
  }
  export interface ResMenuList {
    title: string
    pkId: number
    pid: number
    path: string
    name: string
    icon: string
    component?: string
    type: 'MENU_DIR' | 'MENU' | 'BUTTON'
    openType: 'TAB' | 'URL'
    url?: string
    auth?: string
    keepalive: boolean
    sort: number
    parentName: String
    children: ResMenuList[]
    hide?: boolean
  }
}

// * 类型
export namespace SysType {
  export interface ResTypeList {
    name: string
    id: number
    parentId: number
    description: string
    children: ResTypeList[]
  }
}

// * 文件上传模块
export namespace Upload {
  export interface MinioUpload {
    bucketName: string
    file: FormData[]
  }

  export interface ResFileUrl {
    bucketName: string
    urlList: string[]
  }
}

// * 字典
export interface Dict {
  id: number
  title: string
  updateTime: string
  value: number
}

/** 字典设置 */
export interface DictConfigRes {
  createTime: string
  dictNumber: string
  id: number
  title: string
  updateTime: string
  value: number
}

export namespace Book {
  export interface BookDTO {
    id: number
    bookName: string
    isbn: string
    classificationId: number
    publishingHouse: string
    translator: string
    writer: string
    price: number
    pageNumber: number
    bookStyle: string
    publishDate: string
    authorIntroduction: string
    bookIntroduction: string
    bookCover: string
    forumLink: string
  }

  export interface BookQuery extends ReqPage {
    bookName: string
  }

  export interface BookVO {
    id: number
    bookName: string
    isbn: string
    classification?: string
    classificationId?: number
    publishingHouse: string
    translator: string
    writer: string
    price: number
    pageNumber: number
    bookStyle: string
    publishDate: string
    authorIntroduction: string
    bookIntroduction: string
    bookCover: string
    forumLink: string
    createTime: string
  }

  export interface BookExchangeQuery extends ReqPage {
    bookId: number
  }

  export interface BookExchange {
    id: number
    bookId: number
    userFlag: number
    status: number
    verifyCode: string
    qrCodeLink: string
    exchangeTime: string
    createTime: string
  }

  export interface BookExchangeVO {
    id: number
    bookId: number
    userFlag: number
    status: number
    verifyCode: string
    bookLink: string
    qrCodeLink: string
    exchangeTime: string
    createTime: string
  }

  export interface BookExchangeDTO {
    bookId: number
    count: number
    redirectUrl: string
  }
}

export namespace BookResource {
  export interface BookResourceBO {
    id: number
    bookId: number
    userId: number
    link: string
    resourceName: string
    secret: string
  }
  export interface BookResourceQuery extends ReqPage {
    bookId: number
  }
  export interface AuditResourceBO {
    id: number
    bookId: number
    bookName: string
    bookCover: string
    resourceName: string
    link: string
    isChanged: number
  }
}

export namespace Audit {
  export interface AuditDTO {
    id: number
    name: string
    type: number
    status: number
  }

  export interface AuditVO {
    id: number
    name: string
    type: number
    status: number
    createTime: string
  }
}

export namespace Config {
  export interface ConfigDTO {
    id: number
    name: string
    account: string
    password: string
    publisherId: number
  }

  export interface ConfigVO {
    id: number
    name: string
    account: string
    password: string
    publisherId: number
    createTime: string
  }
}

export namespace Category {
  export interface CategoryDTO {
    id: number
    name: string
    description: string
  }
  export interface CategoryVO {
    id: number
    name: string
    description: string
    createTime: string
  }
  export type CategoryQuery = ReqPage
}

export namespace Feedback {
  export type FeedbackQuery = ReqPage
  export interface FeedbackVO {
    id: number
    userId: number
    username: string
    content: string
    img?: string
    createTime: string
  }
}

export namespace Forum {
  export interface ForumDTO {
    id?: number
    userId?: number
    name?: string
    courseLink?: string
    bookId?: number
    price?: number
    writerIntroduction?: string
    voiceIntroduction?: string
    cover?: string
    avatar?: string
  }
  export interface ForumVO {
    id?: number
    userId?: number
    username?: string
    name?: string
    courseLink?: string
    bookId?: number
    bookName?: string
    price?: number
    userNumber?: number
    writerIntroduction?: string
    voiceIntroduction?: string
    categoryName?: string
    cover?: string
    avatar?: string
    createTime?: string
  }
  export interface ForumQuery extends ReqPage {
    forumName?: string
    forumAdmin?: string
  }
}

export namespace Notice {
  export interface NoticeQuery extends ReqPage {
    keyword?: string
    forumId?: number
  }

  export interface NoticeDTO {
    id?: number
    forumId?: number
    title?: string
    content?: string
    validTime?: Date
    isTop: number
  }
  export interface NoticeVO {
    id?: number
    forumId?: number
    forumName?: string
    adminId?: number
    adminName?: string
    title?: string
    content?: string
    validTime?: Date
    isTop: number
    createTime: string
  }
  export interface ForumNoticeVO {
    id?: number
    adminId?: number
    adminName?: string
    title?: string
    content?: string
    validTime?: Date
    isTop: number
    createTime: string
  }
}

export namespace Publisher {
  export interface PublisherDTO {
    id: number
    name: string
    description: string
  }
  export interface PublisherVO {
    id: number
    name: string
    description: string
    createTime: string
  }
  export type PublisherQuery = ReqPage
}

export namespace User {
  export type UserQuery = ReqPage
  export interface UserVO {
    id: number
    wxId: string
    avatar: string
    wxName: string
    createTime: string
  }
}

export namespace Comment {
  export interface CommentItemVO {
    id: number
    type: number
    content: string
    fileType: number
    files: string[]
    userId: number
    username: string
    avatar: string
    createTime: string
    enabled: number
    replyNum: number
    replyList: CommentItemVO[]
  }
}
