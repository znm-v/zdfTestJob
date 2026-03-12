import React, { useState, useEffect } from 'react';
import Taro from '@tarojs/taro';
import { View, Text, Input, Picker } from '@tarojs/components';
import { Page } from '../../types';
import { adminAPI } from '../../services/api';
import './index.css';

const AdminPage = ({ points, menu, shop, onNavigate, onAddPoints, onAddFood, onAddGift, onRemoveItem }) => {
  const [ptsInput, setPtsInput] = useState('');
  const [foodName, setFoodName] = useState('');
  const [foodPts, setFoodPts] = useState('');
  const [foodDesc, setFoodDesc] = useState('');
  const [foodImg, setFoodImg] = useState('');
  const [giftName, setGiftName] = useState('');
  const [giftPts, setGiftPts] = useState('');
  const [users, setUsers] = useState([]);
  const [selectedUserIndex, setSelectedUserIndex] = useState(0);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const userList = await adminAPI.getUsers();
      setUsers(userList);
    } catch (error) {
      console.error('获取用户列表失败:', error);
    }
  };

  const handleAddPoints = () => {
    const val = parseInt(ptsInput);
    if (val > 0 && users.length > 0) {
      const selectedUser = users[selectedUserIndex];
      onAddPoints(val, selectedUser.id);
      setPtsInput('');
    }
  };

  const handleAddFood = () => {
    const p = parseInt(foodPts);
    if (foodName && p) {
      onAddFood({
        name: foodName,
        points: p,
        desc: foodDesc || '美味食物',
        img: foodImg || 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400',
        category: '热门'
      });
      setFoodName('');
      setFoodPts('');
      setFoodDesc('');
      setFoodImg('');
    }
  };

  const handleAddGift = () => {
    const p = parseInt(giftPts);
    if (giftName && p) {
      onAddGift({
        name: giftName,
        points: p,
        img: 'https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=400'
      });
      setGiftName('');
      setGiftPts('');
    }
  };

  return (
    <View className='admin-page'>
      {/* 返回按钮 */}
      <View className='admin-header' onClick={() => onNavigate(Page.PROFILE)}>
        <Text className='admin-back'>←</Text>
        <Text className='admin-header-text'>返回个人中心</Text>
      </View>

      <Text className='admin-title'>系统后台管理</Text>

      {/* 当前积分显示 */}
      <View className='admin-current-points'>
        <Text className='admin-current-points-label'>当前系统积分</Text>
        <Text className='admin-current-points-value'>{points.toLocaleString()}</Text>
      </View>

      {/* 积分发放 */}
      <View className='admin-section'>
        <View className='admin-section-title'>
          <View className='admin-dot gold'></View>
          <Text>积分发放</Text>
        </View>
        <View className='admin-card'>
          {/* 用户选择 */}
          {users.length > 0 && (
            <View className='admin-user-select'>
              <Text className='admin-label'>选择用户:</Text>
              <Picker
                mode='selector'
                range={users}
                rangeKey='nickname'
                value={selectedUserIndex}
                onChange={(e) => setSelectedUserIndex(e.detail.value)}
              >
                <View className='admin-picker'>
                  <Text className='admin-picker-text'>
                    {users[selectedUserIndex]?.nickname || '请选择'} 
                    ({users[selectedUserIndex]?.personalPoints || 0} 积分)
                  </Text>
                  <Text className='admin-picker-arrow'>▼</Text>
                </View>
              </Picker>
            </View>
          )}
          
          <Input
            type='number'
            value={ptsInput}
            onInput={(e) => setPtsInput(e.detail.value)}
            placeholder='输入分值'
            placeholderStyle='color: #555555; font-size: 28rpx;'
            className='admin-input'
          />
          <View className='admin-btn' onClick={handleAddPoints}>
            <Text>确认发放积分</Text>
          </View>
        </View>
      </View>

      {/* 上架新食物 */}
      <View className='admin-section'>
        <View className='admin-section-title'>
          <Text className='admin-section-icon pink'>+</Text>
          <Text>上架新食物</Text>
        </View>
        <View className='admin-card'>
          <Input
            value={foodName}
            onInput={(e) => setFoodName(e.detail.value)}
            placeholder='食物名称'
            placeholderStyle='color: #555555; font-size: 28rpx;'
            className='admin-input'
          />
          <Input
            type='number'
            value={foodPts}
            onInput={(e) => setFoodPts(e.detail.value)}
            placeholder='积分售价'
            placeholderStyle='color: #555555; font-size: 28rpx;'
            className='admin-input'
          />
          <Input
            value={foodImg}
            onInput={(e) => setFoodImg(e.detail.value)}
            placeholder='图片URL (选填)'
            placeholderStyle='color: #555555; font-size: 28rpx;'
            className='admin-input'
          />
          <Input
            value={foodDesc}
            onInput={(e) => setFoodDesc(e.detail.value)}
            placeholder='简短描述'
            placeholderStyle='color: #555555; font-size: 28rpx;'
            className='admin-input'
          />
          <View className='admin-btn' onClick={handleAddFood}>
            <Text>确认上架食物</Text>
          </View>
        </View>
      </View>

      {/* 上架新礼品 */}
      <View className='admin-section'>
        <View className='admin-section-title'>
          <Text className='admin-section-icon orange'>🎁</Text>
          <Text>上架新礼品</Text>
        </View>
        <View className='admin-card'>
          <Input
            value={giftName}
            onInput={(e) => setGiftName(e.detail.value)}
            placeholder='礼品名称'
            placeholderStyle='color: #555555; font-size: 28rpx;'
            className='admin-input'
          />
          <Input
            type='number'
            value={giftPts}
            onInput={(e) => setGiftPts(e.detail.value)}
            placeholder='需消耗积分'
            placeholderStyle='color: #555555; font-size: 28rpx;'
            className='admin-input'
          />
          <View className='admin-btn' onClick={handleAddGift}>
            <Text>确认上架礼品</Text>
          </View>
        </View>
      </View>

      {/* 存量管理 */}
      <View className='admin-inventory-section'>
        <View className='admin-inventory-title'>
          <Text className='admin-section-icon'>📋</Text>
          <Text>存量管理 (点击下架)</Text>
        </View>

        <Text className='admin-sub-title'>食物管理</Text>
        <View className='admin-inventory-list'>
          {menu.map(item => (
            <View key={item.id} className='admin-inventory-item'>
              <Text className='admin-inventory-name'>{item.name}</Text>
              <View className='admin-remove-btn' onClick={() => onRemoveItem('food', item.id)}>
                <Text>下架</Text>
              </View>
            </View>
          ))}
        </View>

        <Text className='admin-sub-title'>礼品管理</Text>
        <View className='admin-inventory-list'>
          {shop.map(item => (
            <View key={item.id} className='admin-inventory-item'>
              <Text className='admin-inventory-name'>{item.name}</Text>
              <View className='admin-remove-btn' onClick={() => onRemoveItem('gift', item.id)}>
                <Text>下架</Text>
              </View>
            </View>
          ))}
        </View>
      </View>
    </View>
  );
};

export default AdminPage;
