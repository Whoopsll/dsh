<template>
  <div class="home">
    <div style="display: flex">
      <div class="head-left">
        <div class="head-left-top">
          <div class="count-card">
            <div class="icon">
              <img src="@/assets/images/home/icon_book.png" />
            </div>
            <div class="content">
              <span style="font-size: 12px; color: #e3eafe">书籍总数</span>
              <span style="font-size: 20px; line-height: 20px; margin-top: 3px">40</span>
            </div>
          </div>
          <div class="count-card" style="background-color: #ff9623">
            <div class="icon">
              <img src="@/assets/images/home/icon_notice.png" />
            </div>
            <div class="content">
              <span style="font-size: 12px; color: #e3eafe">帖子总数</span>
              <span style="font-size: 20px; line-height: 20px; margin-top: 3px">288</span>
            </div>
          </div>
          <div class="count-card" style="width: 25%; background-color: #f72a2a">
            <div class="icon">
              <img src="@/assets/images/home/icon_resource.png" />
            </div>
            <div class="content">
              <span style="font-size: 12px; color: #e3eafe">资源总数</span>
              <span style="font-size: 20px; line-height: 20px; margin-top: 3px">632</span>
            </div>
            <div class="content-sub">
              <span style="font-size: 11px; margin-top: 5px; color: #e3eafe">当前已使用</span>
              <span style="font-size: 16px; margin-top: 1px">320</span>
            </div>
          </div>
          <div class="count-card" style="width: 35%; background-color: #ff7544">
            <div class="icon">
              <img src="@/assets/images/home/icon_user.png" />
            </div>
            <div class="content">
              <span style="font-size: 12px; color: #e3eafe">用户总数</span>
              <span style="font-size: 20px; line-height: 20px; margin-top: 3px">212</span>
            </div>
            <div style="display: flex; flex: 1; margin-right: 10px; justify-content: flex-end">
              <div class="content-sub">
                <span style="font-size: 11px; margin-top: 5px; color: #e3eafe">昨日新增</span>
                <span style="font-size: 16px; margin-top: 1px">10</span>
              </div>
              <div class="content-sub">
                <span style="font-size: 11px; margin-top: 5px; color: #e3eafe">本月新增</span>
                <span style="font-size: 16px; margin-top: 1px">112</span>
              </div>
            </div>
          </div>
        </div>
        <div class="head-left-bottom">
          <div class="bottom-card">
            <span class="title-font">24</span>
            <span class="label-font">公告数量</span>
          </div>
          <div class="bottom-card">
            <span class="title-font">30</span>
            <span class="label-font">论坛数量</span>
          </div>
          <div class="bottom-card">
            <span class="title-font">168</span>
            <span class="label-font">图片审核数量</span>
          </div>
          <div class="bottom-card">
            <span class="title-font">128</span>
            <span class="label-font">文档审核数量</span>
          </div>
          <div class="bottom-card" style="border-right: 0px solid #f1f1f1">
            <span class="title-font">228</span>
            <span class="label-font">视频审核数量</span>
          </div>
        </div>
      </div>
      <div class="head-right">
        <div style="width: 100%; border-radius: 10px; margin-top: 10px; background-color: white; padding: 20px 10px">
          <div style="color: #1f64fa; font-size: 18px; font-weight: bold; display: flex; align-items: center">
            <img src="@/assets/images/home/icon_service.png" style="width: 18px; height: 18px; margin-right: 5px" alt="" />
            服务列表
          </div>
          <div style="margin-left: 25px">
            <div style="justify-content: space-between; display: flex; margin-top: 10px">
              <div style="font-size: 14px">二维码自动审核服务</div>
              <div style="margin-right: 20px; font-size: 14px; display: flex; align-items: center">
                <img src="@/assets/images/icon_run.png" style="width: 14px; height: 14px; margin-right: 5px" />
                运行中
              </div>
            </div>
            <div style="justify-content: space-between; display: flex; margin-top: 10px">
              <div style="font-size: 14px">第三方资源网址内容审核</div>
              <div style="margin-right: 20px; font-size: 14px; display: flex; align-items: center">
                <img src="@/assets/images/icon_stop.png" style="width: 14px; height: 14px; margin-right: 5px" />
                未启动
              </div>
            </div>
            <div style="justify-content: space-between; display: flex; margin-top: 10px">
              <div style="font-size: 14px">外部网址审核服务</div>
              <div style="margin-right: 20px; font-size: 14px; display: flex; align-items: center">
                <img src="@/assets/images/icon_run.png" style="width: 14px; height: 14px; margin-right: 5px" />
                运行中
              </div>
            </div>
            <div style="justify-content: space-between; display: flex; margin-top: 10px">
              <div style="font-size: 14px">第三方资源网址内容审核</div>
              <div style="margin-right: 20px; font-size: 14px; display: flex; align-items: center">
                <img src="@/assets/images/icon_stop.png" style="width: 14px; height: 14px; margin-right: 5px" />
                未启动
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 中间统计图 -->
    <div style="display: flex">
      <div class="left" style="width: 65%">
        <div style="display: flex; width: 100%">
          <div id="hotBookEchart" style="width: 55%; height: 300px"></div>
          <div id="bookEchart" style="width: 45%; height: 300px"></div>
        </div>
        <div style="display: flex">
          <div id="userEchart" style="width: 100%; height: 300px"></div>
          <div id="weekPostEchart" style="width: 100%; height: 300px"></div>
        </div>
      </div>
      <div class="right" style="width: 35%; margin-right: 20px">
        <div style="width: 100%; border-radius: 10px; background-color: white; padding: 20px 10px; margin-top: 10px">
          <div style="color: #f61f29; font-size: 18px; font-weight: bold; display: flex; align-items: center">
            <img src="@/assets/images/home/icon_post.png" style="width: 20px; height: 20px; margin-right: 5px" alt="" />
            公告信息
          </div>
          <div style="margin-left: 25px">
            <div style="justify-content: space-between; display: flex; margin-top: 10px">
              <div style="font-size: 14px">今日更新版本至2.10，该版本优化了帖子的评论功能</div>
              <div style="margin-right: 20px; font-size: 14px; display: flex; align-items: center"> 2024-11-18 20:00 </div>
            </div>
            <div style="justify-content: space-between; display: flex; margin-top: 10px">
              <div style="font-size: 14px">今日更新版本至2.09，该版本优化了图书的详情界面及论坛</div>
              <div style="margin-right: 20px; font-size: 14px; display: flex; align-items: center"> 2024-11-15 21:00 </div>
            </div>
            <div style="justify-content: space-between; display: flex; margin-top: 10px">
              <div style="font-size: 14px">今日更新版本至2.08，该版本优化了用户扫面资源码跳转</div>
              <div style="margin-right: 20px; font-size: 14px; display: flex; align-items: center"> 2024-11-13 19:00 </div>
            </div>
            <div style="justify-content: space-between; display: flex; margin-top: 10px">
              <div style="font-size: 14px">今日更新版本至2.07，该版本优化了用户扫码登陆的相关功能</div>
              <div style="margin-right: 20px; font-size: 14px; display: flex; align-items: center"> 2024-11-10 22:00 </div>
            </div>
          </div>
        </div>
        <div style="display: flex; margin-top: 20px">
          <div id="myEchart" style="width: 100%; height: 320px; padding: 0; margin-left: 50px"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="home">
import * as echarts from 'echarts'
import { onMounted } from 'vue'
onMounted(() => {
  const myEchartDiv = document.getElementById('myEchart')
  const myEchart = echarts.init(myEchartDiv)
  // 用户新增折线图
  const userEchartDiv = document.getElementById('userEchart')
  const userEchart = echarts.init(userEchartDiv)
  const weekPostEchartDiv = document.getElementById('weekPostEchart')
  const weekPostEchart = echarts.init(weekPostEchartDiv)
  // 七天发帖量柱状图
  const hotBookEchartDiv = document.getElementById('hotBookEchart')
  const hotBookEchart = echarts.init(hotBookEchartDiv)
  // Top5书籍阅读量统计
  const bookEchartDiv = document.getElementById('bookEchart')
  const bookEchart = echarts.init(bookEchartDiv)
  // 每日审核使用率统计
  myEchart.setOption({
    title: {
      text: '当日审核使用率统计',
      left: 'center', // 水平居中
      top: '0px', // 垂直居中
      textStyle: {
        fontSize: 16,
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)' // 格式化显示，添加百分比
    },
    legend: {
      orient: 'horizontal', // 横向排列
      bottom: '0px', // 图例在底部
      width: '100%', // 限制图例宽度，自动换行
      itemGap: 20, // 图例项之间的间距
      align: 'auto' // 自适应对齐
    },
    series: [
      {
        name: '当日审核使用率统计',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 1048, name: '网站审核' },
          { value: 735, name: '视频审核' },
          { value: 580, name: '文本文档审核' },
          { value: 484, name: '图片审核' },
          { value: 300, name: '二维码审核' }
        ],
        itemStyle: {
          normal: {
            color: function (params) {
              // 设置每个分布的颜色
              const colorList = ['#4165f4', '#1abff8', '#f61f29', '#51de90', '#ff6841']
              return colorList[params.dataIndex % colorList.length]
            }
          }
        },
        label: {
          // show: false // 去除饼图内部文字
          formatter: '{b}: {d}%' // 标签格式，显示名称和百分比
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })

  // 用户新增折线图
  userEchart.setOption({
    title: {
      text: '七天用户新增趋势图',
      left: 'center',
      top: '20px'
    },
    xAxis: {
      data: ['11.1', '11.2', '11.3', '11.4', '11.5', '11.6', '11.7'],
      axisLine: {
        show: true,
        symbol: ['none', 'arrow'], // 设置x轴右侧箭头
        symbolSize: [10, 15] // 设置箭头的大小
      },
      axisTick: {
        show: false // 关闭刻度线，避免影响整体布局
      },
      axisLabel: {
        show: true,
        margin: 20 // 设置 x 轴标签与轴的内边距，拉开 x 轴与柱状图的距离
      }
    },
    yAxis: {
      axisLine: {
        show: true,
        symbol: ['none', 'arrow'], // 设置y轴上方箭头
        symbolSize: [10, 15] // 设置箭头的大小
      },
      axisLabel: {
        show: false // 去除y轴的数量
      },
      max: 35
    },
    series: [
      {
        data: [10, 22, 28, 23, 19, 10, 13],
        type: 'line',
        smooth: true,
        label: {
          show: true,
          position: 'top', // 显示在柱状图上方
          formatter: '{c}', // 显示柱状图的数值
          fontSize: 14, // 设置文字的字体大小
          color: '#333' // 设置文字颜色
        }
      }
    ]
  })

  // 七天发帖量趋势图
  weekPostEchart.setOption({
    title: {
      text: '七天发帖量趋势图',
      left: 'center',
      top: '20px'
    },
    xAxis: {
      data: ['11.1', '11.2', '11.3', '11.4', '11.5', '11.6', '11.7'],
      axisLine: {
        show: true,
        symbol: ['none', 'arrow'], // 设置x轴右侧箭头
        symbolSize: [10, 15] // 设置箭头的大小
      },
      axisTick: {
        show: false // 关闭刻度线，避免影响整体布局
      },
      axisLabel: {
        show: true,
        margin: 20 // 设置 x 轴标签与轴的内边距，拉开 x 轴与柱状图的距离
      }
    },
    yAxis: {
      axisLine: {
        show: true,
        symbol: ['none', 'arrow'], // 设置y轴上方箭头
        symbolSize: [10, 15] // 设置箭头的大小
      },
      axisLabel: {
        show: false // 去除y轴的数量
      },
      max: 35
    },
    series: [
      {
        data: [10, 22, 28, 23, 19, 10, 3],
        type: 'line',
        smooth: true,
        label: {
          show: true,
          position: 'top', // 显示在柱状图上方
          formatter: '{c}', // 显示柱状图的数值
          fontSize: 14, // 设置文字的字体大小
          color: '#333' // 设置文字颜色
        }
      }
    ]
  })

  // 热门书籍类型发帖量Top6
  hotBookEchart.setOption({
    title: {
      text: '热门书籍类型发帖量Top6',
      left: 'center',
      top: '20px'
    },
    xAxis: {
      data: ['生活', '艺术', '科技', '自然科学', '教育', '农业'],
      axisLine: {
        show: true,
        symbol: ['none', 'arrow'], // 设置x轴右侧箭头
        symbolSize: [10, 15] // 设置箭头的大小
      },
      axisTick: {
        show: false // 关闭刻度线，避免影响整体布局
      },
      axisLabel: {
        show: true,
        margin: 20 // 设置 x 轴标签与轴的内边距，拉开 x 轴与柱状图的距离
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        show: false // 去除y轴的数量
      },
      axisLine: {
        show: true,
        symbol: ['none', 'arrow'], // 设置y轴上方箭头
        symbolSize: [10, 15] // 设置箭头的大小
      },
      max: 25
    },
    series: [
      {
        type: 'bar',
        barWidth: 35,
        label: {
          show: true,
          position: 'top', // 显示在柱状图上方
          formatter: '{c}', // 显示柱状图的数值
          fontSize: 14, // 设置文字的字体大小
          color: '#333' // 设置文字颜色
        },
        data: [
          { value: 8, itemStyle: { color: '#F39C12' } }, // D柱子颜色为黄色
          { value: 12, itemStyle: { color: '#FF5733' } }, // A柱子颜色为红色
          { value: 15, itemStyle: { color: '#3357FF' } }, // C柱子颜色为蓝色
          { value: 17, itemStyle: { color: '#1bbaf7' } }, // E柱子颜色为紫色
          { value: 17, itemStyle: { color: '#f61f29' } }, // E柱子颜色为紫色
          { value: 20, itemStyle: { color: '#00da69' } } // B柱子颜色为绿色
        ]
      }
    ]
  })

  // Top5书籍阅读量统计
  bookEchart.setOption({
    title: {
      text: 'Top5书籍阅读量统计',
      left: 'center',
      top: '20px'
    },
    xAxis: {
      data: ['野草', '骆驼祥子', '视听语言', '天龙八部', '白夜行'],
      axisLine: {
        show: true,
        symbol: ['none', 'arrow'], // 设置x轴右侧箭头
        symbolSize: [10, 15] // 设置箭头的大小
      },
      axisTick: {
        show: false // 关闭刻度线，避免影响整体布局
      },
      axisLabel: {
        show: true,
        margin: 20 // 设置 x 轴标签与轴的内边距，拉开 x 轴与柱状图的距离
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        show: false // 去除y轴的数量
      },
      axisLine: {
        show: true,
        symbol: ['none', 'arrow'], // 设置y轴上方箭头
        symbolSize: [10, 15] // 设置箭头的大小
      },
      max: 25
    },
    series: [
      {
        data: [
          { value: 8, itemStyle: { color: '#F39C12' } }, // D柱子颜色为黄色
          { value: 12, itemStyle: { color: '#FF5733' } }, // A柱子颜色为红色
          { value: 15, itemStyle: { color: '#3357FF' } }, // C柱子颜色为蓝色
          { value: 17, itemStyle: { color: '#9B59B6' } }, // E柱子颜色为紫色
          { value: 20, itemStyle: { color: '#00da69' } } // B柱子颜色为绿色
        ],
        type: 'bar',
        smooth: true,
        barWidth: 35,
        label: {
          show: true,
          position: 'top', // 显示在柱状图上方
          formatter: '{c}', // 显示柱状图的数值
          fontSize: 14, // 设置文字的字体大小
          color: '#333' // 设置文字颜色
        }
      }
    ]
  })
})
</script>

<style scoped lang="less">
.home {
  width: 100%;
  height: 100%;

  .home-bg {
    width: 70%;
    margin-bottom: 20px;
  }
}

.head-left {
  display: flex;
  width: 65%;
  flex-direction: column;

  .head-left-top {
    display: flex;
    justify-content: flex-start;
    margin-top: 10px;
    margin-left: 20px;
    align-items: center;

    .count-card {
      display: flex;
      align-items: center;
      border-radius: 10px;
      margin-right: 20px;
      background-color: #4970fa;
      width: 20%;
      padding: 20px 15px;

      .icon {
        background-color: white;
        padding: 10px;
        width: 45px;
        height: 45px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;

        img {
          width: 35px;
          height: 25px;
        }
      }

      .content {
        display: flex;
        margin-left: 10px;
        flex-direction: column;
        align-items: flex-start;

        span {
          color: white;
          display: block;
        }
      }

      .content-sub {
        display: flex;
        margin-left: 30px;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        span {
          color: white;
          display: block;
        }
      }
    }
  }

  .head-left-bottom {
    display: flex;
    margin: 0px 20px;
    margin-top: 20px;
    background-color: white;
    padding: 20px 0;
    border-radius: 10px;

    .bottom-card {
      width: 25%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border-right: 1px solid #f1f1f1;

      span {
        display: block;
      }

      .label-font {
        color: #828282;
        font-size: 12px;
      }

      .title-font {
        color: black;
        font-size: 20px;
      }
    }
  }
}

.head-right {
  width: 35%;
  margin-right: 20px;
}
</style>
