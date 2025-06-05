<template>
  <view>
     <SafeAreaWrapper title="我的问答" :childPage="true">
      <view class="infoCard">
        <view class="header">
          <view
            class="select"
            :class="pageParams.type === 1 ? 'active' : ''"
            @click="handleSelectClick(1)"
            >我提问的</view
          >
          <view
            class="select"
            :class="pageParams.type === 2 ? 'active' : ''"
            @click="handleSelectClick(2)"
            >我回答的</view
          >
        </view>
        <scroll-view
          v-if="pageParams.type === 1"
          scroll-y
          enable-back-to-top
          class="scroll-view"
        >
          <view v-if="questionData.total > 0">
            <resourceBonusInfo
              v-for="item in questionData.list"
              :key="item.pkId"
              :info="item"
            />
          </view>
          <view class="empty-list" v-else>—— 暂无数据 ——</view>
        </scroll-view>
        <scroll-view scroll-y class="exchangeList" v-else>
          <view v-if="answerData.total > 0">
            <resourceActionInfo
              v-for="item in answerData.list"
              :key="item.pkId"
              :info="item"
            />
          </view>
          <view class="empty-list" v-else>—— 暂无数据 ——</view>
        </scroll-view>
      </view>
    </SafeAreaWrapper>

  </view>
</template>

<script setup>
import { ref } from 'vue'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
/**
 * 页面参数
 */
const pageParams = ref({
  page: 1,
  limit: 10,
  type: 1,
})

/**
 *  类型点击切换
 * @param type
 */
const handleSelectClick = (type) => {
  pageParams.value.type = type
}


const questionData = ref({
  list: [],
  total: 0,
})

const answerData = ref({
  list: [],
  total: 0,
})
</script>

<style lang="scss" scoped>
.infoCard {
    margin-top: 20rpx;
    border-top-left-radius: 20rpx;
    border-top-right-radius: 20rpx;
    background: #fff;
    .header {
      display: flex;
      align-items: center;
      padding: 26rpx 10rpx;
      border-bottom: 1px solid #eee;
      .select {
        flex: 1;
        text-align: center;
        font-size: 28rpx;
        font-weight: 700;
        padding-bottom: 6rpx;
      }
      .active {
        position: relative;
        &::after {
          content: '';
          position: absolute;
          bottom: -20rpx;
          left: 50%;
          transform: translateX(-50%);
          height: 8rpx;
          width: 100rpx;
          background: rgb(0, 204, 255);
          border-radius: 20rpx;
        }
      }
    }
  }
  .empty-list {
    height: 100%;
    width: 100vw;
    text-align: center;
    padding: 200rpx 0;
    color: #949494;
    font-size: 36rpx;
  }
</style>
