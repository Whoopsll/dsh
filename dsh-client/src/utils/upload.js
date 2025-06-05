export const uploadFile = (filePath) => {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: 'http://8.153.16.66/dsh-client-api/common/upload/img', // 你的后端接口
      name: 'file', // 后端字段名
      filePath: filePath,
      success: (res) => {
        if (res.statusCode === 200) {
          let data = JSON.parse(res.data);
          resolve(data); // 返回后端的结果
        } else {
          uni.showToast({ icon: 'error', title: '上传失败' });
          reject(new Error('上传失败'));
        }
      },
      fail: (err) => {
        uni.showToast({ icon: 'error', title: '请求失败' });
        reject(err);
      }
    });
  });
};
