import React, { useState, useEffect } from 'react';
import Taro from '@tarojs/taro';
import { View, Text, Image, Button, Input } from '@tarojs/components';
import { authAPI } from '../../services/api';
import { getUserInfo } from '../../utils/auth';
import './index.css';

export default function EditProfilePage() {
  const [nickname, setNickname] = useState('');
  const [avatarUrl, setAvatarUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    const info = getUserInfo();
    if (info) {
      setUserInfo(info);
      setNickname(info.nickname || '');
      setAvatarUrl(info.avatarUrl || '');
    }
  }, []);

  const handleChooseAvatar = async () => {
    try {
      const res = await Taro.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera']
      });
      
      const tempFilePath = res.tempFilePaths[0];
      
      Taro.showLoading({ title: '上传中...' });
      
      const uploadRes = await Taro.uploadFile({
        url: 'http://localhost:8080/api/v1/auth/upload-avatar',
        filePath: tempFilePath,
        name: 'file',
        header: {
          'Authorization': `Bearer ${Taro.getStorageSync('token')}`
        }
      });
      
      Taro.hideLoading();
      
      const result = JSON.parse(uploadRes.data);
      if (result.code === 200) {
        setAvatarUrl(result.data);
        Taro.showToast({ title: '头像上传成功', icon: 'success' });
      } else {
        Taro.showToast({ title: result.message || '上传失败', icon: 'none' });
      }
    } catch (error) {
      Taro.hideLoading();
      console.error('选择头像失败:', error);
      Taro.showToast({ title: '选择头像失败', icon: 'none' });
    }
  };

  const handleSave = async () => {
    if (!nickname.trim()) {
      Taro.showToast({ title: '昵称不能为空', icon: 'none' });
      return;
    }

    try {
      setLoading(true);
      
      await authAPI.updateUserInfo({
        nickname: nickname.trim(),
        avatarUrl: avatarUrl || userInfo?.avatarUrl
      });
      
      const updatedUserInfo = {
        ...userInfo,
        nickname: nickname.trim(),
        avatarUrl: avatarUrl || userInfo?.avatarUrl
      };
      
      Taro.setStorageSync('userInfo', JSON.stringify(updatedUserInfo));
      
      Taro.showToast({ title: '保存成功', icon: 'success' });
      
      setTimeout(() => {
        Taro.navigateBack();
      }, 1500);
    } catch (error) {
      Taro.showToast({ title: error.message || '保存失败', icon: 'none' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <View className='edit-profile-page'>
      <View className='edit-profile-header'>
        <Text className='edit-profile-title'>编辑个人资料</Text>
      </View>

      <View className='avatar-section'>
        <View className='avatar-wrapper' onClick={handleChooseAvatar}>
          <Image 
            src={avatarUrl || userInfo?.avatarUrl || 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix'} 
            mode='aspectFill' 
            className='avatar-image'
          />
          <View className='avatar-edit-badge'>
            <Text>✏️</Text>
          </View>
        </View>
        <Text className='avatar-tip'>点击头像更换</Text>
      </View>

      <View className='form-section'>
        <View className='form-item'>
          <Text className='form-label'>昵称</Text>
          <Input
            className='form-input'
            placeholder='请输入昵称'
            value={nickname}
            onInput={(e) => setNickname(e.detail.value)}
            placeholderStyle='color: #999'
            maxLength={20}
          />
        </View>
      </View>

      <View className='action-section'>
        <Button 
          className='save-btn'
          onClick={handleSave}
          loading={loading}
        >
          保存修改
        </Button>
      </View>
    </View>
  );
}
