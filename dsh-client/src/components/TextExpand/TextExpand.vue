<template>
	<view>
		<view class="showHidden" :style="{ maxHeight: showDisplay ? centerHeight : defaultHeight }">
			<view id="centerBox">
				<view class="hide-preCode-box" :style="{ height: showDisplay ? '0' : vagueHeight,}"></view>
				<slot />
			</view>
		</view>
		<view class="unfold" @click.stop="changeShowDisplay">
			<text style="color: #007aff;margin-left:10rpx">{{ showDisplay ? hideText : showText }}</text>
      <uni-icons :type="showDisplay ? 'up' : 'down'" size="30" color="#007aff"></uni-icons>
		</view>
	</view>
</template>
<script setup>
import { ref } from 'vue';

// Props
const props = defineProps({
  showText: {
    type: String,
    default: '展开更多',
  },
  hideText: {
    type: String,
    default: '收起',
  },
  defaultHeight: {
    type: String,
    default: '300rpx',
  },
  canShow: {
    type: Boolean,
    default: true,
  },
  vagueHeight: {
    type: String,
    default: '200rpx',
  },
});

// Data
const showDisplay = ref(false);
const centerHeight = ref(0);

// Methods
const px2rpx = (px) => {
  const systemInfo = uni.getSystemInfoSync()
  return px / (systemInfo.screenWidth / 750);
};

const emit = defineEmits(['change'])

const changeShowDisplay = () => {
  if (!props.canShow) return;
  bindWindowHeight()
  showDisplay.value = !showDisplay.value;
  emit('change', showDisplay.value);
};

const bindWindowHeight = () => {
  const view = uni.createSelectorQuery().select('#centerBox');
  view.boundingClientRect((res) => {
    centerHeight.value = res ? px2rpx(res.height) + 'rpx' : '';
  }).exec();
}
</script>

<style lang="scss" scoped>
	.showHidden {
		overflow: hidden;
		transition: max-height 0.5s, opacity 0.5s;
		position: relative;
	}

	.hide-preCode-box {
		width: 100%;
		background-image: -webkit-gradient(linear, left top, left bottom, from(rgba(255, 255, 255, 0)), to(#fff));
		background-image: linear-gradient(-180deg, rgba(255, 255, 255, 0) 0%, #fff 100%);
		position: absolute;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: 10;
	}

	.unfold {
		text-align: center;
		height: 64rpx;
		font-size: 28rpx;
		color: #999999;
		line-height: 64rpx;
		margin: 16rpx 0;
		display: flex;
		justify-content: center;

		.unfold-icon {
			width: 28rpx;
			height: 28rpx;
			margin: auto 0;
		}
	}
</style>
