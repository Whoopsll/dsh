<template>
  <div class="container">
    <div :class="[isHighLight ? 'highLight' : 'top']">
      <el-avatar :src="commentItem.avatar" :size="50" />
      <div class="nickname">{{ commentItem.username }}</div>
    </div>
    <div class="content">
      <div class="textBar">{{ commentItem.content }}</div>
      <div class="pic" v-show="commentItem.fileType === 0 && commentItem.files.length > 0">
        <template v-for="(item, index) in commentItem.files" :key="index">
          <el-image class="img" :src="item"></el-image>
        </template>
      </div>
      <div class="mic" v-show="commentItem.fileType === 1 && commentItem.files.length > 0">
        <audio controls :src="commentItem.files[0]" class="audio-mic"></audio>
      </div>
    </div>
    <div class="bottom" v-show="control">
      <div>{{ commentItem.createTime }} 回答{{ commentItem.replyNum }}次</div>
      <div>
        <el-button type="primary" @click="enabledChange">{{ commentItem.enabled === 0 ? '屏蔽' : '取消屏蔽' }}</el-button>
        <el-button type="primary" @click="showMore">查看</el-button>
        <el-button type="danger" @click="handleRemove">删除</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Comment } from '@/api/interface/index'
import { defineEmits } from 'vue'

interface Props {
  control?: boolean
  isHighLight?: boolean
  commentItem: Comment.CommentItemVO
}

const props = withDefaults(defineProps<Props>(), {
  control: true,
  isHighLight: false
})

const showMore = () => {
  emit('detail', props.commentItem)
}

const enabledChange = () => {
  emit('enabled', props.commentItem.id)
}

const handleRemove = async () => {
  // 发出删除事件，并传递评论项的 ID
  emit('remove', props.commentItem.id)
}

const emit = defineEmits(['remove', 'detail', 'enabled'])
</script>

<style scoped lang="less">
.container {
  padding: 30px;
  width: 70vw;

  .top {
    display: flex;
    align-items: center;
    padding: 10px;
    background-color: #dddddd;
    color: #46a6ff;

    .nickname {
      font-size: 24px;
      margin: 0 10px;
    }
  }

  .highLight {
    display: flex;
    align-items: center;
    padding: 10px;
    background-color: #66b5ff;
    color: #ffffff;

    .nickname {
      font-size: 24px;
      margin: 0 10px;
    }
  }

  .content {
    display: flex;
    flex-direction: column;
    padding: 20px;
    min-height: 300px;

    .textBar {
      font-size: 18px;
      line-height: 1.2;
      margin: 20px 0;
    }

    .pic {
      display: flex;
      flex-wrap: wrap;

      .img {
        width: 150px;
        height: 150px;
        margin: 0 5px;
      }
    }

    .mic {
      .audio-mic {
      }
    }
  }

  .bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    background-color: #dddddd;
  }
}
</style>
